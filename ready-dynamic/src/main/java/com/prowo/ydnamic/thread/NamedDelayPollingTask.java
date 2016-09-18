package com.prowo.ydnamic.thread;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;

public abstract class NamedDelayPollingTask extends NamedPollingTask {
    @Override
    public void run() {
        while (true) {
            try {
                polling();
            } catch (Exception e0) {
                LoggerUtil.log(LoggerUtil.Level.ERROR, e0, getName() + " encounter exception!");
            }

            try {
                Thread.sleep(getSleepMills());
            } catch (InterruptedException e) {
                LoggerUtil.log(Level.ERROR, e, getName() + " is interrupted!");
                throw new RuntimeException();
            }
        }
    }

}
