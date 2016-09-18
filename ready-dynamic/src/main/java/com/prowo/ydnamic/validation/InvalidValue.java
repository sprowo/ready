package com.prowo.ydnamic.validation;

import java.io.Serializable;

/**
 * <pre>
 *   Title: InvalidValue.java
 *   Description: 
 *   Project:member project
 *   Copyright: yundaex.com Copyright (c) 2013
 *   Company: shanghai yundaex
 * </pre>
 * 
 * @author Jiangwubo
 * @version 2.0
 * @date 2013-5-9
 */
public class InvalidValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String message;
	private final Object value;
	private final String propertyName;
	@SuppressWarnings("rawtypes")
	private final Class beanClass;
	private final Object bean;
	private Object rootBean;

	public Object getRootBean() {
		return rootBean;
	}

	public String getPropertyPath() {
		return propertyPath;
	}

	private String propertyPath;

	@SuppressWarnings("rawtypes")
	public InvalidValue(String message, Class beanClass, String propertyName, Object value, Object bean) {
		this.message = message;
		this.value = value;
		this.beanClass = beanClass;
		this.propertyName = propertyName;
		this.bean = bean;
		this.rootBean = bean;
		this.propertyPath = propertyName;
	}

	public void addParentBean(Object parentBean, String propertyName) {
		this.rootBean = parentBean;
		this.propertyPath = propertyName + "." + this.propertyPath;
	}

	@SuppressWarnings("rawtypes")
	public Class getBeanClass() {
		return beanClass;
	}

	public String getMessage() {
		return message;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object getValue() {
		return value;
	}

	public Object getBean() {
		return bean;
	}

	public String toString() {
		return propertyName + ' ' + message;
	}
}
