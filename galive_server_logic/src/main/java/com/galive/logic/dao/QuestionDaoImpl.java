package com.galive.logic.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.Query;
import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
import com.galive.logic.model.Question;
import com.galive.logic.model.Sid;
import com.galive.logic.model.Sid.EntitySeq;

public class QuestionDaoImpl implements QuestionDao {

	private MongoDao<Question> dao;

	public QuestionDaoImpl() {
		super();
		dao = new MongoDao<Question>(Question.class, MongoManager.store);
	}
	
	private Question find(Query<Question> q) {
		return dao.findOne(q);
	}
	
	@Override
	public Question saveOrUpdate(Question q) {
		if (StringUtils.isBlank(q.getSid())) {
			String sid = Sid.getNextSequence(EntitySeq.Question) + "";
			q.setSid(sid);
		} else {
			Question existQ = find(q.getSid());
			if (existQ != null) {
				q.setId(existQ.getId());
			}
		}
		dao.save(q);
		return q;
	}
	
	@Override
	public Question find(String sid) {
		Query<Question> q = dao.createQuery();
		q.field("sid").equal(sid);
		Question question = find(q);
		return question;
	}

	@Override
	public List<Question> listByCreateTime(int start, int end) {
		Query<Question> q = dao.createQuery();
		int size = Math.max(end - start + 1, 1);
		q.limit(size);
		q.offset(start);
		q.order("-createAt");
		List<Question> questions = dao.find(q).asList();
		return questions;
	}
	
	@Override
	public List<Question> listByUser(String userSid, int start, int end) {
		Query<Question> q = dao.createQuery();
		int size = Math.max(end - start + 1, 1);
		q.field("userSid").equal(userSid);
		q.limit(size);
		q.offset(start);
		q.order("-createAt");
		List<Question> questions = dao.find(q).asList();
		return questions;
	}

	@Override
	public long count(String userSid) {
		Query<Question> q = dao.createQuery();
		q.field("userSid").equal(userSid);
		return dao.count(q);
	}

}
