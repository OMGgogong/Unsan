package com.unsan.core.classLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
/**
 * 1.先加载 模块下 <privatelib></privateLib>路径 包下的类
 * 2.如果第1步加载失败则使用AppClassLoader加载的类
 * 
 * 实现的关键：
 * 1.父加载器为null
 * 2.findClass方法报异常使用Class.forName加载
 * @author Administrator
 *
 */
public class PrivateURLClassLoader extends URLClassLoader{
	

	public PrivateURLClassLoader(URL[] arg0) {
		
		super(arg0);
	}

	public PrivateURLClassLoader(URL[] arg0,String version){

		super(arg0,null);
		loadResource(version);
	
	}
	public  PrivateURLClassLoader(URL[] arg0,ClassLoader arg1) {
		super(arg0, arg1);
	}
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return super.loadClass(name);
	}
	
	@Override
	protected Class<?> findClass(String name) {
		
		Class<?> clazzre = null;
		try {
				 clazzre = super.findClass(name);
		} catch (ClassNotFoundException e) {
			try {
				return Class.forName(name,false,new PrivateClassLoader("c"));
			} catch (ClassNotFoundException e1) {
				//System.out.println("调用appclass加载报错:	"+name);
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return clazzre;
	}

	
	private void loadResource(String version){
		
		tryLoadJarInDir(version);
	}

	private void tryLoadJarInDir(String dirPath) {

		File dir = new File(dirPath);
		if(dir.exists()&&dir.isDirectory()){
			for(File file:dir.listFiles()){
				if(file.isFile() && file.getName().endsWith(".jar")){
					this.addURL(file);
					continue;
				}
			}
		}
	}

	private void addURL(File file) {
		
		try {
			super.addURL(new URL("file",null,file.getCanonicalPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
