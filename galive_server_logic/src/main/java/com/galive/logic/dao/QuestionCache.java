package com.galive.logic.dao;

import java.util.List;

public interface QuestionCache {

	public void saveTag(String tag);
	
	public void deleteTag(String tag);
	
	public List<String> listTags();
}
