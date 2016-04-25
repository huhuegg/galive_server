package com.galive.logic.dao;

import org.mongodb.morphia.query.Query;

import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
import com.galive.logic.model.Answer;

public class AnswerDaoImpl implements AnswerDao {

	private MongoDao<Answer> dao;

	public AnswerDaoImpl() {
		super();
		dao = new MongoDao<Answer>(Answer.class, MongoManager.store);
	}
	
	@Override
	public long count(String userSid) {
		Query<Answer> q = dao.createQuery();
		q.field("userSid").equal(userSid);
		return dao.count(q);
	}

}
