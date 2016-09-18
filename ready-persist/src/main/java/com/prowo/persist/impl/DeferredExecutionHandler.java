package com.prowo.persist.impl;

import com.prowo.persist.Objectx;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class DeferredExecutionHandler extends Objectx implements RejectedExecutionHandler {

    public DeferredExecutionHandler() {
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            logger.error(e);
        }
        if (!executor.isShutdown()) {
            executor.submit(r);
        }
    }

}
