package com.prowo.persist;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 全局计数器，目前用于动态类生成
 */
public final class Counter {
	private final AtomicInteger count = new AtomicInteger(0);

	public int increment() {
		return count.incrementAndGet();
	}

	public int value() {
		return count.intValue();
	}

	public int reset() {
		return count.getAndSet(0);
	}
}
