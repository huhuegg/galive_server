package com.galive.logic.dao;

import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.Query;

import com.galive.logic.dao.db.MongoDao;
import com.galive.logic.dao.db.MongoManager;
import com.galive.logic.model.PlatformUser;
import com.galive.logic.model.PlatformUser.UserPlatform;
import com.galive.logic.model.Sid;
import com.galive.logic.model.Sid.EntitySeq;

public class PlatformUserDaoImpl implements PlatformUserDao {

	private MongoDao<PlatformUser> dao;

	public PlatformUserDaoImpl() {
		super();
		dao = new MongoDao<PlatformUser>(PlatformUser.class, MongoManager.store);
	}

	private PlatformUser find(Query<PlatformUser> q) {
		return dao.findOne(q);
	}
	
	@Override
	public PlatformUser find(String deviceid, UserPlatform platform) {
		Query<PlatformUser> q = dao.createQuery();
		q.field("deviceid").equal(deviceid);
		q.field("platform").equal(platform);
		PlatformUser user = find(q);
		return user;
	}
	
	@Override
	public PlatformUser findByUdid(String udid, UserPlatform platform) {
		Query<PlatformUser> q = dao.createQuery();
		q.field("udid").equal(udid);
		q.field("platform").equal(platform);
		PlatformUser user = find(q);
		return user;
	}

	@Override
	public PlatformUser saveOrUpdate(PlatformUser u) {
		if (StringUtils.isBlank(u.getSid())) {
			String sid = Sid.getNextSequence(EntitySeq.PlatformUser) + "";
			u.setSid(sid);
		} else {
			PlatformUser existU = find(u.getUdid(), u.getPlatform());
			if (existU != null) {
				u.setId(existU.getId());
			}
		}
		dao.save(u);
		return u;
	}

	
	
}
