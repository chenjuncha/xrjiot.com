package com.xrj.business.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring 工具类
 * 
 * @author 
 * @since 1.0
 */
public class SpringUtils implements ApplicationContextAware {
	/**
	 * ApplicationContext
	 */
	private static ApplicationContext applicationContext;

	/**
	 * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
//		LOG.info("Version: "+version);
		SpringUtils.applicationContext = applicationContext;
	}

	/**
	 * @return 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 * 
	 * @param name
	 *            Bean的名称
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 清除applicationContext静态变量.
	 */
	public static void cleanApplicationContext() {
		applicationContext = null;
	}

	/**
	 * 检查是否applicaitonContext未注入
	 */
	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException(
					"applicaitonContext未注入,请在applicationContext.xml中定义SpringUtils");
		}
	}
	
/*	*//**
	 * @param version 当前的版本
	 * @since 3.0.2
	 *//*
	@Value("${version}")
	public void setVersion(String version){
		SpringUtils.version = version;
	}
	*//**
	 * @return 当前的版本
	 * @since 3.0.2
	 *//*
	public static String getVersion(){
		return version;
	}*/
}
