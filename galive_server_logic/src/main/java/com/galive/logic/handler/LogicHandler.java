package com.galive.logic.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 自定义@LogicHandler标签 用于处理业务逻辑
 * @author Luguangqing
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicHandler {

	public String command(); // command 请求命令
	public String desc(); // 文字说明  无业务用途

}
