package com.galive.logic.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.Query;

import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
import com.galive.logic.model.Answer;
import com.galive.logic.model.Sid;
import com.galive.logic.model.Answer.AnswerResult;
import com.galive.logic.model.Sid.EntitySeq;

public class AnswerDaoImpl implements AnswerDao {

	private MongoDao<Answer> dao;

	public AnswerDaoImpl() {
		super();
		dao = new MongoDao<Answer>(Answer.class, MongoManager.store);
	}
	
	private Answer find(Query<Answer> q) {
		return dao.findOne(q);
	}
	
	@Override
	public Answer saveOrUpdate(Answer a) {
		if (StringUtils.isBlank(a.getSid())) {
			String sid = Sid.getNextSequence(EntitySeq.Answer) + "";
			a.setSid(sid);
		} else {
			Answer existA = find(a.getSid());
			if (existA != null) {
				a.setId(existA.getId());
			}
		}
		dao.save(a);
		return a;
	}
	
	@Override
	public Answer find(String answerSid) {
		Query<Answer> q = dao.createQuery();
		q.field("sid").equal(answerSid);
		Answer answer = find(q);
		return answer;
	}
	
	@Override
	public long count(String userSid, AnswerResult result) {
		Query<Answer> q = dao.createQuery();
		q.field("userSid").equal(userSid);
		if (result != null) {
			q.field("result").equal(result);
		}
		return dao.count(q);
	}

	@Override
	public List<Answer> listByQuestion(String questionSid, int start, int end) {
		Query<Answer> q = dao.createQuery();
		int size = Math.max(end - start + 1, 1);
		q.limit(size);
		q.offset(start);
		q.order("-createAt");
		List<Answer> answers = dao.find(q).asList();
		return answers;
	}

	@Override
	public List<Answer> listAllByQuestion(String questionSid) {
		Query<Answer> q = dao.createQuery();
		q.order("-createAt");
		List<Answer> answers = dao.find(q).asList();
		return answers;
	}

	

}
