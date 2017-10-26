package com.unsan.core.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	/**
	 * 读取 拓展目录下的unsam*。xml
	 * 
	 * @param re
	 * @param exRootDir
	 */
	public static void readUnsanXml(List<File> re, File exRootDir) {

		if (exRootDir.isFile()) {
			String s = exRootDir.getName();
			if (s.endsWith(".xml") && s.startsWith("Unsan")) {
				re.add(exRootDir);
			}
		} else {
			File[] moduleXml = exRootDir.listFiles();

			for (File f : moduleXml) {
				readUnsanXml(re, f);

			}
		}

	}

	public static String readPrivateLibPath(File fileName) {

		if (fileName.getParent() != null) {
			File[] currentPath = fileName.getParentFile().listFiles();

			for (File f : currentPath) {
				if (f.getName().toLowerCase().equals("privatelib")) {
					return f.getAbsolutePath();
				}

			}
			return readPrivateLibPath(fileName.getParentFile());

		}
		return null;

	}
	
	
	
	public static URL[] searchModuleJarFromXml(String xmlPath) {

		if(xmlPath == null){
			return null;
		}
		File path = new File(xmlPath);
		File parent = path.getParentFile().getParentFile();
		return searchJarURL(parent.getAbsolutePath());
	}

	public static URL[] searchJarURL(String args) {

		if(args == null){
			return null;
		}
		File path = new File(args);
		File[] jarFiles = path.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String s) {
				// TODO Auto-generated method stub
				return (s.endsWith(".jar"));
			}
		});
		List<URL> urllist = new ArrayList<>();
		for (File f : jarFiles) {
			String urlstring = "file:" + f.getAbsoluteFile();
			try {
				urllist.add(new URL(urlstring));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 URL[] url =  urllist.toArray(new URL[urllist.size()]);

		return url;
	}

	public static void main(String[] args) {
		URL[] ceshi = searchModuleJarFromXml("C:\\Users\\Administrator\\Desktop\\Unsan\\Unsan_Robot_External\\wali\\conf\\Unsan_wali.xml");

		System.out.println(ceshi);
	}

}
