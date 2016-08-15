package galive_server_logic;

import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.galive.common.protocol.Command;

public class TokenRequest {

	private static final CloseableHttpClient httpclient = HttpClients.createDefault();
	
	public static String req(String account) throws Exception {
		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost("127.0.0.1")
				.setPort(4050)
				.setPath("/galive/logic")
				.setParameter("command", Command.REQ_TOKEN)
				.setParameter("account", account)
				.build();
		HttpPost post = new HttpPost(uri);
		CloseableHttpResponse httpresponse = httpclient.execute(post);  
		// 获取返回数据  
        HttpEntity entity = httpresponse.getEntity();  
        String body = EntityUtils.toString(entity);  
        TokenResponse resp = JSON.parseObject(body, TokenResponse.class);
        return resp.token;
	}
	
	public static class TokenResponse {
		public String token;
	}
}
