package com.unsan.netty.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyRobotClient implements Runnable{

	private final String host = "127.0.0.1";
	private final int port = 6666;
	
	
	

	protected void start() {
		
		EventLoopGroup group = new NioEventLoopGroup();
		 
		Bootstrap b = new Bootstrap();
		b.group(group);
		b.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);
		b.remoteAddress(new InetSocketAddress(host,port));
		b.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				//ch.pipeline().addLast()
				ch.pipeline().addLast(new EchoClientHandler());
			}
		});
		
		try {
			ChannelFuture f  = b.connect(host,port).sync();
			f.addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess()){
						System.out.println("client conneted");
						
					}else{
						System.out.println("server attemp failed");
						future.cause().printStackTrace();
					}
				}
			});
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			System.out.println("异常退出");
			e.printStackTrace();
		}
		finally {
			try {
				group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				System.out.println("退出失败");
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void main(String[] args) {
			
		new NettyRobotClient().start();
	}


	@Override
	public void run() {
		new NettyRobotClient().start();
	}

}
