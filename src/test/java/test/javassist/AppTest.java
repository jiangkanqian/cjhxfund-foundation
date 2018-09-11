package test.javassist;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.cjhxfund.foundation.web.model.HttpCtrl;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class AppTest {

	public static void main(String[] args) {
		 ClassPool pool = ClassPool.getDefault();
		 try {
			CtClass cls = pool.get("test.javassist.Sub");
//			CtMethod cmd = cls.getDeclaredMethod("action");
//			CtMethod cmd = cls.getDeclaredMethod("action");
			StringBuilder body = new StringBuilder();
			body.append("public void action(String method){");
			body.append("System.out.println(\"yrdy\");");
			body.append("if(method.equals(\"add\")){").append("add();").append("}");
			body.append(" else if( method.equals(\"add1\")){").append("add1();").append("}");
			body.append(" else if( method.equals(\"add2\")){").append("add2();").append("}");
			body.append("}");
			CtMethod cmd = CtNewMethod.make(body.toString(), cls);
//			CtField param = new CtField(pool.get("java.lang.String"), "methodN", cls);
			System.out.println(cmd.getLongName());
//			cmd.setBody(body.toString());
			cls.addMethod(cmd);
			cls.writeFile();
			Class clazz = cls.toClass();
			TestSubject instance = (TestSubject) clazz.newInstance();
			instance.action("add");
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
//		TestSubject test = new Sub();
//		test.action("add2");
		
//		Method mm;
//		try {
//			mm = Sub.class.getMethod("action", String.class);
//			mm.invoke(new Sub(), "add");
//		} catch (NoSuchMethodException | SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
