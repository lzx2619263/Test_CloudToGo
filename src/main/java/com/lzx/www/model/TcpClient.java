package com.lzx.www.model;


import java.net.InetSocketAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;

public class TcpClient {
	
	public EventLoopGroup group;
	public String receiveMsg;

	public void init(String serverHost, int serverPort) {
		group = new NioEventLoopGroup();
		Bootstrap clientBootstrap = new Bootstrap();
		clientBootstrap.group(group);
		clientBootstrap.channel(NioSocketChannel.class);
		clientBootstrap.remoteAddress(new InetSocketAddress(serverHost, serverPort));
		
		clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
		    protected void initChannel(SocketChannel socketChannel) throws Exception {
		        socketChannel.pipeline().addLast(new ClientHandler());
		    }
		});
		
		try {
			ChannelFuture channelFuture = clientBootstrap.connect().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public void close() {
		try {
			Future<?> future =  this.group.shutdownGracefully().await();
			
		} catch (InterruptedException e) {
			System.out.println("客户端无法正常停止");
			e.printStackTrace();
		}
		System.out.println("客户端已经停止");
	}
	
	
	public class ClientHandler extends SimpleChannelInboundHandler {

	    @Override
	    public void channelActive(ChannelHandlerContext channelHandlerContext){
	        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("Netty Rocks!", CharsetUtil.UTF_8));
	    }

	    @Override
	    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
	        cause.printStackTrace();
	        channelHandlerContext.close();
	    }

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
			ByteBuf inBuffer = (ByteBuf) msg;
	        String received = "TCP server return: " + inBuffer.toString(CharsetUtil.UTF_8);
	        TcpClient.this.receiveMsg = received;
		}
	}
	
}
