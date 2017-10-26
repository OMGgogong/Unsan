package com.unsan.core.constants;

import com.unsan.core.utils.SystemUtils;

public class UnsanParameters {
	// public static String RUNTIME_PATH = "/home/unsan/app_robot";
	// public static String External_PATH = "/home/unsan/Unsan_Robot_External";
	public static String RUNTIME_PATH = SystemUtils.isWindowSystem?"D:\\workspaces\\eclipseWorkSpace\\Unsan":"/home/unsan/app_robot";
	public static String External_PATH = SystemUtils.isWindowSystem?"C:\\Users\\Administrator\\Desktop\\Unsan\\Unsan_Robot_External":"/home/unsan/Unsan_Robot_External";
	public static final String CONFILE = "unsan.xml";

	public static final String DEFAULT_LOAD_PRIORITY = "5";
	public static final String DEFAULT_THREAD_NUM = "15";

}
