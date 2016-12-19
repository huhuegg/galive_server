package galive_server_logic;

import java.util.List;

import org.apache.http.client.AuthCache;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CreateLiveChannelRequest;
import com.aliyun.oss.model.CreateLiveChannelResult;
import com.aliyun.oss.model.LiveChannelInfo;
import com.aliyun.oss.model.LiveChannelTarget;

public class Test {

	public static void main(String[] args) {
		
		CreateLiveChannelRequest req = new CreateLiveChannelRequest("tubbx", "mac_test");
		OSSClient client = new OSSClient("http://oss-cn-hangzhou.aliyuncs.com", "LTAIPnA1sE4GQcTN", "PDV8GR7v3oQjPdRViFvG7xAEVHHxlG");
		CreateLiveChannelResult result = client.createLiveChannel(req);
		
		List<String> publishUrls = result.getPublishUrls();
		   
		List<String> playUrls = result.getPlayUrls();
		System.out.println("推流地址");
		for (String s : publishUrls) {
			System.out.println(s);
		}
		System.out.println("播放地址");
		for (String s : playUrls) {
			System.out.println(s);
		}
	}
}
