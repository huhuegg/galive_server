package com.galive.logic.network.socket;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SocketRequestHandler {

	public String command(); 
	public String desc();

}
