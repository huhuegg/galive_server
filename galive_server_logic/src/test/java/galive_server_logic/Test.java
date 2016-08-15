package galive_server_logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;
import com.galive.logic.network.socket.handler.JoinLiveHandler.JoinLiveIn;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

public class Test {

	private static Logger logger = LoggerFactory.getLogger(Test.class);
	public static final String paramsDelimiter = "&";
	public static final String delimiter = "-@-xmn-@-";
	
	public static final String act1 = "act_1";
	public static final String act2 = "act_2";
	public static String token1 = "";
	public static String token2 = "";
	
	public static Channel channel1;
	public static Channel channel2;
	
	public static String liveSid;
	public static boolean joinLivePush = false;
	public static boolean leaveLivePush = false;
	
	public static void main(String[] args) throws Exception {
		token1 = TokenRequest.req(act1);
		logger.debug("act1 token:" + token1);
		
		token2 = TokenRequest.req(act2);
		logger.debug("act2 token:" + token2);
		
		TestNettyClient client1 = new TestNettyClient();
		Bootstrap b1 = client1.start(40100);
		channel1 = b1.connect("127.0.0.1", 52195).sync().channel();
		
		TestNettyClient client2 = new TestNettyClient();
		Bootstrap b2 = client2.start(40111);
		channel2 = b2.connect("127.0.0.1", 52195).sync().channel();
       
		String onlineReq1 = onlineReq(act1, token1);
		channel1.writeAndFlush(onlineReq1);
		
		String onlineReq2 = onlineReq(act2, token2);
		channel2.writeAndFlush(onlineReq2);
		
		
		String createLiveReq = createLiveReq();
		channel1.writeAndFlush(createLiveReq);
		
		Thread.sleep(1000);
		while(true) {
			if (liveSid != null) {
				logger.debug("liveSid:" + liveSid);
				break;
			}
		}
		
		String joinLiveReq = joinLiveReq(liveSid);
		channel2.writeAndFlush(joinLiveReq);
		
		Thread.sleep(1000);
		while(true) {
			if (joinLivePush) {
				logger.debug("joinLivePush:" + true);
				break;
			}
		}
		
		
		String leaveLiveReq = leaveLiveReq();
		channel2.writeAndFlush(leaveLiveReq);
		
		Thread.sleep(1000);
		while(true) {
			if (leaveLivePush) {
				logger.debug("leaveLivePush:" + true);
				break;
			}
		}
		
		String destroyLiveReq = destroyLiveReq();
		channel1.writeAndFlush(destroyLiveReq);
		
	}
	
	private static String onlineReq(String act, String token) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Command.ONLINE);
		buffer.append(paramsDelimiter);
		
		buffer.append(act);
		buffer.append(paramsDelimiter);
		
		buffer.append(token);
		buffer.append(paramsDelimiter);
		
		buffer.append("1");
		buffer.append(paramsDelimiter);
		
		buffer.append("{}");
		
		buffer.append(delimiter);
		return buffer.toString();
	}
	
	private static String createLiveReq() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Command.CREATE_LIVE);
		buffer.append(paramsDelimiter);
		
		buffer.append(act1);
		buffer.append(paramsDelimiter);
		
		buffer.append(token1);
		buffer.append(paramsDelimiter);
		
		buffer.append("1");
		buffer.append(paramsDelimiter);
		
		buffer.append("{}");
		
		buffer.append(delimiter);
		return buffer.toString();
	}
	
	private static String joinLiveReq(String liveSid) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Command.JOIN_LIVE);
		buffer.append(paramsDelimiter);
		
		buffer.append(act2);
		buffer.append(paramsDelimiter);
		
		buffer.append(token2);
		buffer.append(paramsDelimiter);
		
		buffer.append("1");
		buffer.append(paramsDelimiter);
		
		JoinLiveIn in = new JoinLiveIn(); 
		in.liveSid = liveSid;
		buffer.append(JSON.toJSONString(in));
		
		buffer.append(delimiter);
		return buffer.toString();
	}
	
	private static String leaveLiveReq() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Command.LEAVE_LIVE);
		buffer.append(paramsDelimiter);
		
		buffer.append(act2);
		buffer.append(paramsDelimiter);
		
		buffer.append(token2);
		buffer.append(paramsDelimiter);
		
		buffer.append("1");
		buffer.append(paramsDelimiter);
		
		buffer.append("{}");
		
		buffer.append(delimiter);
		return buffer.toString();
	}
	
	private static String destroyLiveReq() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Command.DESTROY_LIVE);
		buffer.append(paramsDelimiter);
		
		buffer.append(act1);
		buffer.append(paramsDelimiter);
		
		buffer.append(token1);
		buffer.append(paramsDelimiter);
		
		buffer.append("1");
		buffer.append(paramsDelimiter);
		
		buffer.append("{}");
		
		buffer.append(delimiter);
		return buffer.toString();
	}
}
