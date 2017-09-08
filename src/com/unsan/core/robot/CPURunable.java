package com.unsan.core.robot;

import java.util.concurrent.TimeUnit;

import com.unsan.msg.LaserLight;

/**
 * 机器人核心芯片 【永动】
 * @author gszhang
 *
 */
public class CPURunable implements Runnable{

	private BaseRobot robot = null;
	
	public CPURunable(BaseRobot robot) {
		this.robot = robot;
	}
	@Override
	public void run() {
		;
		while(robot.moduleState==2){
			try {
			LaserLight LaserLight;
			try {
				LaserLight = robot.msgQueue.poll(180000L, TimeUnit.MILLISECONDS);
				if(LaserLight!=null){
					robot.log.info("机器人"+robot.getRobotName()+"即将处理消息");
					robot.processHandle(LaserLight);
					robot.log.info("机器人"+robot.getRobotName()+"即将处理消息完成");
				}
				
			} catch (InterruptedException e) {
				robot.log.error("获取消息过程中出错"+e.toString());
				e.printStackTrace();
			}
			finally {
				/**
				 * 交出时间片 ， 自己也参与下次竞争
				 */
				Thread.yield();
			}
			/**
			 * 交出时间片 自己不参与下次竞争;防止自己没有消息处理自己占着cpu、但是其他有[没有处理完成的消息]机器人得不到工作
			 * 
			 * 线程中有sleep方法 所以 在baseRobot里线程池的shutdownNow()方法可以打断线程的哦   ╮(╯▽╰)╭
			 */
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

	
}
