package com.prowo.ydnamic.thread;

import com.prowo.ydnamic.context.ComxContext;

public abstract class BraveWatchChild extends BraveWatchMan {
    private Object[] objs;

    protected BraveWatchChild(Object... objs) {
        this.objs = objs;
    }

    protected Object getObj(int index) {
        return objs[index];
    }

    @Override
    public void run() {
        init();
        for (int i = 0; i < 3; i++) {
            try {
                if (isNapFirst()) {
                    nap();
                    autoAddPatrolTimes();
                    patrol();
                } else {
                    autoAddPatrolTimes();
                    patrol();
                    nap();
                }
            } catch (InterruptedException e) {
                replyDisturbNap(e);
                continue;
            } catch (Exception e0) {
                dealPatrolException(e0);
                i--;
            }
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
        return Integer.parseInt(ComxContext.getContext().get(getName() + ".interval") == null ? "3000" : ComxContext
                .getContext().get(getName() + ".interval"));
    }

    protected long getSleepMillSec() {
        return 0;
    }

}
