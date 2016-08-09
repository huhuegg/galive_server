package com.galive.logic.network.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChannelByteHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(ChannelByteHandler.class);
	private static final int MAX_NAME_SIZE = 32;
	private static final int USER_KEY_SIZE = 64;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		printLog("channelRead", ctx);
		// super.channelRead(ctx, msg);
		ByteBuf buf = (ByteBuf) msg;
		/*byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);*/
		logger.debug("===========================================");
		decodeRegisterRoomInfo(buf);
		logger.debug("===========================================");
		buf.release();
	}
	
	private void decodeRegisterRoomInfo(ByteBuf buf) {
		long timestamp = buf.readLong();
		logger.debug("timestamp:" + timestamp);
		
		long serverId = buf.readUnsignedInt();
		logger.debug("serverId:" + serverId);
		
		byte size = buf.readByte();
		int finalSize = size&0xff; // 转无符号 
		logger.debug("size:" + finalSize);
		
		for (int i = 0; i < finalSize; i++) {
			long roomId = buf.readUnsignedInt();
			logger.debug("roomId:" + roomId);
		}
	}
	
	private void decodeReqUserInfo(ByteBuf buf) {
		long timestamp = buf.readLong();
		logger.debug("timestamp:" + timestamp);
		
		long serverId = buf.readUnsignedInt();
		logger.debug("serverId:" + serverId);
		
		long conID = buf.readUnsignedInt();
		logger.debug("conID:" + conID);
		
		char[] charUserId = new char[MAX_NAME_SIZE];
		for (int i = 0; i < MAX_NAME_SIZE; i++) {
			char c = buf.readChar();
			charUserId[i] = c;
		}
		String userId = String.valueOf(charUserId);
		logger.debug("userId:" + userId);
		
		char[] charUserKey = new char[USER_KEY_SIZE];
		for (int i = 0; i < USER_KEY_SIZE; i++) {
			char c = buf.readChar();
			charUserKey[i] = c;
		}
		String userKey = String.valueOf(charUserKey);
		logger.debug("userKey:" + userKey);
	}
	
	private void encodeReqUserInfo(ByteBuf buf, long conID, String userId, boolean isValid) {
		buf.writeLong(conID);
		char[] charUserId = userId.toCharArray();
		int userIdLen = charUserId.length;
		int zeroCount = MAX_NAME_SIZE - userIdLen;
		buf.writeZero(zeroCount);
		for (int i = 0; i < userIdLen; i++) {
			buf.writeChar(charUserId[i]);
		}
		buf.writeBoolean(isValid);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		printLog("channelReadComplete", ctx);
		super.channelReadComplete(ctx);
		ctx.flush();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		printLog("channelRegistered", ctx);
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		printLog("channelUnregistered", ctx);
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
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		printLog("userEventTriggered", ctx);
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
		super.exceptionCaught(ctx, cause);
	}

	private void printLog(String message, ChannelHandlerContext ctx) {
		String ip = ctx.channel().remoteAddress().toString();
		logger.debug("client:" + ip + " " + message);
	}

	public static void main(String args[]) {
		byte[] message = "hello".getBytes();
		int len = message.length;
		Socket socket = new Socket();
		try {
			
			
			socket.connect(new InetSocketAddress("127.0.0.1", 44100));
			//socket.setKeepAlive(true);
			OutputStream out = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);  
			while (true) {
				ByteBuffer header = ByteBuffer.allocate(4);
				header.putInt(len);
				dos.write(header.array());
				dos.write(message);
				//out.write(header.array());
				//out.write(message, 0, message.length);
				out.flush();
				InputStream in = socket.getInputStream();
				byte[] buff = new byte[4096];
				int readed = in.read(buff);
				if (readed > 0) {
					String str = new String(buff, 4, readed);
					logger.info("client received msg from server:" + str);
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//out.close();
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
