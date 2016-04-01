package com.galive.common.protocol;

import java.util.ArrayList;
import java.util.List;

public class ListCommandOut<T> extends CommandOut {

	public ListCommandOut(String command) {
		super(command);
	}

	protected List<T> objs = new ArrayList<T>();

	public List<T> getObjs() {
		return objs;
	}

	public void setObjs(List<T> objs) {
		this.objs = objs;
	}
	
}
