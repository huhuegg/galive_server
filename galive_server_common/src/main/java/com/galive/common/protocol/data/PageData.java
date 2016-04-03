package com.galive.common.protocol.data;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.galive.common.protocol.RetCode;
import com.galive.common.protocol.params.PageParams;


public class PageData<T> extends BaseData {

	public int index = 0;
	
	public int size = 20;
	
	public PageData(String command, PageParams in) {
		super(command);
		this.index = in.index;
		this.size = in.size;
	}
	
	public void setData(List<T> data) {
		this.objs = data;
		this.setRet_code(RetCode.SUCCESS);
		boolean isEmpty = CollectionUtils.isEmpty(data);
		if (index == 0) {
			if (isEmpty) {
				this.setRet_code(RetCode.NO_DATA);
			}
		} else {
			if (isEmpty) {
				this.setRet_code(RetCode.NO_MORE);
			}
		}
	}
	
	
}
