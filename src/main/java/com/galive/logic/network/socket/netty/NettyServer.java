package com.galive.logic.network.socket.netty;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.network.socket.ChannelStringLineHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

public class NettyServer {

	private static Logger logger = LoggerFactory.getLogger(NettyServer.class);
	
	private ChannelFuture logicChannelFuture;
	private EventLoopGroup logicMainGroup;
	private EventLoopGroup logicWorkerGroup;
	
//	private ChannelFuture voiceChannelFuture;
//	private EventLoopGroup voiceMainGroup;
//	private EventLoopGroup voiceWorkerGroup;

	public void start() throws InterruptedException, IOException {
		startLogicChannel();
//		startVoiceChannel();
	}
	
	private void startLogicChannel() throws InterruptedException, IOException {
		logicMainGroup = new NioEventLoopGroup(); 
		logicWorkerGroup = new NioEventLoopGroup();
		final NettyConfig nettyConfig = NettyConfig.loadConfig();
		final SocketConfig socketConfig = ApplicationConfig.getInstance().getSocketConfig();
		ServerBootstrap b = new ServerBootstrap(); 
		b.group(logicMainGroup, logicWorkerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() { 
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
						
//						ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
						// 基于指定字符串【换行符，这样功能等同于LineBasedFrameDecoder】
						ByteBuf[] delimiter = new ByteBuf[] {
				                Unpooled.wrappedBuffer(socketConfig.getMessageDelimiter().getBytes()),
				        };
						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(nettyConfig.getBufferSize(), true, delimiter));
						
						// 基于最大长度
//						e.pipeline().addLast(new FixedLengthFrameDecoder(4));
						ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
						
						// 编码器 String
						ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
						
						//心跳
						ch.pipeline().addLast(new IdleStateHandler(0, 0, nettyConfig.getBothIdleTime(), TimeUnit.SECONDS));
						
						ch.pipeline().addLast(new ChannelStringLineHandler());
					}
				}).childOption(ChannelOption.SO_KEEPALIVE, false);

		// Bind and start to accept incoming connections.
		int port = socketConfig.getPort();
		logicChannelFuture = b.bind(port).sync();
		logicChannelFuture.channel().closeFuture();
		logger.info("绑定端口" + port + (logicChannelFuture.isSuccess() ? "成功" : "失败"));
	}
//	
//	private void startVoiceChannel() throws InterruptedException, IOException {
//		voiceMainGroup = new NioEventLoopGroup(); 
//		voiceWorkerGroup = new NioEventLoopGroup();
//		ServerBootstrap b = new ServerBootstrap(); 
//		b.group(voiceMainGroup, voiceWorkerGroup).channel(NioServerSocketChannel.class)
//				.childHandler(new ChannelInitializer<SocketChannel>() { 
//					@Override
//					public void initChannel(SocketChannel ch) throws Exception {
//						ChannelPipeline pipeline = ch.pipeline();  
//						pipeline.addLast(new LoggingHandler(LoggerFactory.class, LogLevel.DEBUG));
//						LengthFieldBasedFrameDecoder decoder;
//						LengthFieldPrepender prepender;
//						if (ApplicationMain.sharedInstance().getMode() == ApplicationMode.Develop) {
//							decoder = new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 0, true);
//							prepender = new LengthFieldPrepender(2);
//						} else {
//							decoder = new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE, 0, 2, 0, 0, true);
//							prepender = new LengthFieldPrepender(ByteOrder.BIG_ENDIAN, 2, 0, false);
//						}
//		                pipeline.addLast("frameDecoder", decoder);  
//		                pipeline.addLast("frameEncoder", prepender); 
//		                pipeline.addLast(new IdleStateHandler(0, 0, 2, TimeUnit.HOURS));
//					}
//				}).childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.TCP_NODELAY, true);
//		int port = 44100;
//		voiceChannelFuture = b.bind(port).sync();
//		voiceChannelFuture.channel().closeFuture();
//		logger.info("绑定端口" + port + (voiceChannelFuture.isSuccess() ? "成功" : "失败"));
//	}

	public void stop() {
		try {
			if (logicMainGroup != null) {
				logicMainGroup.shutdownGracefully().sync();
			}
			if (logicWorkerGroup != null) {
				logicWorkerGroup.shutdownGracefully().sync();
			}
//			if (voiceMainGroup != null) {
//				voiceMainGroup.shutdownGracefully().sync();
//			}
//			if (voiceWorkerGroup != null) {
//				voiceWorkerGroup.shutdownGracefully().sync();
//			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
