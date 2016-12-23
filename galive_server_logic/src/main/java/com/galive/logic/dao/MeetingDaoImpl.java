package com.galive.logic.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.Query;

import com.galive.logic.db.MongoDao;
import com.galive.logic.db.MongoManager;
import com.galive.logic.db.RedisManager;
import com.galive.logic.model.Meeting;
import com.galive.logic.model.Sid;
import com.galive.logic.model.Sid.EntitySeq;


public class MeetingDaoImpl extends BaseDao implements MeetingDao {

	private MongoDao<Meeting> dao = new MongoDao<Meeting>(Meeting.class, MongoManager.store);
	
	private String shareStartedKey() {
		return RedisManager.getInstance().keyPrefix() + "meeting:share_started";
	}
	
	private String meetingRoomKey(String meetingSid) {
		return RedisManager.getInstance().keyPrefix() + "meeting:room:" + meetingSid;
	}
	
	private String meetingRankByStartTimeKey() {
		return RedisManager.getInstance().keyPrefix() + "meeting:list:start_time";
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
	public Meeting findBySearchName(String meetingSearchName) {
		Query<Meeting> q = dao.createQuery();
		q.field("searchName").equal(meetingSearchName);
		Meeting meeting = dao.findOne(q);
		return meeting;
	}

	@Override
	public Meeting findByAccount(String accountSid) {
		Query<Meeting> q = dao.createQuery();
		q.field("accountSid").equal(accountSid);
		Meeting meeting = dao.findOne(q);
		return meeting;
	}

	@Override
	public Meeting findByMember(String accountSid) {
		Query<Meeting> q = dao.createQuery();
		q.criteria("memberSids").equal(accountSid);
		Meeting meeting = dao.findOne(q);
		return meeting;
	}

	@Override
	public void bindRoom(String meetingSid, String room) {
		String key = meetingRoomKey(meetingSid);
		jedis().set(key, room);
	}

	@Override
	public void unbindRoom(String meetingSid) {
		String key = meetingRoomKey(meetingSid);
		jedis().del(key);
	}
	
	@Override
	public String findRoom(String meetingSid) {
		String key = meetingRoomKey(meetingSid);
		return jedis().get(key);
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
	public boolean findShareState(String accountSid) {
		String key = shareStartedKey();
		boolean started = jedis().sismember(key, accountSid);
		return started;
	}

	@Override
	public void insertToMeetingRank(String meetingSid) {
		String key = meetingRankByStartTimeKey();
		jedis().zadd(key, System.currentTimeMillis(), meetingSid);
	}

	@Override
	public void removeFromMeetingRank(String meetingSid) {
		String key = meetingRankByStartTimeKey(); 
		jedis().zrem(key, meetingSid);
	}

	@Override
	public List<String> meetingRank(long start, long end) {
		String key = meetingRankByStartTimeKey(); 
		Set<String> members = jedis().zrevrange(key, start, end);
		List<String> results = new ArrayList<>();
		for (String s : members) {
			results.add(s);
		}
		return results;
	}

	@Override
	public List<Meeting> meetings(int start, int end) {
		Query<Meeting> q = dao.createQuery();
		int size = Math.max(end - start + 1, 1);
		q.limit(size);
		q.offset(start);
		q.order("-createAt");
		List<Meeting> meetings = dao.find(q).asList();
		return meetings;
	}

	

}
