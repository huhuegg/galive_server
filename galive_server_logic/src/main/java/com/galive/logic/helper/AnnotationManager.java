package com.galive.logic.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.common.utils.PackageUtil;
import com.galive.logic.handler.BaseHandler;
import com.galive.logic.handler.LogicHandler;

/**
 * 自定义标签工具类 
 * @author Luguangqing
 *
 */
public class AnnotationManager {

	private static Logger logger = LoggerFactory.getLogger(AnnotationManager.class);

	private static Map<String, Class<?>> logicHandlers = new HashMap<>();
	
	/**
	 * 扫描AppLogicHandler所在包 加载所有@LogicHandler标签类
	 */
	public static void initAnnotation() {
		String packageName = BaseHandler.class.getPackage().getName();
		logger.info("扫描包：" + packageName);
		logger.info("加载@LogicHandler");
		List<Class<?>> classes = PackageUtil.getClasssFromPackage(packageName);
		for (Class<?> clazz : classes) {
			LogicHandler handler = clazz.getAnnotation(LogicHandler.class);
			if (handler != null) {
				logger.info(clazz.getName() + ":" + handler.desc() + "(" + handler.command() + ")");
				logicHandlers.put(handler.command(), clazz);
			}
		}
		logger.info("@LogicHandler加载完成");
	}
	
	/**
	 * 反射读取标签类
	 * @param messageID
	 * @return
	 */
	public static BaseHandler createLogicHandlerInstance(String command) {
		BaseHandler handler = null;
		Class<?> clazz = logicHandlers.get(command);
		if (clazz != null) {
			try {
				handler = (BaseHandler) clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return handler;
	}
	
}
