package com.galive.logic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.query.Query;
import com.galive.logic.config.ApplicationConfig;
import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
import com.galive.logic.dao.db.RedisManager;
import com.galive.logic.helper.LogicHelper;
import com.galive.logic.model.Sid.EntitySeq;
import com.galive.logic.model.UserOnlineState;
import redis.clients.jedis.Jedis;

@Entity(value="user", noClassnameStored = true)
@Indexes({@Index("username")})
public class User extends BaseEntity {

	private String username = "";

	private String password = "";

	private String nickname = "";

	private String avatar = "";
	
	private UserGender gender = UserGender.Unknown;
	
	
	/* ======================= Method ======================= */
	public static UserOnlineState onlineState(String userSid) {
//		Session session = LogicHandler.WSClients.get(userSid);
//		if (session != null && session.isOpen()) {
//			return UserOnlineState.Online;
//		}
		// TODO onlineState
		return UserOnlineState.Offline;
	}
	
	/* ======================= DAO ======================= */

	private static MongoDao<User> getDao() {
		return new MongoDao<User>(User.class, MongoManager.store);
	}
	
	public void save() {
		sid = Sid.getNextSequence(EntitySeq.User) + "";
		getDao().save(this);
	}
	
	public static User findBySid(String sid) {
		MongoDao<User> dao = getDao();
		Query<User> q = dao.createQuery();
		q.field("sid").equal(sid);
		User user = dao.findOne(q);
		return user;
	}
	
	public static User findByUsername(String username) {
		MongoDao<User> dao = getDao();
		Query<User> q = dao.createQuery();
		q.field("username").equal(username);
		dao.find(q).asList();
		return dao.findOne(q);
	}
	
	public static List<User> list(int startIndex, int endIndex) {
		MongoDao<User> dao = User.getDao();
		Query<User> q = dao.createQuery();
		//q.field("state").equal(PostState.Normal);
//		if (type != null) {
//			q.field("type").equal(type);
//		}
		int size = Math.max(endIndex - startIndex + 1, 1);
		q.limit(size);
		q.offset(startIndex);
		q.order("-createAt");
		List<User> users = dao.find(q).asList();
		return users;
	}
	
	public void updateLatestLogin() {
		String key = userListByLatestLoginKey();
		Jedis j = null;
		try {
			j = RedisManager.getResource();
			j.zadd(key, System.currentTimeMillis(), sid);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
	}
	
	public static List<User> listByLatestLogin(int startIndex, int endIndex) {
		List<User> users = new ArrayList<>();
		Jedis j = null;
		try {
			j = RedisManager.getResource();
			String key = userListByLatestLoginKey();
			Set<String> sets = j.zrevrange(key, startIndex, endIndex);
			for (String sid : sets) {
				User u = findBySid(sid);
				if (u != null) {
					users.add(u);
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
		return users;
	}
	
	/* ======================= Redis ======================= */
	public static void updateDeviceToken(String userSid, String deviceToken) {
		Jedis j = null;
		try {
			j = RedisManager.getResource();
			String key = userDeviceTokenKey(userSid);
			j.hset(key, userSid, deviceToken);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
	}
	
	public static String updateToken(String userSid) {
		Jedis j = null;
		try {
			String token = LogicHelper.generateRandomMd5();
			j = RedisManager.getResource();
			String key = userTokenKey(userSid);
			j.set(key, token);
			j.expire(key, ApplicationConfig.getInstance().getTokenExpire());
			return token;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.returnToPool(j);
		}
		return null;
	}
	
	public static boolean verifyToken(String userSid, String token) {
		Jedis j = null;
		try {
			j = RedisManager.getResource();
			String key = userTokenKey(userSid);
			String existToken = j.get(key);
			if (!StringUtils.isBlank(existToken)) {
				if (existToken.equals(token)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static String userTokenKey(String userSid) {
		return RedisManager.keyPrefix() + "user_token:" + userSid;
	}
	
	private static String userDeviceTokenKey(String userSid) {
		Long id = Long.parseLong(userSid);
		return RedisManager.keyPrefix() + "user_device_token:" + (id % 5);
	}
	
	private static String userListByLatestLoginKey() {
		return RedisManager.keyPrefix() + "user_list:latest_login";
	}
	
	/* ======================= Getter Setter ======================= */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserGender getGender() {
		return gender;
	}

	public void setGender(UserGender gender) {
		this.gender = gender;
	}
	
	
	
}
