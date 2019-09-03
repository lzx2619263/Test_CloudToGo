package com.lzx.www.model;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

public class TcpServer {

	ChannelFuture channelFuture = null;
	
	public void init(int port) {
		//配置TCP服务器端启动引导
		EventLoopGroup group = new NioEventLoopGroup();
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(group);
		serverBootstrap.channel(NioServerSocketChannel.class);
		//给启动引导附加socket初始化设计器，用于server接收到客户端连接后初始化socket
		serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				socketChannel.pipeline().addLast(new HelloServerHandler());
			}
		});
		//启动server
		try {
			if(channelFuture == null) {
				channelFuture = serverBootstrap.bind(port).sync();
				System.out.println("TCP服务器启动完成");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public class HelloServerHandler extends ChannelInboundHandlerAdapter{
		@Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	        ByteBuf inBuffer = (ByteBuf) msg;
	        String received = inBuffer.toString(CharsetUtil.UTF_8);
	        System.out.println("Server received: " + received);
	        ctx.write(Unpooled.copiedBuffer("Hello TCP client!", CharsetUtil.UTF_8));
	    }
		
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}
	}

}
