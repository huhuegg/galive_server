package com.galive.logic.dao.db;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

public class MongoManager {

	//private static Logger logger = LoggerFactory.getLogger(MongoManager.class);

	public static Datastore store = null;
	private static MongoConfig config;
	
	static {
		config = MongoConfig.loadConfig();
		MongoClientOptions.Builder builder = MongoClientOptions.builder();
		builder.connectionsPerHost(config.getPoolSize());
		builder.threadsAllowedToBlockForConnectionMultiplier(config.getThreadsAllowedToBlockForConnectionMultiplier());
		builder.maxWaitTime(config.getMaxWaitTime());
		builder.connectTimeout(config.getConnectTimeout());
		builder.socketTimeout(config.getSocketTimeout());
		builder.socketKeepAlive(config.isSocketKeepAlive());
		MongoClientOptions opt = builder.build();

		ServerAddress addr = new ServerAddress(config.getHost(), config.getPort());
		String dbName = config.getDbName();
		/*MongoCredential cre = MongoCredential.createCredential(config.getUsername(), dbName, config.getPassword().toCharArray());
		MongoClient mongo = new MongoClient(addr, Arrays.asList(cre), opt);
		Morphia morphia = new Morphia();*/
		
		MongoClient mongo = new MongoClient(addr, opt);
		Morphia morphia = new Morphia();
		store = morphia.createDatastore(mongo, dbName);
		store.ensureIndexes(); // 创建索引
		store.ensureCaps(); // 设置默认的mongoDB集合容量
	}
}
