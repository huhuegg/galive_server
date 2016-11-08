package com.galive.logic.dao;

import com.galive.logic.dao.cache.RedisManager;
import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
import com.galive.logic.model.Meeting;


public class MeetingDaoImpl extends BaseDao implements MeetingDao {

	private MongoDao<Meeting> dao = new MongoDao<Meeting>(Meeting.class, MongoManager.store);
	
	private String shareStartedKey() {
		return RedisManager.getInstance().keyPrefix() + "meeting:share_started";
	}

	@Override
	public Meeting saveOrUpdate(Meeting meeting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting find(String meetingSid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting findBySearchName(String meetingSearchName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting findByAccount(String accountSid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting findByMember(String accountSid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bindRoom(String meetingSid, String room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unbindRoom(String meetingSid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveShareState(String accountSid, boolean start) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean findShareState(String accountSid) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*@Override
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
	public Meeting findMeetingBySearchName(String meetingSearchName) {
		Query<Meeting> q = dao.createQuery();
//		q.criteria("members.accountSid").equal(accountSid);
		q.field("options.searchName").equal(meetingSearchName);
		Meeting meeting = dao.findOne(q);
		return meeting;
	}*/

}
