package com.prowo.ydnamic.thread;

import com.prowo.ydnamic.context.ComxContext;
import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;

import java.math.BigInteger;
import java.util.*;

public abstract class BraveWatchMan implements Runnable {
    public abstract String getName();

    private Calendar calendar;

    protected int getHours() {
        calendar = new GregorianCalendar();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private Date startDate;
    private BigInteger napTimes;
    private BigInteger patrolTimes;
    private BigInteger sleepTimes;

    protected void init() throws RuntimeException {
        startDate = new Date();
        napTimes = new BigInteger("0");
        patrolTimes = new BigInteger("0");
        sleepTimes = new BigInteger("0");
    }

    protected void clean() {
    }

    protected void autoAddPatrolTimes() {
        patrolTimes = patrolTimes.add(BigInteger.ONE);

    }

    @Override
    public void run() {
        init();
        while (true) {
            if (getHours() >= getFromHour() && getHours() <= getToHour())
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
                    continue;
                } catch (InterruptedException e) {
                    replyDisturbNap(e);
                    continue;
                } catch (Exception e0) {
                    dealPatrolException(e0);
                    continue;
                }
            try {
                sleep();
            } catch (InterruptedException e) {
                replyAwakeSleep(e);
            }
        }
    }

    protected abstract void patrol() throws Exception;

    private boolean napFirst = true;

    protected boolean isNapFirst() {
        return this.napFirst;
    }

    public void setNap1st() {
        this.napFirst = true;
    }

    public void setPatrol1st() {
        this.napFirst = false;
    }

    protected void dealPatrolException(Exception e) {
        LoggerUtil.log(Level.ERROR, e, getName() + " encounter exception!");
    }

    protected int getFromHour() {
        return Integer.parseInt(ComxContext.getContext().get(getName() + ".from.hour") == null ? "9" : ComxContext
                .getContext().get(getName() + ".from.hour"));
    }

    protected int getToHour() {
        return Integer.parseInt(ComxContext.getContext().get(getName() + ".to.hour") == null ? "17" : ComxContext
                .getContext().get(getName() + ".to.hour"));
    }

    protected long getNapMillSec() {
        return Integer.parseInt(ComxContext.getContext().get(getName() + ".interval") == null ? "600000" : ComxContext
                .getContext().get(getName() + ".interval"));
    }

    protected void nap() throws InterruptedException {
        this.stage = STAGE.NAP;
        napTimes = napTimes.add(BigInteger.ONE);
        Thread.sleep(getNapMillSec());
        this.stage = STAGE.PATROL;
    }

    public void disturbNap() {
        this.interuptCause = INTERUPT_CAUSE.DISTURB_NAP;
    }

    protected void replyDisturbNap(InterruptedException e) {
        switch (interuptCause) {
            case DISTURB_NAP:
                LoggerUtil.log(Level.WRAN, e, getName() + " is interupted by " + interuptCause);
                this.stage = STAGE.PATROL;
                break;
            case ACCEPT_SUICIDE:
                LoggerUtil.log(Level.WRAN, e, getName() + " is interupted by " + interuptCause);
                clean();
                throw new RuntimeException();
            default:
                LoggerUtil.log(Level.WRAN, e, getName() + " interupted failed at nap by interuptCause: " + interuptCause);
                break;
        }
    }

    protected long getSleepMillSec() {
        return Integer.parseInt(ComxContext.getContext().get(getName() + ".sleeping") == null ? "1800000" : ComxContext
                .getContext().get(getName() + ".sleeping"));
    }

    protected void sleep() throws InterruptedException {
        this.stage = STAGE.SLEEP;
        sleepTimes = sleepTimes.add(BigInteger.ONE);
        Thread.sleep(getSleepMillSec());
        this.stage = STAGE.PATROL;
    }

    public void wakeupSleep() {
        this.interuptCause = INTERUPT_CAUSE.AWAKE_SLEEP;
        this.stage = STAGE.PATROL;
    }

    protected void replyAwakeSleep(InterruptedException e) {
        switch (interuptCause) {
            case AWAKE_SLEEP:
                LoggerUtil.log(Level.WRAN, e, getName() + " is interupted by " + interuptCause);
                this.stage = STAGE.PATROL;
                break;
            case ACCEPT_SUICIDE:
                LoggerUtil.log(Level.WRAN, e, getName() + " is interupted by " + interuptCause);
                throw new RuntimeException();
            default:
                LoggerUtil.log(Level.WRAN, e, getName() + " interupted failed at sleep by interuptCause: " + interuptCause);
                break;
        }
    }

    public void accecptSuicide() {
        this.interuptCause = INTERUPT_CAUSE.ACCEPT_SUICIDE;
    }

    private INTERUPT_CAUSE interuptCause = INTERUPT_CAUSE.ACCEPT_SUICIDE;

    public enum INTERUPT_CAUSE {
        DISTURB_NAP, AWAKE_SLEEP, ACCEPT_SUICIDE;
    }

    private STAGE stage = STAGE.PATROL;

    public enum STAGE {
        NAP, SLEEP, PATROL;
    }

    public Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("name", getName());
        info.put("stage", this.stage);
        info.put("isNapFirst", isNapFirst());
        info.put("fromHour", getFromHour());
        info.put("toHour", getToHour());
        info.put("napMillSec", getNapMillSec());
        info.put("sleepMillSec", getSleepMillSec());
        info.put("interuptCause", this.interuptCause);
        info.put("napTimes", this.napTimes);
        info.put("patrolTimes", this.patrolTimes);
        info.put("sleepTimes", this.sleepTimes);
        info.put("startDate", this.startDate);
        return info;

    }
}
