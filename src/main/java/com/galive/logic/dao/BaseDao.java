package com.galive.logic.dao;

import com.galive.logic.db.RedisManager;

public class BaseDao {

	protected RedisManager redis = RedisManager.getInstance();

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
	
}
