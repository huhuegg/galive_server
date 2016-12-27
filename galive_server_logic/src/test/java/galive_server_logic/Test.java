package galive_server_logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.logic.db.RedisManager;

import redis.clients.jedis.Jedis;

public class Test {

	private static Logger logger = LoggerFactory.getLogger(Test.class);
	
	public static void main(String[] args) throws Exception {
		while (true) {
			for (int i = 0; i < 10; i++) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Jedis j1 = RedisManager.getInstance().getResource();
						j1.set("test", System.currentTimeMillis() + "");
						
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						RedisManager.getInstance().returnToPool(j1);
					}
				}).start();
			}
			
			
			Thread.sleep(5000);
		}
		
	}
	
}
