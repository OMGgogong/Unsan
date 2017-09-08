package com.unsan.core.robot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unsan.core.RobotFactory;
import com.unsan.msg.LaserLight;

public abstract class BaseRobot {

	public final Logger log = LoggerFactory.getLogger(getClass());
	private Properties properties = new Properties();
	private String robotName ;
	
	

	/*
	 * 激光消息存储的最大消息数量 10W
	 */
	private int maxLaserLightNum = 100000;
	/*
	 * 激光介质消息收发装置
	 */
	 PriorityBlockingQueue<LaserLight> msgQueue = new PriorityBlockingQueue<>();
	 
	 volatile  List<Runnable> unstart =  new Vector<>();
			 
	 /*
	  * 机器人的心脏
	  */
	 ThreadPoolExecutor cpu;
	 /*
	  * 配置文件路径
	  */
	 private String  filePath = null;
	/**
	 * volatile保证内存可见性
	 */
	public volatile int moduleState = 0;
	/**
	 * 组装成型
	 * @param filePath
	 */
	public void assemble(String filePath){
		this.filePath = filePath;
		try {
			properties.load(new FileReader(filePath));
			
		} catch (FileNotFoundException e) {
			log.error(String.format("机器人 %s 配置文件路径找不到 详情 %s", robotName,e.toString()));
			e.printStackTrace();
		} catch (IOException e) {
			log.error(String.format("机器人 %s 配置不合法 详情 %s", robotName,e.toString()));
			e.printStackTrace();
		}
	}
	/**
	 * 机器人被创建
	 * 
	 * 加载私有配置+日志
	 */
	public boolean create(){
		this.moduleState = 0;
		Switch Switch = new Switch();
		Switch.setName("机器人的创建线程");
		Switch.start();
		log.info("机器人被创建...");
		try {
			//robotServer_JVM_main线程 等待Switch线程，等待时间是半分钟，除非Switch线程早结束
			Switch.join(30000L);
			//start();
		} catch (InterruptedException e) {
			log.info("机器人创建超时....机器人被玩坏.."+e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	/**
	 * 启动
	 * 在运行前 机器人需要做的准备工作 
	 * 如：
	 * 1.数据库连接成功
	 * 2.socket握手成功
	 * 
	 * @return
	 */
	public boolean start(){
		this.moduleState = 1;
		log.info("机器人启动工作...");
		
		
		Switch Switch = new Switch();
		Switch.setName("机器人的启动线程");
		Switch.start();
		//Switch 会对 已经停止 、 已经 开始 、 已经销毁 、还没创建 的机器人做出相应的方法
		//启动之后开始工作..
		
		try {
			//robotServer_JVM_main线程 等待Switch线程，等待时间是半分钟，除非Switch线程早结束
			Switch.join(30000L);
			 resume();
		} catch (InterruptedException e) {
			log.info("机器人启动超时....机器人被玩坏.."+e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 重启
	 * 重新读取配置
	 * 
	 * @return
	 */
	public boolean restart(){
		this.moduleState = 6;
		log.info("机器人重启..");
		//重新加载配置...
		assemble(this.filePath);
		
		Switch Switch = new Switch();
		Switch.setName("机器人的重启线程");
		Switch.start();

		try {
			//等待Switch线程，等待时间是半分钟，除非Switch线程提前结束
			Switch.join(30000L);
			start();
		} catch (InterruptedException e) {
			log.info("机器人重启超时....机器人被玩坏.."+e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void resume(){
		this.moduleState = 2;
		log.info("机器人开始工作...");
		
		Switch Switch = new Switch();
		Switch.setName("机器人的开始工作线程");
		Switch.start();
		run();
//		try {
//			//等待Switch线程，等待时间是半分钟，除非Switch线程提前结束
//			Switch.join(30000L);
//			
//		} catch (InterruptedException e) {
//			log.info("机器人开始工作等待超时....机器人被玩坏.."+e.toString());
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//		
		
	}
	/*
	 *启动
	 */
	private void run(){
		
		 this.cpu = new ThreadPoolExecutor(30, 30, 3, TimeUnit.SECONDS, new ArrayBlockingQueue(4000),new RejectedExecutionHandler() {
			
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				//如果超线程堆积太多
				//TODO::咋办？？  
				
			}
		});

		 //TODO::把原先 stop时候但是没有执行的线程需要执行
		for(int i = 0;i<30;i++){
			
			this.cpu.execute(new CPURunable(this));
		}
	}
	public void pause(){
		this.moduleState = 3;
		
		log.info("机器人暂停工作...");
		List<Runnable> unstart = this.cpu.shutdownNow();
		this.unstart = unstart;
		
		Switch Switch = new Switch();
		Switch.setName("机器人的暂停工作线程");
		Switch.start();
		
	}
	
	/**
	 * 机器人停止工作
	 * 清除所有的消息
	 * 要想工作必须重启
	 * @return
	 */
	public boolean stop(){
		this.moduleState = 4;
		log.info("机器人停止工作...");
		
	
		
		Switch Switch = new Switch();
		Switch.setName("机器人的停止工作线程");
		Switch.start();

		try {
			//等待Switch线程，等待时间是半分钟，除非Switch线程提早结束
			Switch.join(30000L);
			/**
			 * 直接销毁 未执行的机器人直接丢弃
			 */
			this.cpu.shutdownNow();
			unstart.clear();
			/**
			 * 把所有的消息全部清掉
			 */
			cleanLaserLight();
		} catch (InterruptedException e) {
			log.info("机器人停止超时....机器人被玩坏.."+e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private void cleanLaserLight(){
		msgQueue.clear();
	}
	/**
	 * 直接销毁 不再给用户销毁时间
	 * 
	 * @return
	 */
	public boolean destroy(){
		this.moduleState = 5;
		log.info("机器人销毁工作...");
		
		Switch Switch = new Switch();
		Switch.setName("机器人的销毁工作线程");
		Switch.start();
//		try {
//			//雲山郡 系统等待用户的销毁，等待时间是半分钟，除非销毁线程提早结束
//			Switch.join(30000L);
//		} catch (InterruptedException e) {
//			log.info("机器人销毁超时....机器人被玩坏.."+e.toString());
//			e.printStackTrace();
//			return false;
//		}
		return true;
	}
	
	protected void onCreate() {
		
	}

	protected void onStart() {

	}

	protected void onResume() {

	}

	protected void onPause() {

	}

	protected void onStop() {

	}

	protected void onDestroy() {

	}

	protected void onRestart() {

	}
	
	/**
	 * 另起线程----防止某用户阻塞整个系统
	 * @author gszhang
	 *
	 */
	private class Switch extends Thread{
		private Switch(){}
		
		@Override
		public void run() {
			try {
				switch (BaseRobot.this.moduleState) {
				case 0:
					BaseRobot.this.onCreate();
					break;
				case 1:
					BaseRobot.this.onStart();
					break;
				case 2:
					BaseRobot.this.onResume();
					break;
				case 3:
					BaseRobot.this.onPause();
					break;
				case 4:
					BaseRobot.this.onStop();
					break;
				case 5:
					BaseRobot.this.onDestroy();
					break;
				case 6:
					BaseRobot.this.onRestart();
					break;

				default:
					break;
				}
			} catch (Exception e) {
				BaseRobot.this.log.error("状态机出错 ╭∩╮（︶︿︶）╭∩╮"+e.toString());
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * 消息发送装置
	 * @param msg
	 */
	public void aSend(LaserLight msg){
		
		String robotName = msg.getToRobotName();
		
		BaseRobot robot = RobotFactory.getInstance().getRobots().get(robotName);
		
		
	//	robot.msgQueue.offer(msg);
		
	
		//超过8w报警
		if(robot.msgQueue.size() > maxLaserLightNum*0.8D){
			log.info("机器人堆积的消息超过了8W....报警！！！！...");
		}
		//机器人处于工作状态才可以
		if(robot.moduleState == 2 && robot.msgQueue.size() < maxLaserLightNum){
			robot.msgQueue.offer(msg);
			return;
		}else if(robot.msgQueue.size() < maxLaserLightNum){
			//机器人处于非运行状态 可能处于 【暂停】 【停止】 【销毁】 【重启】 【启动】 状态
			//TODO::这个消息 我扔掉？还是返回给客户  ...
			log.error(String.format("机器人 %s 处于非工作状态 状态 :%d ", robot.getRobotName(),robot.moduleState));
		}
		
		log.info("机器人堆积的消息过多..目前先丢弃..");
		//TODO::不然咋办？？可以放入另外一台专门放消息的机器上
		
	}
	
	/**
	 * 消息处理
	 * @param msg
	 */
	protected abstract void processHandle(LaserLight msg);
	
	public String getRobotName() {
		return robotName;
	}
	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}
	
}
