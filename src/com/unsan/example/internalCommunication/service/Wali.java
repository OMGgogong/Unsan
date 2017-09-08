package com.unsan.example.internalCommunication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unsan.core.robot.BaseRobot;
import com.unsan.example.internalCommunication.client.msg.LoveMsgreq;
import com.unsan.msg.LaserLight;

/**
 * 我是瓦力  ---测试机器人
 * 加载的时候 伊娃先加载（没有伊娃我存在也没有意义）
 * 
 * @author gszhang
 *
 */
public class Wali extends BaseRobot{
	public Wali() {
		this.setRobotName("wali");
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
		
		/**
		 * 开始工作 发送消息给伊娃
		 */
		LoveMsgreq msg = new LoveMsgreq("伊娃！伊娃！ 我是瓦力。周六有《战狼2》要不要一起看。╮(╯▽╰)╭");
		msg.setFromRobotName("wali");
		msg.setToRobotName("yiwa");
		this.aSend(msg);
		
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
		log.info(String.format(" %s 运行处理.... 处理的消息为  : %s ", getClass().getName(),msg));
		//处理完消息 发给..瓦力
		//msg.setMsg("伊娃！伊娃！ 我是瓦力。周六有《战狼2》要不要一起看。╮(╯▽╰)╭");
		//aSend(msg);
	}

}
