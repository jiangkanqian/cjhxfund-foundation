package com.cjhxfund.foundation.web.context;

/**
 * 只重写findClass方法,
因为JDK已经在loadClass方法中帮我们实现了ClassLoader搜索类的算法，当在loadClass方法中搜索不到类时，loadClass方法就会调用findClass方法来搜索类，
所以我们只需重写该方法即可。如没有特殊的要求，一般不建议重写loadClass搜索类的算法。
 * @author xiejiesheng
 * 20170504
 */
public class MyClassLoader extends ClassLoader{

	private ClassLoader defaultLoader;
	
	public MyClassLoader(ClassLoader defaultLoader) {
		this.defaultLoader = defaultLoader;
	}

	public ClassLoader getDefaultLoader() {
		return defaultLoader;
	}


	public void setDefaultLoader(ClassLoader defaultLoader) {
		this.defaultLoader = defaultLoader;
	}


	

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return defaultLoader.loadClass(name);
	}

	
	
}
