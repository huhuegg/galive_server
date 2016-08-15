package galive_server_logic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.galive.logic.config.ApplicationConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class TestChannelManager {

	private Map<String, ChannelHandlerContext> clientChannels = new ConcurrentHashMap<>();
	
	public static final AttributeKey<String> ACCOUNT_KEY = AttributeKey.valueOf("account"); 
	
	private static TestChannelManager instance = null;
	
	private String delimiter = "";
	
	public static TestChannelManager getInstance() {
		if (instance == null) {
			instance = new TestChannelManager();
			instance.delimiter = ApplicationConfig.getInstance().getSocketConfig().getMessageDelimiter();
		}
		return instance;
	}
	
	public ChannelHandlerContext findChannel(String account) {
		return clientChannels.get(account);
	}
	
	public void addChannel(String account, ChannelHandlerContext context) {
		context.attr(ACCOUNT_KEY).set(account);
		clientChannels.put(account, context);
	}
	
	public void removeChannel(String account) {
		clientChannels.remove(account);
	}
	
	public void closeAndRemoveChannel(String account) {
		ChannelHandlerContext context = clientChannels.get(account);
		closeChannel(context);
		clientChannels.remove(account);
	}
	
	public long channelCount() {
		return clientChannels.size();
	}
	
	public void sendMessage(String account, String message) {
		if (message != null) {
			ChannelHandlerContext context = clientChannels.get(account);
			if (context != null && context.channel().isActive()) {
				message += delimiter;
				context.writeAndFlush(message);
			}
		}
	}
	
	public static void closeChannel(ChannelHandlerContext ctx) {
		if (ctx != null) {
			ctx.flush();
			ctx.close();
		}
	}

}
