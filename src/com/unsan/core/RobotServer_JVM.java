package com.unsan.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unsan.core.constants.UnsanParameters;

public class RobotServer_JVM {
	Logger LOG = LoggerFactory.getLogger(RobotServer_JVM.class);

	/**
	 * 程序住入口
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args!=null&&args.length>0){
			UnsanParameters.RUNTIME_PATH = args[0];
		}
		
		
		RobotFactory rf = RobotFactory.getInstance();
		System.out.println("机器启动");
		rf.buildRobots();
		rf.loadRobots();
		rf.wakeupAll();

		Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
		System.out.println("机器启动成功！");
	}
}
