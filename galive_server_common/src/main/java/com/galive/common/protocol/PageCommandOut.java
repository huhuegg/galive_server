package com.galive.common.protocol;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;


public class PageCommandOut<T> extends ListCommandOut<T> {

	public int index = 0;
	
	public int size = 20;
	
	public PageCommandOut(Command command, PageCommandIn in) {
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
