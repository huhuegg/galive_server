package com.galive.common.protocol.data;

import java.util.ArrayList;
import java.util.List;

public class ListData<T> extends BaseData {


	protected List<T> objs = new ArrayList<T>();

	public List<T> getObjs() {
		return objs;
	}

	public void setObjs(List<T> objs) {
		this.objs = objs;
	}
	
}
