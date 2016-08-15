package galive_server_logic;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

public class TestNettyClient {

	
	public Bootstrap start(int port) throws InterruptedException, IOException {
		EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ByteBuf[] delimiter = new ByteBuf[] {
		                Unpooled.wrappedBuffer(Test.delimiter.getBytes()),
		        };

                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, true, delimiter));
				ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
				ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
				ch.pipeline().addLast(new IdleStateHandler(0, 0, 30, TimeUnit.SECONDS));
				ch.pipeline().addLast(new TestChannelHandler());
            }
        });
        b.option(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture future = b.bind(port).sync();
        future.channel().closeFuture();
        return b;
	}
	
	

}
