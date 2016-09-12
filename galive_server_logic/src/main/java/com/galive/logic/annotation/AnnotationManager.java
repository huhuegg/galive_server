package com.galive.logic.annotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galive.common.utils.PackageUtil;
import com.galive.logic.network.http.HttpRequestHandler;
import com.galive.logic.network.http.handler.HttpBaseHandler;
import com.galive.logic.network.socket.SocketRequestHandler;
import com.galive.logic.network.socket.handler.SocketBaseHandler;

/**
 * 自定义标签工具类 
 * @author Luguangqing
 *
 */
public class AnnotationManager {

	private static Logger logger = LoggerFactory.getLogger(AnnotationManager.class);

	private static Map<String, Class<?>> socketHandlers = new HashMap<>();
	private static Map<String, Class<?>> httpHandlers = new HashMap<>();
	
	/**
	 * 扫描AppLogicHandler所在包 加载所有@LogicHandler标签类
	 */
	public static void initAnnotation() {
		String packageName = SocketBaseHandler.class.getPackage().getName();
		logger.info("扫描包：" + packageName);
		logger.info("加载@LogicHandler");
		List<Class<?>> classes = PackageUtil.getClasssFromPackage(packageName);
		for (Class<?> clazz : classes) {
			SocketRequestHandler handler = clazz.getAnnotation(SocketRequestHandler.class);
			if (handler != null) {
				logger.info(clazz.getName() + ":" + handler.desc() + "(" + handler.command() + ")");
				socketHandlers.put(handler.command(), clazz);
			}
		}
		logger.info("@LogicHandler加载完成");
		
		packageName = HttpBaseHandler.class.getPackage().getName();
		logger.info("扫描包：" + packageName);
		logger.info("加载@HttpRequestHandler");
		classes = PackageUtil.getClasssFromPackage(packageName);
		for (Class<?> clazz : classes) {
			HttpRequestHandler handler = clazz.getAnnotation(HttpRequestHandler.class);
			if (handler != null) {
				logger.info(clazz.getName() + ":" + handler.desc() + "(" + handler.command() + ")");
				httpHandlers.put(handler.command(), clazz);
			}
		}
		logger.info("@HttpRequestHandler加载完成");
	}
	
	/**
	 * 反射读取标签类
	 * @param messageID
	 * @return
	 */
	public static SocketBaseHandler createSocketHandlerInstance(String command) {
		SocketBaseHandler handler = null;
		Class<?> clazz = socketHandlers.get(command);
		if (clazz != null) {
			try {
				handler = (SocketBaseHandler) clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return handler;
	}
	
	/**
	 * 反射读取标签类
	 * @param messageID
	 * @return
	 */
	public static HttpBaseHandler createHttpHandlerInstance(String command) {
		HttpBaseHandler handler = null;
		Class<?> clazz = httpHandlers.get(command);
		if (clazz != null) {
			try {
				handler = (HttpBaseHandler) clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return handler;
	}
	
	
}
