package com.prowo.persist;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public class CacheZone implements CacheManager {
	private CacheManager cacheManager;

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public Cache getCache(String name) {
		return cacheManager.getCache(name);
	}

	@Override
	public Collection<String> getCacheNames() {
		return cacheManager.getCacheNames();
	}
}
