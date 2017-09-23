package com.unsan.netty.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.unsan.netty.client.NettyRobotClient;

public class ConcurrencyTest {
public static void main(String[] args) {
	 ExecutorService cachep = Executors.newFixedThreadPool(100000);
	 for(int i = 0 ;i<10000;i++){
		 cachep.execute(new NettyRobotClient());
	 }
}
}
