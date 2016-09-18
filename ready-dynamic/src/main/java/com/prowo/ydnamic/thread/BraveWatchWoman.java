package com.prowo.ydnamic.thread;

public abstract class BraveWatchWoman extends BraveWatchMan {
    private Object[] objs;

    protected BraveWatchWoman(Object... objs) {
        this.objs = objs;
    }

    protected Object getObj(int index) {
        return objs[index];
    }

    @Override
    public void run() {
        init();
        try {
            if (isNapFirst()) {
                nap();
                patrol();
            } else {
                patrol();
                nap();
            }
        } catch (InterruptedException e) {
            replyDisturbNap(e);
        } catch (Exception e0) {
            dealPatrolException(e0);
        }
        clean();
    }

    protected void clean() {
        WatchManGroup.clean(getName());
    }

    protected int getFromHour() {
        return 0;
    }

    protected int getToHour() {
        return 24;
    }

    protected long getNapMillSec() {
        return 3000;
    }

    protected long getSleepMillSec() {
        return 0;
    }

}
