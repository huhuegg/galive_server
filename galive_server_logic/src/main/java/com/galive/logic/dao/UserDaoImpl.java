package com.galive.logic.dao;

import org.mongodb.morphia.query.Query;

import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
import com.galive.logic.model.Sid;
import com.galive.logic.model.User;
import com.galive.logic.model.Sid.EntitySeq;

public class UserDaoImpl implements UserDao {

	private User find(Query<User> q) {
		MongoDao<User> dao = getDao();
		return dao.findOne(q);
	}

	private static MongoDao<User> getDao() {
		return new MongoDao<User>(User.class, MongoManager.store);
	}
	
	@Override
	public User findUser(String sid) {
		Query<User> q = getDao().createQuery();
		q.field("sid").equal(sid);
		User user = find(q);
		return user;
	}

	@Override
	public User findUserByUsername(String username) {
		Query<User> q = getDao().createQuery();
		q.field("username").equal(username);
		User user = find(q);
		return user;
	}
	
	@Override
	public User saveUser(User u) {
		String sid = Sid.getNextSequence(EntitySeq.User) + "";
		u.setSid(sid);
		getDao().save(u);
		return u;
	}
	
	

	
}
