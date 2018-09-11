package com.cjhxfund.foundation.annotation;
//package org.cmpy.lanhan.common.annotation;
//
//import java.lang.reflect.Method;
//
//public class Test {
//
//	public static void test(Class clazz){
//		String className=clazz.getName();
//		System.out.println(className);
//		Method[] methods=clazz.getMethods();
//		System.out.println(methods.length);
//		for(Method m:methods){
//			System.out.println(m.getName());
//			
//			
//		}
//	}
//	
//	public static String executeMethodAnno(Method m){
//		if(m.getAnnotation(Author.class)!=null){
//			System.out.println(m);
//			String[] authors=m.getAnnotation(Author.class).value();
//			String typeName=m.getAnnotation(APIType.class).value().toString();
//			Param[] params=m.getAnnotation(Params.class).value();
//			if(m.getAnnotation(SessionType.class)!=null){
//				String sessionDesc=m.getAnnotation(SessionType.class).desc();
//				String sessionType=m.getAnnotation(SessionType.class).value().toString();
//				System.out.println(sessionDesc);
//				System.out.println(sessionType);
//			}
//			for(int i=0;i<authors.length;i++){
//				System.out.println(authors[i]);
//			}
//			for(int i=0;i<params.length;i++){
//				System.out.println(params[i]);
//			}
//			System.out.println(typeName);
//		}
//		return "123";
//	}
//	
//	
//	public static Method getMethod(Class clazz,String name){
//		try {
//			return clazz.getDeclaredMethod(name, String.class);
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	public static String getAnnoMsg(Class clazz,String name){
//		return executeMethodAnno(getMethod(clazz,name));
//	}
//	
//	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
////		test(TestAnno.class);
//		Test t = new Test();
//		
//		System.out.println(getAnnoMsg(TestAnno.class,"test2"));
////		for(InitCacheInfo cacheInfo:InitCacheInfo.values()){
////			System.out.println(cacheInfo.getInitName());
////			System.out.println(cacheInfo.getUrl());
////		}
//	}
//}
