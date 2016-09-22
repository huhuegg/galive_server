package com.galive.logic.dao;

import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.Query;

import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.Sid;
import com.galive.logic.model.Sid.EntitySeq;

public class MeetingDaoImpl extends BaseDao implements MeetingDao {

	private MongoDao<Meeting> dao = new MongoDao<Meeting>(Meeting.class, MongoManager.store);
	
	@Override
	public Meeting saveOrUpdate(Meeting meeting) {
		if (StringUtils.isEmpty(meeting.getSid())) {
			String sid = String.valueOf(Sid.getNextSequence(EntitySeq.Meeting));
			meeting.setSid(sid);
		}
		dao.save(meeting);
		return meeting;
	}

	@Override
	public Meeting find(String meetingSid) {
		Query<Meeting> q = dao.createQuery();
		q.field("sid").equal(meetingSid);
		Meeting meeting = dao.findOne(q);
		return meeting;
	}

	@Override
	public Meeting delete(Meeting meeting) {
		dao.delete(meeting);
		return meeting;
	}

	@Override
	public Meeting findByAccount(String accountSid) {
		Query<Meeting> q = dao.createQuery();
//		q.criteria("members.accountSid").equal(accountSid);
		q.field("members.accountSid").equal(accountSid);
		Meeting meeting = dao.findOne(q);
		return meeting;
	}
}
