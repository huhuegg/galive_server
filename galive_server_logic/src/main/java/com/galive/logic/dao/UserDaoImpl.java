package com.galive.logic.dao;

import org.mongodb.morphia.query.Query;

import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
import com.galive.logic.model.Sid;
import com.galive.logic.model.User;
import com.galive.logic.model.Sid.EntitySeq;

public class UserDaoImpl implements UserDao {

	private MongoDao<User> dao;

	public UserDaoImpl() {
		super();
		dao = new MongoDao<User>(User.class, MongoManager.store);
	}
	
	private User find(Query<User> q) {
		return dao.findOne(q);
	}

	@Override
	public User find(String sid) {
		Query<User> q = dao.createQuery();
		q.field("sid").equal(sid);
		User user = find(q);
		return user;
	}

	@Override
	public User findByUsername(String username) {
		Query<User> q = dao.createQuery();
		q.field("username").equal(username);
		User user = find(q);
		return user;
	}

	@Override
	public User saveOrUpdate(User u) {
		String sid = Sid.getNextSequence(EntitySeq.User) + "";
		u.setSid(sid);
		dao.save(u);
		return u;
	}

}
