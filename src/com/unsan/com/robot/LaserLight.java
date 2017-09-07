package com.unsan.com.robot;

/**
 * 机器人通过激光发送消息
 * @author gszhang
 *
 */
public class LaserLight {
	private String fromRobotName;
	private String toRobotName;
	public String getFromRobotName() {
		return fromRobotName;
	}
	public void setFromRobotName(String fromRobotName) {
		this.fromRobotName = fromRobotName;
	}
	public String getToRobotName() {
		return toRobotName;
	}
	public void setToRobotName(String toRobotName) {
		this.toRobotName = toRobotName;
	}
	
	@Override
	public String toString() {
		return "从:"+fromRobotName+"到"+toRobotName;
	}

}
