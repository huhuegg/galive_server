package com.galive.logic.network.http;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HttpRequestHandler {

	public String command();
	public String desc();

}
