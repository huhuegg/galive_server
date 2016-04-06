package com.galive.logic.network.socket.netty;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.network.socket.ChannelHandler;

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
	
	private ChannelFuture channelFuture;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public void start() throws InterruptedException, IOException {
		bossGroup = new NioEventLoopGroup(); 
		workerGroup = new NioEventLoopGroup();
		final NettyConfig nettyConfig = NettyConfig.loadConfig();
		final SocketConfig socketConfig = ApplicationConfig.getInstance().getSocketConfig();
		ServerBootstrap b = new ServerBootstrap(); 
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
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
						
						ch.pipeline().addLast(new ChannelHandler());
					}
				}).childOption(ChannelOption.SO_KEEPALIVE, true);

		// Bind and start to accept incoming connections.
		int port = socketConfig.getPort();
		channelFuture = b.bind(port).sync();
		channelFuture.channel().closeFuture();
		logger.info("绑定端口" + port + (channelFuture.isSuccess() ? "成功" : "失败"));
	}

	public void stop() {
		try {
			if (workerGroup != null) {
				workerGroup.shutdownGracefully().sync();
			}
			if (bossGroup != null) {
				bossGroup.shutdownGracefully();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
