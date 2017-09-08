package com.unsan.msg;
/**
 * 原路返回激光消息
 * @author gszhang
 *
 */
public class RespLaserLight extends LaserLight{

	public RespLaserLight(ReqLaserLight req){
		this.setToRobotName(req.getFromRobotName());
		this.setFromRobotName(req.getToRobotName());
	}
	
}
