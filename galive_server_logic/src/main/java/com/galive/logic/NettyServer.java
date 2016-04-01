package com.galive.logic;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.NettyConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.helper.LogicHelper;

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

public class NettyServer {

	private static Logger logger = LoggerFactory.getLogger(NettyServer.class);
	
	private ChannelFuture channelFuture;

	public void start() throws InterruptedException, IOException {
		// TODO 配置 心跳
		EventLoopGroup bossGroup = new NioEventLoopGroup(); 
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		final NettyConfig nettyConfig = NettyConfig.loadConfig();
		final SocketConfig socketConfig = ApplicationConfig.getInstance().getSocketConfig();
		try {
			ServerBootstrap b = new ServerBootstrap(); 
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() { 
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
							
//							ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
							// 基于指定字符串【换行符，这样功能等同于LineBasedFrameDecoder】
							ByteBuf[] delimiter = new ByteBuf[] {
					                Unpooled.wrappedBuffer(socketConfig.getDelimiter().getBytes()),
					        };
							ch.pipeline().addLast(new DelimiterBasedFrameDecoder(nettyConfig.getBufferSize(), delimiter));
							
							// 基于最大长度
//							e.pipeline().addLast(new FixedLengthFrameDecoder(4));
							ch.pipeline().addLast(new StringDecoder());
							
							// 编码器 String
							ch.pipeline().addLast(new StringEncoder());
							
							//心跳
							ch.pipeline().addLast(new IdleStateHandler(0, 0, nettyConfig.getBothIdleTime(), TimeUnit.SECONDS));
							
							ch.pipeline().addLast(new LogicSocketHandler());
						}
					})
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			// Bind and start to accept incoming connections.
			Properties prop = LogicHelper.loadProperties();
			int port = Integer.parseInt(prop.getProperty("netty.bind.port", "52194"));
			logger.info("绑定端口:" + port);
			channelFuture = b.bind(port).sync(); // (7)

		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void stop() {
		try {
			if (channelFuture != null) {
				channelFuture.channel().closeFuture().sync();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
