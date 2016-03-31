package com.galive.common.utils;

public class StringBufferLine {

	private StringBuffer buffer = new StringBuffer();
	
	public StringBuffer append(String str) {
		buffer.append(str);
		buffer.append("\n");
		return buffer;
	}
	
	public String toString() {
		return buffer.toString();
	}
	
}
