package com.unsan.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobotServer_JVM {
	Logger LOG = LoggerFactory.getLogger(RobotServer_JVM.class);

	/**
	 * 程序住入口
	 * @param args
	 */
	public static void main(String[] args) {
		RobotFactory rf = RobotFactory.getInstance();
		System.out.println("机器启动");
		rf.buildRobots();
		rf.loadRobots();
		rf.wakeupAll();

		Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
		System.out.println("机器启动成功！");
	}
}
