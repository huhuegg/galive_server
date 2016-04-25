package com.galive.logic.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.galive.logic.dao.QuestionDao;
import com.galive.logic.dao.QuestionDaoImpl;
import com.galive.logic.exception.LogicException;
import com.galive.logic.model.Question;
import com.galive.logic.model.Question.QuestionState;


public class QuestionServiceImpl extends BaseService implements QuestionService {

	private QuestionDao questionDao = new QuestionDaoImpl();
	
	public QuestionServiceImpl() {
		super();
		appendLog("QuestionServiceImpl");
	}
	
	@Override
	public Question create(String desc, List<String> imageUrls, String recordUrl, List<String> tags)
			throws LogicException {
		appendLog("-创建问题-");
		if (StringUtils.isBlank(desc) && CollectionUtils.isEmpty(imageUrls) && StringUtils.isBlank(recordUrl)) {
			appendLog("问题描述不完整。");
			throw new LogicException("问题描述不完整。");
		}
		if (CollectionUtils.isEmpty(tags)) {
			appendLog("未选择问题标签。");
			throw new LogicException("未选择问题标签。");
		}
		Question q = new Question();
		q.setDesc(desc);
		q.setImageUrls(imageUrls);
		q.setRecordUrl(recordUrl);
		q.setState(QuestionState.Pending);
		q.setTags(tags);
		q = questionDao.saveOrUpdate(q);
		appendLog(q.desc() + "保存成功");
		return q;
	}

	@Override
	public List<Question> listByCreateTime(int index, int size) throws LogicException {
		List<Question> questions = questionDao.listByCreateTime(index, index + size - 1);
		return questions;
	}

	@Override
	public Question findBySid(String questionSid) throws LogicException {
		Question q = questionDao.find(questionSid);
		if (q == null) {
			throw new LogicException("问题不存在。");
		}
		return q;
	}

	@Override
	public long countQuestion(String userSid) throws LogicException {
		long count = questionDao.count(userSid);
		appendLog("问题总数:" + count);
		return count;
	}

}
