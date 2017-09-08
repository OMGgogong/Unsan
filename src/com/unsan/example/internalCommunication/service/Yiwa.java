package com.unsan.example.internalCommunication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unsan.core.robot.BaseRobot;
import com.unsan.example.internalCommunication.client.msg.LoveMsgreq;
import com.unsan.example.internalCommunication.client.msg.LoveMsgresp;
import com.unsan.msg.LaserLight;

/**
 * 伊娃 ---测试机器人
 * 瓦力 的女朋友
 * 加载瓦力必须先加载伊娃
 * 
 * @author gszhang
 *
 */
public class Yiwa extends BaseRobot{
	
	public Yiwa() {
		this.setRobotName("yiwa");
	}
	Logger log = LoggerFactory.getLogger(getClass());
	@Override
	protected void onCreate() {
		log.info(String.format(" %s 创建....", getClass().getName()));
	}
	@Override
	protected void onStart() {
		log.info(String.format(" %s 启动....", getClass().getName()));
	}
	@Override
	protected void onRestart() {
		log.info(String.format(" %s 重启....", getClass().getName()));
	}
	@Override
	protected void onResume() {
		log.info(String.format(" %s 开始....", getClass().getName()));
	}
	@Override
	protected void onPause() {
		log.info(String.format(" %s 暂停....", getClass().getName()));
	}
	@Override
	protected void onStop() {
		log.info(String.format(" %s 停止....", getClass().getName()));
	}
	@Override
	protected void onDestroy() {
		log.info(String.format(" %s 销毁....", getClass().getName()));
	}
	@Override
	protected void processHandle(LaserLight msg) {
		if(msg instanceof LoveMsgreq){
			log.info(String.format(" %s 运行处理.... 处理的消息为  : %s ", getClass().getName(),msg));
			//处理完消息 发给..瓦力
		//	LoveMsgreq resp = new LoveMsgreq("我收到你的消息了，周六我有时间，我是伊娃！");
			LoveMsgresp resp = new  LoveMsgresp((LoveMsgreq) msg);
			resp.setMsg("我收到你的消息了，周六我有时间，我是伊娃！");
			aSend(resp);
		}
	
	}

}
