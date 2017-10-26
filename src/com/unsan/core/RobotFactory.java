package com.unsan.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unsan.core.classLoader.PrivateClassLoader;
import com.unsan.core.classLoader.PrivateURLClassLoader;
import com.unsan.core.constants.UnsanParameters;
import com.unsan.core.robot.BaseRobot;
import com.unsan.core.utils.FileUtils;

public class RobotFactory {
	  private Logger log;
	/*
	 * 机器人
	 */
	private Hashtable<String, BaseRobot> robots = new Hashtable<>();

	/*
	 * 机器人配置
	 */
	private List<Map<String,String>> robotConf = new ArrayList<>();
	
	private  SAXReader read = new SAXReader();
	/**
	 * 单例模式 延迟加载+线程安全
	 * 
	 * @return
	 */
	public static RobotFactory getInstance() {
		return ClientHolder.instance;
	}

	private static class ClientHolder {
		private static RobotFactory instance = new RobotFactory();
	}

	/**
	 * 从零件组装成机器人
	 */
	@SuppressWarnings("unchecked")
	public void buildRobots() {
		 this.log = LoggerFactory.getLogger(getClass());
		 log.info("机器工厂从文件中组装机器人...");
		 
		 //从核心目录下读取
		 readCoreFile();
		 
		 //从拓展目录下读取
		 readExpandFile();
		 // 整理启动顺序
		 
		 Collections.sort(robotConf,new Comparator<Map<String,String>>(){

			@Override
			public int compare(Map<String,String> o1, Map<String,String> o2) {
				double priority1 = 0d;
				double priority2 = 0d;
				if(o1 != null ){
					String o1P = o1.get("loadPriority");
					priority1 = Double.parseDouble(o1P)	;
				}else{
					
				}
				
				if(o2 != null){
					String o2P =  o2.get("loadPriority");
					priority2 = Double.parseDouble(o2P)	;
				}
				int rt = new Double(priority1 - priority2).intValue();
				return rt;
			}
			 
		 });
		 
	}
	
	/*
	 * 从核心目录下获取
	 */
	private void readCoreFile(){
		try {
			read.setEncoding("UTF-8");
			log.debug("核心目录: "+UnsanParameters.RUNTIME_PATH+File.separatorChar+"conf"+File.separatorChar+UnsanParameters.CONFILE);
			Document doc = read.read(new File(UnsanParameters.RUNTIME_PATH+File.separatorChar+"conf"+File.separatorChar+UnsanParameters.CONFILE));
			
		     Element unsan = doc.getRootElement();
		    // Element unsan = root.element("robot");
		     Iterator<?> it = unsan.elementIterator("robot");
		     while (it.hasNext()) {
		    	 Element mod = (Element)it.next();
		    	  Map<String, String> map = new HashMap<String, String>();
		          map.put("name", mod.element("name").getTextTrim());
		          map.put("class", mod.element("class").getTextTrim());
		          if(mod.element("confPath")!=null){
		        	  map.put("confPath", mod.element("confPath").getTextTrim());
		          }
		          if(mod.element("privateLib")!=null){
		        	  map.put("privateLib", mod.element("privateLib").getTextTrim());
		          }
		         
		          if(mod.element("loadPriority")!=null){
		        	  map.put("loadPriority", mod.element("loadPriority").getTextTrim());
		          }else{
		        	  map.put("loadPriority", UnsanParameters.DEFAULT_LOAD_PRIORITY);
		          }
		          if(mod.element("workThreadNum")!=null){
		        	  map.put("workThreadNum", mod.element("workThreadNum").getTextTrim());
		          }else{
		        	  map.put("workThreadNum", UnsanParameters.DEFAULT_THREAD_NUM);
		          }
		          
		          robotConf.add(map);
		     }
		     
		} catch (DocumentException e) {
			log.info("机器工厂 扫描文件位置或者读取失败 详情 "+e.toString());
			e.printStackTrace();
		}
	}
	
	/*
	 * 从拓展目录下获取
	 * 以Unsan开头 以。xml结尾的module
	 */
	private void readExpandFile(){
		//TODO:://从拓展目录下获得机器人
		read.setEncoding("UTF-8");
		log.debug("拓展路径: "+UnsanParameters.External_PATH);
		File exRootDir = new File(UnsanParameters.External_PATH);
		//Document doc = read.read();
		
		 if (!exRootDir.exists()) {
		      this.log.info("无外部模块需要加载");
		      return;
		    }
		 
		 
		 List<File> unsanXml = new ArrayList<>();
		 FileUtils.readUnsanXml(unsanXml, exRootDir);
			for(File fxml: unsanXml ){
				//String privateLib = FileUtils.readPrivateLibPath(fxml);
				try {
		          FileInputStream in = new FileInputStream(fxml);
		          Document doc = read.read(in);
					 Element unsan = doc.getRootElement();
					     Iterator<?> it = unsan.elementIterator("robot");
					     while (it.hasNext()) {
					    	 Element mod = (Element)it.next();
					    	  Map<String, String> map = new HashMap<String, String>();
					          map.put("name", mod.element("name").getTextTrim());
					          map.put("class", mod.element("class").getTextTrim());
					          if(mod.element("confPath")!=null){
					        	  map.put("confPath", mod.element("confPath").getTextTrim());
					          }
					          if(mod.element("privateLib")!=null){
					        	  map.put("privateLib", mod.element("privateLib").getTextTrim());
					          }
					         
					          if(mod.element("loadPriority")!=null){
					        	  map.put("loadPriority", mod.element("loadPriority").getTextTrim());
					          }else{
					        	  map.put("loadPriority", UnsanParameters.DEFAULT_LOAD_PRIORITY);
					          }
					          if(mod.element("workThreadNum")!=null){
					        	  map.put("workThreadNum", mod.element("workThreadNum").getTextTrim());
					          }else{
					        	  map.put("workThreadNum", UnsanParameters.DEFAULT_THREAD_NUM);
					          }
					          map.put("xmlPath", fxml.getAbsolutePath());
					          robotConf.add(map);
					     }
			          
					
					
				} catch (DocumentException | FileNotFoundException e) {
					this.log.error(String.format("获取模块[%s]配置失败", new Object[] { fxml.getName() }), e);
					e.printStackTrace();
				}
		          
		         
				
				
			}
			
			
		    
}
		

	
	/**
	 * 装载机器人到JVM
	 */
	public void loadRobots() {
		log.info("机器工厂装载机器人到JVM...");
		for(Map robot : this.robotConf){
			loadRobot(robot);
		}
		
	}

	/*
	 * 加载机器到JVM
	 */
	@SuppressWarnings("unchecked")
	private void loadRobot(Map<String,String> robotMap) {
		
		String robotClassName =  robotMap.get("class");
		String robotName =  robotMap.get("name");
		String filePath =  robotMap.get("confPath");
		String privateLib =  robotMap.get("privateLib");
		String xmlPath =  robotMap.get("xmlPath");
		try {
			
			//new PrivateClassLoader(robotName)
			//sddnew PrivateClassLoader(robotName).findClass("");
			//直接初始化
			
			Class<BaseRobot> robotclass = null;
			if(privateLib!=null){
				 robotclass = (Class<BaseRobot>) Class.forName(robotClassName,true,new PrivateURLClassLoader(FileUtils.searchModuleJarFromXml(xmlPath),privateLib));
			}else{
				robotclass = (Class<BaseRobot>) Class.forName(robotClassName);
			}
			// robotclass = (Class<BaseRobot>) Class.forName(robotClassName,true,new URLClassLoader(FileUtils.searchJarURL(privateLib)));
			
			/**
			 * 判断 该模块下有没有lib包 如果有 先用lib包的jar的classLoader加载类 如果加载不成功则用内存中的类。
			 * 
			 */
//			try {
//				String className = "ceshi.com.jvm.classload.target.Target";
//				URLClassLoader load1 = new URLClassLoader(new URL[]{new URL("file:C:\\Users\\Administrator\\Desktop\\target1.jar")});
//				URLClassLoader load2 = new URLClassLoader(new URL[]{new URL("file:C:\\Users\\Administrator\\Desktop\\target2.jar")});
//			
//				 Class<BaseRobot> clazz1 =  (Class<BaseRobot>)Class.forName(className,true,load1);
//				 
//				 Class<BaseRobot> clazz2 =  (Class<BaseRobot>)Class.forName(className,true,load2);
//				BaseRobot taget1 = clazz1.newInstance();
//				BaseRobot taget2 = clazz2.newInstance();
//				System.out.println("--------------target start-----------");
//				taget1.create();
//				System.out.println("");
//				taget2.create();
//				System.out.println("--------------target end-----------");
//				
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			log.debug("【"+robotclass+"】 使用类加载器：：：	"+robotclass.getClassLoader());
			BaseRobot robot = robotclass.newInstance();
			robot.assemble(filePath);
			
			robot.create();
			robots.put(robotName, robot);
		} catch (ClassNotFoundException e) {
			log.error(String.format("机器人 %s 类找不到 ， 无法加载到内存 详情： %s ", robotName,e.toString())); 
			e.printStackTrace();
		} catch (InstantiationException e) {
			log.error(String.format("机器人 %s 初始化错误 ， 无法加载到内存 详情： %s ", robotName,e.toString()));
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error(String.format("机器人 %s 初始化参数错误 ， 无法加载到内存  详情： %s ", robotName,e.toString()));
			e.printStackTrace();
		}
		
	}
	/**
	 * 唤醒机器人
	 */
	public void wakeupAll() {
		log.info("机器工厂唤醒机器人...");
		for(Map robot : this.robotConf){
			startRobot(robot);
		}
	}

	/*
	 * 唤醒机器人 然后开始工作
	 */
	private void startRobot(Map robotMap) {
		BaseRobot robot = robots.get(robotMap.get("name"));
		robot.start();
	}

	/**
	 * 销毁所有机器人
	 * 
	 */
	public void destroyAll() {
		log.info("机器工厂销毁所有机器人...");
	}

	public Hashtable<String, BaseRobot> getRobots() {
		return robots;
	}

public static void main(String[] args) {
	
	try {
	String className = "ceshi.com.jvm.classload.target.Target";
	//URLClassLoader load1 = new URLClassLoader(FileUtils.searchJarURL(privateLib));
	//URLClassLoader load1 = (Class<BaseRobot>) Class.forName(className,true,new URLClassLoader(FileUtils.searchJarURL("C:\\Users\\Administrator\\Desktop\\Unsan\\Unsan_Robot_External\\wali\\privatelib")));
	//URLClassLoader load2 = new URLClassLoader(new URL[]{new URL("file:C:\\Users\\Administrator\\Desktop\\target2.jar")});

	 Class<BaseRobot> clazz1 =  (Class<BaseRobot>)Class.forName(className,true,new PrivateURLClassLoader(FileUtils.searchJarURL("C:\\Users\\Administrator\\Desktop\\Unsan\\Unsan_Robot_External\\wali\\privatelib"),new PrivateClassLoader("ceshi")));
	 
	 //Class<BaseRobot> clazz2 =  (Class<BaseRobot>)Class.forName(className,true,load2);
	BaseRobot taget1 = clazz1.newInstance();
	//BaseRobot taget2 = clazz2.newInstance();
	System.out.println("--------------target start-----------"+clazz1.getClassLoader());
 	taget1.create();
 	System.out.println("");
	//taget2.create();
	System.out.println("--------------target end-----------");
	
} catch ( ClassNotFoundException | InstantiationException | IllegalAccessException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	
	
}
	
}
