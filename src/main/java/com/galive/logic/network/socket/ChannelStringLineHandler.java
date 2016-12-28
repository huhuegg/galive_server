package com.galive.logic.network.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.galive.logic.db.RedisManager;
import com.galive.logic.network.protocol.Command;
import com.galive.logic.network.protocol.CommandIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.logic.annotation.AnnotationManager;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.config.SocketConfig;
import com.galive.logic.network.socket.handler.SocketBaseHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import redis.clients.jedis.Jedis;

public class ChannelStringLineHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(ChannelStringLineHandler.class);
	
	private static String KEEP_ALIVE_REQ;
	private static String KEEP_ALIVE_RESP;
	
	static {
		SocketConfig config = ApplicationConfig.getInstance().getSocketConfig();
		KEEP_ALIVE_REQ = config.getLiveReq();
		KEEP_ALIVE_RESP = config.getLiveResp() + config.getMessageDelimiter();
	}
	
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	printLog("channelRegistered", ctx);
    	super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {    
    	printLog("channelUnregistered", ctx);
    	String account = ctx.channel().attr(ChannelManager.ACCOUNT_KEY).get(); 
		if (account != null) {
			CommandIn in = new CommandIn();
			in.setAccount(account);
			in.setCommand(Command.OFFLINE);
			SocketBaseHandler handler = AnnotationManager.createSocketHandlerInstance(in.getCommand());
			if (handler != null) {
				handler.handle(in, ctx);
			}
		}
		super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	printLog("channelActive", ctx);
    	super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	printLog("channelInactive", ctx);
    	super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	printLog("channelRead", ctx);
    	String reqData = (String) msg;
    	// 心跳
    	logger.debug(reqData);
    	if (keepAlive(reqData, ctx)) {
    		super.channelRead(ctx, msg);
    		return;
    	}
		CommandIn in = CommandIn.fromSocketReq(reqData, ApplicationConfig.getInstance().getSocketConfig().getParamsDelimiter());
		if (in != null) {
			SocketBaseHandler handler = AnnotationManager.createSocketHandlerInstance(in.getCommand());
			if (handler != null) {
				handler.handle(in, ctx);
			} else {
				logger.error("channelRead 消息错误:" + reqData);
				closeAndRemoveChannel(ctx);
			}
		} else {
			logger.error("channelRead 消息错误:" + reqData);
			closeAndRemoveChannel(ctx);
		}
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	printLog("channelReadComplete", ctx);
    	super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    	printLog("userEventTriggered", ctx);
    	/*心跳处理*/
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            IdleState state = event.state();
            if (state == IdleState.ALL_IDLE) {
            	String ip = ctx.channel().remoteAddress().toString();  
            	logger.info("ALL_IDLE:" + ip + " close connection");
            	closeAndRemoveChannel(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    	printLog("channelWritabilityChanged", ctx);
    	super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	printLog("exceptionCaught", ctx);
//    	logger.debug("exceptionCaught");
//    	logger.error(cause.getMessage());
//    	closeChannel(ctx);
    	super.exceptionCaught(ctx, cause);
    }
    
    private boolean keepAlive(String message, ChannelHandlerContext ctx) {
    	if (message.equals(KEEP_ALIVE_REQ)) {
    		ctx.writeAndFlush(KEEP_ALIVE_RESP);
    		return true;
    	}
    	return false;
    }
    
    private void closeAndRemoveChannel(ChannelHandlerContext ctx) {
    	String account = ctx.channel().attr(ChannelManager.ACCOUNT_KEY).get();  
		if (account != null) {
			ChannelManager.getInstance().closeAndRemoveChannel(account);
		} else {
			ChannelManager.closeChannel(ctx);
		}
    }
    
    private void printLog(String message, ChannelHandlerContext ctx) {
    	String ip = ctx.channel().remoteAddress().toString();  
    	logger.debug("client:" + ip + " " + message);
    }
    
    
    public static void main(String args[]) {
		Socket socket = new Socket();
		try {
			//socket.connect(new InetSocketAddress("127.0.0.1", 44100));
			socket.connect(new InetSocketAddress("127.0.0.1", 52195));
			// socket.setKeepAlive(true);
			
			while (true) {
				try {
					OutputStream o = socket.getOutputStream();
					DataOutputStream out = new DataOutputStream(o);
					out.writeBytes("test-@-xmn-@-");
					out.flush();
					InputStream in = socket.getInputStream();
					byte[] buff = new byte[4096];
					int readed = in.read(buff);
					if (readed > 0) {
						String str = new String(buff, "utf-8");
						logger.info("received:" + str);
					}
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// out.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
