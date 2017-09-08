package com.unsan.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jvm销毁前运行的方法
 * @author Administrator
 *
 */
public class ShutdownHookThread extends Thread {
	public Logger log = null;

	public ShutdownHookThread() {
		log = LoggerFactory.getLogger(getClass());
	}

	@Override
	public void run() {
		log.info("机器人工厂销毁所有机器人...");
		RobotFactory.getInstance().destroyAll();
	}

}
