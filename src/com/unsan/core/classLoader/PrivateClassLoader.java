package com.unsan.core.classLoader;

public class PrivateClassLoader extends ClassLoader{

	
	private  final String moduleName ;
	
	public PrivateClassLoader(String moduleName){
		this.moduleName = moduleName;
	}

	public String getModuleName() {
		return moduleName;
	}
	
	

	@Override
	public Class<?> loadClass(String arg0) throws ClassNotFoundException {

		return super.loadClass(arg0);
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
	
		return super.findClass(name);
		
	}

	
	
	
}
