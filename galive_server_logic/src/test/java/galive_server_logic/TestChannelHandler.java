package galive_server_logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.galive.logic.network.socket.handler.CreateLiveHandler.CreateLiveOut;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TestChannelHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(TestChannelHandler.class);
		
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	//printLog("channelRegistered", ctx);
    	super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {    
		super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	String reqData = (String) msg;
    	logger.debug("接受服务端消息:" + reqData);
    	if (reqData.contains("CREATE_LIVE")) {
    		CreateLiveOut out = JSON.parseObject(reqData, CreateLiveOut.class);
    		Test.liveSid = out.live.getSid();
    	} else if (reqData.contains("JOIN_LIVE_PUSH")) {
    		Test.joinLivePush = true;
    	} else if (reqData.contains("LEAVE_LIVE")) {
    		Test.leaveLivePush = true;
    	} 
		super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    	super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	super.exceptionCaught(ctx, cause);
    }
    
 
}
