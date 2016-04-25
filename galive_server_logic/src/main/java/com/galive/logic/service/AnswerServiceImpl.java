package com.galive.logic.service;

import com.galive.logic.dao.AnswerDao;
import com.galive.logic.dao.AnswerDaoImpl;
import com.galive.logic.exception.LogicException;


public class AnswerServiceImpl extends BaseService implements AnswerService {

	private AnswerDao answerDao = new AnswerDaoImpl();
	
	public AnswerServiceImpl() {
		super();
		appendLog("AnswerServiceImpl");
	}

	@Override
	public long countAnswer(String userSid) throws LogicException {
		long count = answerDao.count(userSid);
		appendLog("解答总数:" + count);
		return count;
	}

}
