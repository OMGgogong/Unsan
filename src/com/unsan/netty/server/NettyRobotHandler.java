package com.unsan.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyRobotHandler extends ChannelInboundHandlerAdapter {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			//System.out.println("MSG:"+((ByteBuf)msg).r);
		
		
		ByteBuf b = (ByteBuf)msg;
		ByteBuf heapBuffer = Unpooled.buffer(b.readableBytes());
		byte[] ba = new byte[b.readableBytes()];
		b.readBytes(ba);
		String smsg = new String(ba);
		System.out.println(smsg);
		heapBuffer.writeBytes(ba);
		ctx.write(heapBuffer);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		//super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
		
		ctx.close();
	}
}
