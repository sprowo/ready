package com.prowo.persist;

/**
 * 可识别对象
 */
public interface Discernible {

	/**
	 * 标识
	 * 
	 * @return
	 */
	public String id();

	/**
	 * 名称
	 * 
	 * @return
	 */
	public String name();
}
