package com.unsan.com;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unsan.com.robot.BaseRobot;

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
	public void buildRobots() {
		 this.log = LoggerFactory.getLogger(getClass());
		 log.info("机器工厂从文件中组装机器人...");
		 
		 //从核心目录下读取
		 
		 //从拓展目录下读取
		 
		 // 整理启动顺序
		 
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
	private void loadRobot(Map<String,String> robotMap){
		
		String robotClassName =  robotMap.get("class");
		String robotName =  robotMap.get("name");
		String filePath =  robotMap.get("path");
		try {
			Class<BaseRobot> robotclass = (Class<BaseRobot>) Class.forName(robotClassName);
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


	
}
