package com.prowo.persist;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

/**
 * 根据不同服务器类型代理的不同对象
 */
public class ServerDifferenceObject<T> extends Objectx implements FactoryBean<T>, ApplicationContextAware, ServletContextAware, InitializingBean {

	/**
	 * 目标类型
	 */
	private Class<T> clazz;

	/**
	 * 代理目标的对象池
	 */
	private Map<String, String> pool;

	/**
	 * Servlet 上下文环境
	 */
	private ServletContext sc;

	/**
	 * 目标对象
	 */
	private T target;

	/**
	 * Spring 上下文环境
	 */
	private ApplicationContext context;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (pool != null) {
			String info = sc.getServerInfo();
			for (Entry<String, String> entry : pool.entrySet()) {
				if (info.matches(entry.getKey())) {
					String ref = entry.getValue();
					target = context.getBean(ref, clazz);
					break;
				}
			}
		}
	}

	@Override
	public void setServletContext(ServletContext sc) {
		this.sc = sc;
	}

	@Override
	public T getObject() throws Exception {
		return target;
	}

	@Override
	public Class<T> getObjectType() {
		return clazz;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Map<String, String> getPool() {
		return pool;
	}

	public void setPool(Map<String, String> pool) {
		this.pool = pool;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
}
