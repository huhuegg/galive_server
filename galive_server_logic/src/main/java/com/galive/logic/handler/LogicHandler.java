package com.galive.logic.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 自定义@LogicHandler标签 用于处理Websocket业务逻辑
 * @author Luguangqing
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicHandler {

	public String commandID(); // MessageID 请求id
	public String desc(); // 文字说明  无业务用途

}
