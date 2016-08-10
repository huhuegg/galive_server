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
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChannelByteHandler extends ChannelInboundHandlerAdapter {

	private static final short REGISTER_ROOM_INFO = 10001;
	private static final short REQ_USER_INFO = 10002;
	private static final short RET_USER_INFO = 10003;
	
	private static Logger logger = LoggerFactory.getLogger(ChannelByteHandler.class);
	private static final int MAX_NAME_SIZE = 32;
	private static final int USER_KEY_SIZE = 64;
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		printLog("channelRead", ctx);
		//super.channelRead(ctx, msg);
		ByteBuf buf = (ByteBuf) msg;
		
		byte[] bytes = new byte[buf.readableBytes()];
		buf.getBytes(0, bytes);
		
//		byte[] bytes = new byte[buf.readableBytes()];
//		buf.readBytes(bytes);
		logger.debug(bytes + "");
		logger.debug(new String(bytes));		
		logger.debug("===========================================");
		//short messageId = buf.readUnsignedShort()
		short pkglen = buf.readShort();
		logger.debug("pkglen:" + pkglen);
		
		short contentlen = buf.readShort();
		logger.debug("contentlen:" + contentlen);
		
		short encrypt = buf.readShort();
		logger.debug("encrypt:" + encrypt);
		
		int messageId = buf.readShort();
		logger.debug("messageId:" + messageId);
		
		int timestamp = buf.readInt();
		logger.debug("timestamp:" + timestamp);
		
		long serverId = buf.readUnsignedInt();
		logger.debug("serverId:" + serverId);
		
		
		switch (messageId) {
		case REGISTER_ROOM_INFO:
			decodeRegisterRoomInfo(buf);
			break;
		case REQ_USER_INFO:
			decodeReqUserInfo(buf);
			break;
		}
		logger.debug("===========================================");
		buf.release();
		
		
	}
	
	private void decodeRegisterRoomInfo(ByteBuf buf) {
		byte size = buf.readByte();
		logger.debug("size:" + size);
		for (int i = 0; i < size; i++) {
			long roomId = buf.readUnsignedInt();
			logger.debug("roomId:" + roomId);
		}
	}
	
	private void decodeReqUserInfo(ByteBuf buf) {		
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
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("127.0.0.1", 44100));
			//socket.setKeepAlive(true);
			OutputStream o = socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(o);  
			while (true) {
				byte[] message = "aaaaaaaaaa".getBytes();
				int len = message.length;
				
				ByteBuffer headLen = ByteBuffer.allocate(2);
				headLen.putShort( (short) (2 + 2 + 4 + len));
				
				ByteBuffer encryptLen = ByteBuffer.allocate(2);
				encryptLen.putShort( (short) 0);
				
				
				
				out.write(headLen.array());
				out.write(encryptLen.array());
				out.writeShort(REGISTER_ROOM_INFO);
				out.writeInt(0);
				out.write(message);
				
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
	
	private static int toUnsigned(short s) {  
	    return s & 0x0FFFF;  
	}
	
	public static long getUnsignedInt (int data){     //将int数据转换为0~4294967295 (0xFFFFFFFF即DWORD)。
		return data&0x0FFFFFFFFl;
	}
}
