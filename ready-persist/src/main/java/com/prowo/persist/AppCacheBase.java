package com.prowo.persist;

/**
 * 应用缓存基类
 */
public abstract class AppCacheBase extends BaseBO {
	protected CacheZone czone;

	public CacheZone getCzone() {
		return czone;
	}

	public void setCzone(CacheZone czone) {
		this.czone = czone;
	}
}
