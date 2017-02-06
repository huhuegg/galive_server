package galive_server_logic;

import com.alibaba.fastjson.JSON;
import com.galive.logic.db.RedisManager;

import redis.clients.jedis.Jedis;
import sun.plugin.javascript.navig.LinkArray;

import java.util.HashMap;
import java.util.Map;

public class Test {
	
	public static void main(String[] args) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("code", 1);
		map.put("name", "QQ_V5.3.1.dmg");
		map.put("icon", "http://upload5.51yasai.com/attached/upload/201305131827473DZV.jpg");
		map.put("size", 42217910);
		map.put("url", "http://dldir1.qq.com/qqfile/QQforMac/QQ_V5.3.1.dmg");

		String json = JSON.toJSONString(map);
		System.out.println(json);


//		while (true) {
//			for (int i = 0; i < 10; i++) {
//				new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//						Jedis j1 = RedisManager.getInstance().getResource();
//						j1.set("test", System.currentTimeMillis() + "");
//
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							//
//							e.printStackTrace();
//						}
//						RedisManager.getInstance().returnToPool(j1);
//					}
//				}).start();
//			}
//
//
//			Thread.sleep(5000);
//		}
		
	}
	
}
