package com.galive.logic.network.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;
import com.galive.logic.service.RoomService;
import com.galive.logic.service.RoomServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChannelByteHandler extends ChannelInboundHandlerAdapter {

	private static final short REGISTER_ROOM_INFO = 10001;
	private static final short REQ_USER_INFO = 10002;
	private static final short RET_USER_INFO = 10003;
	private static final short RET_USER_VOICE_PERMISSION = 10004;

	private static Logger logger = LoggerFactory.getLogger(ChannelByteHandler.class);
	private static final int MAX_NAME_SIZE = 32;
	private static final int USER_KEY_SIZE = 64;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		printLog("channelRead", ctx);
		// super.channelRead(ctx, msg);

		ByteBuf buf = (ByteBuf) msg;

		byte[] bytes = new byte[buf.readableBytes()];
		buf.getBytes(0, bytes);

		// byte[] bytes = new byte[buf.readableBytes()];
		// buf.readBytes(bytes);
		logger.debug("===========================================");
		printByte(bytes);
		// short messageId = buf.readUnsignedShort()
		short pkglen = buf.readShort();
		logger.debug("pkglen:" + pkglen);

		short contentlen = buf.readShort();
		logger.debug("contentlen:" + contentlen);

		short encrypt = buf.readShort();
		logger.debug("encrypt:" + encrypt);

		short messageId = buf.readShort();
		logger.debug("messageId:" + messageId);

		int timestamp = buf.readInt();
		logger.debug("timestamp:" + timestamp);

		switch (messageId) {
		case REGISTER_ROOM_INFO:
			InetSocketAddress address = ((InetSocketAddress) (ctx.channel().remoteAddress()));
			String ip = address.getHostString();
			int port = address.getPort();
			decodeRegisterRoomInfo(buf, ip, port);
			break;
		case REQ_USER_INFO:
			decodeReqUserInfo(timestamp, buf, ctx);
			break;
		}
		logger.debug("===========================================");
		buf.release();

	}

	private void decodeRegisterRoomInfo(ByteBuf buf, String ip, int port) {
		long serverId = buf.readUnsignedInt();
		logger.debug("serverId:" + serverId);

		byte size = buf.readByte();
		logger.debug("size:" + size);
		List<String> rooms = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			long roomId = buf.readUnsignedInt();
			logger.debug("roomId:" + roomId);
			rooms.add(roomId + "");
		}
		RoomService rs = new RoomServiceImpl();
		rs.saveRooms(ip, port, rooms);
	}

	private void decodeReqUserInfo(int timestamp, ByteBuf buf, ChannelHandlerContext ctx) {
		long conID = buf.readLong();
		logger.debug("conID:" + conID);

		byte[] userIdBytes = new byte[MAX_NAME_SIZE];
		buf.readBytes(userIdBytes);

		String userId = new String(userIdBytes, StandardCharsets.UTF_8);
		userId = userId.trim();
		logger.debug("userId:" + userId);
		
		byte[] userkeyBytes = new byte[USER_KEY_SIZE];
		buf.readBytes(userkeyBytes);

		String userKey = new String(userkeyBytes, StandardCharsets.UTF_8);
		userKey = userKey.trim();
		logger.debug("userKey:" + userKey);

		AccountService accountService = new AccountServiceImpl();
		boolean isValid = accountService.verifyToken(userId, userKey);
		
		encodeReqUserInfo(conID, timestamp ,userId, isValid, ctx);
		
	}

	
	
	private void encodeReqUserInfo(long conID, int timestamp, String userId, boolean isValid, ChannelHandlerContext ctx) {
		ByteBuf buf = ctx.alloc().buffer(4096); 
//		buf.writeShort((short) pkgLen);
		buf.writeShort((short) 0);
		buf.writeShort((short) 0);
		buf.writeShort(RET_USER_INFO);
		buf.writeInt(timestamp);
		buf.writeLong(conID);

		byte[] userIdBytes = userId.getBytes();
		buf.writeBytes(userIdBytes);
		buf.writeZero(MAX_NAME_SIZE - userIdBytes.length);
		
		buf.writeByte(isValid ? 0x01 : 0x00);
		
		byte[] pkg = new byte[buf.readableBytes()];
		buf.getBytes(0, pkg);
		printByte(pkg);
		ctx.write(buf);
		
		encodeReqUserVoicePermission(timestamp, userId, ctx);
	}
	
	private void encodeReqUserVoicePermission(int timestamp, String userId, ChannelHandlerContext ctx) {
		ByteBuf buf = ctx.alloc().buffer(4096); 
		buf.writeShort((short) (0));
		buf.writeShort((short) 0);
		buf.writeShort(RET_USER_VOICE_PERMISSION);
		buf.writeInt(timestamp);
		
		int count = 1;
		// 用户数
		buf.writeByte(count);
		for (int i = 0; i < count; i++) {
			byte[] userIdBytes = userId.getBytes();
			buf.writeBytes(userIdBytes);
			buf.writeZero(MAX_NAME_SIZE - userIdBytes.length);
			// 权限
			buf.writeByte(1);
		}		
		byte[] pkg = new byte[buf.readableBytes()];
		buf.getBytes(0, pkg);
		printByte(pkg);
		ctx.write(buf);
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
		logger.info("client:" + ip + " " + message);
	}

	public static void main(String args[]) {
		Socket socket = new Socket();
		try {
			//socket.connect(new InetSocketAddress("127.0.0.1", 44100));
			socket.connect(new InetSocketAddress("222.73.196.99", 44100));
			// socket.setKeepAlive(true);
			OutputStream o = socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(o);
			while (true) {
				byte[] userId = "wangxin".getBytes();
				int userIdLen = userId.length;
				
				byte[] userKey = "ab啊吧".getBytes();
				int userKeyLen = userKey.length;
				// ByteBuffer headLen = ByteBuffer.allocate(2);
				// headLen.putShort( (short) (20 + len));
				//
				// ByteBuffer contentlen = ByteBuffer.allocate(2);
				// contentlen.putShort((short)0);
				//
				// ByteBuffer encryptLen = ByteBuffer.allocate(2);
				// encryptLen.putShort( (short) 0);
				out.writeShort(35);
				out.writeShort(0);
				out.writeShort(0);
				out.writeShort(REGISTER_ROOM_INFO);
				out.writeInt(0);
				out.writeInt(100);
				int roomcount = 5;
				out.writeByte(roomcount);
				for (int i = 0; i < roomcount; i++) {
					out.writeInt(i + 1);
				}
				
				
				
				

//				out.writeShort((short) (18 + MAX_NAME_SIZE + USER_KEY_SIZE));
//				out.writeShort((short) 1);
//				out.writeShort((short) 2);
//				out.writeShort(REQ_USER_INFO);
//				out.writeInt(3);
//				out.writeLong(4);
//
//				ByteBuffer userIdBuf = ByteBuffer.allocate(MAX_NAME_SIZE);
//				for (int i = 0; i < MAX_NAME_SIZE - userIdLen; i++) {
//					userIdBuf.put((byte) 0);
//				}
//				userIdBuf.put(userId);
//				byte[] userIdByte = userIdBuf.array();
//				out.write(userIdByte);
//				
//				ByteBuffer userKeyBuf = ByteBuffer.allocate(USER_KEY_SIZE);
//				for (int i = 0; i < USER_KEY_SIZE - userKeyLen; i++) {
//					userKeyBuf.put((byte) 0);
//				}
//				userKeyBuf.put(userKey);
//				byte[] userKeyByte = userKeyBuf.array();
//				out.write(userKeyByte);
				
				
				

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

/*	private byte[] getBytes(char[] chars) {
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = StandardCharsets.UTF_8.encode(cb);

		return bb.array();

	}

	private char[] getChars(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = StandardCharsets.UTF_8.decode(bb);
		return cb.array();
	}*/
	
	private void printByte(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for (byte b : bytes) {
			buffer.append(b + ",");
		}
		buffer.append("]");
		logger.debug(buffer.toString());
	}
}
