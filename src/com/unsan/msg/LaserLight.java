package com.unsan.msg;

import java.io.Serializable;

/**
 * 机器人通过激光发送消息
 * @author gszhang
 *
 */
public class LaserLight implements Serializable ,Comparable<LaserLight>{
	/**
	 * 可序列化
	 */
	private static final long serialVersionUID = 1L;
	
	private String fromRobotName;
	private String toRobotName;
	private String msg;
	private int level = 0;
	
	public long msgcreatetime = 0L;
    public LaserLight() {
    	msgcreatetime =  System.currentTimeMillis();
	}
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
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "从:"+fromRobotName+" 到: "+toRobotName+" 消息："+msg;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	@Override
	public int compareTo(LaserLight o)  {
	    int rs = 0;
	    rs = this.level - o.getLevel();
	    if ((rs == 0) && 
	      (this.msgcreatetime - o.msgcreatetime > 0L)) {
	      rs = 1;
	    }

	    return rs;
	  }
	
}
