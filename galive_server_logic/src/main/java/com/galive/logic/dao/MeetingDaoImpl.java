package com.galive.logic.dao;

import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.Query;

import com.galive.logic.dao.cache.RedisManager;
import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.Sid;
import com.galive.logic.model.Sid.EntitySeq;


public class MeetingDaoImpl extends BaseDao implements MeetingDao {

	private MongoDao<Meeting> dao = new MongoDao<Meeting>(Meeting.class, MongoManager.store);
	
	private String shareStartedKey() {
		return RedisManager.getInstance().keyPrefix() + "meeting:share_started";
	}
	
	private String displayIdKey() {
		return RedisManager.getInstance().keyPrefix() + "meeting:display_id:";
	}
	
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

	@Override
	public void saveShareState(String accountSid, boolean start) {
		String key = shareStartedKey();
		if (start) {
			jedis().sadd(key, accountSid);
		} else {
			jedis().srem(key, accountSid);
		}
	}

	@Override
	public boolean loadShareState(String accountSid) {
		String key = shareStartedKey();
		boolean started = jedis().sismember(key, accountSid);
		return started;
	}

	@Override
	public void bindDisplayId(String meetingSid, String mettingDisplayId) {
		String key = displayIdKey();
		
		jedis().hset(key, mettingDisplayId, meetingSid);
		
	}

	@Override
	public String findMeetingId(String mettingDisplayId) {
		String meetingSid = jedis().hget(displayIdKey(), mettingDisplayId);
		return meetingSid;
	}

	@Override
	public void unbundDisplayId(String mettingDisplayId) {
		jedis().hdel(displayIdKey(), mettingDisplayId);
		
	}
}
