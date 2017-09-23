package com.unsan.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unsan.core.robot.BaseRobot;
import com.unsan.msg.LaserLight;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

/**
 * netty机器人---超级联络机器人（员） 用于连通 【雲山郡】-【外部】 的通讯员 基于 netty 4.1 版本 理论上单机 50w-100w
 * 长连接不是梦
 * 
 * @author gszhang
 *
 */
public class NettyRobot extends BaseRobot {
	private Logger log = LoggerFactory.getLogger(getClass());
	private static final int PORT = 6666;
	private static final int WORK_GROUP = Runtime.getRuntime().availableProcessors()*2+1;
	private static final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup(WORK_GROUP);
	private static volatile boolean ISSTOP  = false;
	
	@Override
	protected void onCreate() {
		log.info(String.format(" %s 创建....", getClass().getName()));
		//读取配置
		
	}

	@Override
	protected void onStart() {
		log.info(String.format(" %s 启动....", getClass().getName()));
		//启动
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class).
		option(ChannelOption.SO_BACKLOG, 100)
		.handler(new LoggingHandler(LogLevel.INFO))
		.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new NettyRobotHandler());
				
			}

		});
		
		try {
			ChannelFuture f = bootstrap.bind(PORT).sync();
			System.out.println("TCP窗口服务器已经启动！");
		} catch (InterruptedException e) {
			log.info("IP---端口绑定失败！"+e.toString());
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onResume() {
		log.info(String.format(" %s 开始工作....", getClass().getName()));
	}

	@Override
	protected void onRestart() {
		log.info(String.format(" %s 重启....", getClass().getName()));
	}

	@Override
	protected void onPause() {
		log.info(String.format(" %s 暂停....", getClass().getName()));
		//抛弃所有msg 哈哈哈... 暂时不再给【雲山郡】平台提供消息
		
	}

	@Override
	protected void onStop() {
		log.info(String.format(" %s 停止....", getClass().getName()));
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}

	@Override
	protected void onDestroy() {
		log.info(String.format(" %s 销毁....", getClass().getName()));
	}

	@Override
	protected void processHandle(LaserLight msg) {
		log.info(String.format(" %s netty 处理消息....", msg));
		
	}

}
