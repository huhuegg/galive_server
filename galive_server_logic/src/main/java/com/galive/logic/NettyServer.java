package com.galive.logic;

import java.io.IOException;
import java.util.Properties;
import com.galive.logic.helper.LogicHelper;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

	private ChannelFuture channelFuture;

	public void start() throws InterruptedException, IOException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new NettyServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			Properties prop = LogicHelper.loadProperties();
			channelFuture = b.bind(Integer.parseInt(prop.getProperty("netty.bind.port", "52194"))).sync(); // (7)

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
