package com.prowo.ydnamic.thread;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;
import com.prowo.ymlchain.thread.NamedTask;

public abstract class NamedPollingTask implements NamedTask {

    protected abstract long getSleepMills();

    protected abstract void polling() throws Exception;

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(getSleepMills());
                polling();
            } catch (InterruptedException e) {
                LoggerUtil.log(Level.ERROR, e, getName() + " is interrupted!");
                throw new RuntimeException();
            } catch (Exception e0) {
                LoggerUtil.log(Level.ERROR, e0, getName() + " encounter exception!");
            }

        }
    }

}
