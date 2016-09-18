package com.prowo.ydnamic.thread;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class WatchManGroup {
    private static Map<String, Object[]> threads = new ConcurrentHashMap<String, Object[]>();

    public static void registerWatchMan(BraveWatchMan watchMan) {
        if (threads.get(watchMan.getName()) != null) {
            accecptSuicide(watchMan.getName());
        }
        Thread thread = new Thread(watchMan, watchMan.getName());
        threads.put(watchMan.getName(), new Object[]{watchMan, thread});
        LoggerUtil.log(Level.INFO, watchMan.getName() + " start to work");
        thread.start();
    }

    public static void clean(String name) {
        threads.remove(name);
    }

    public static void accecptSuicide(String name) {
        if (threads.get(name) == null) {
            LoggerUtil.log(Level.WRAN, "accecptSuicide faild!can not find WatchMan named [{0}]", name);
            return;
        }
        ((BraveWatchMan) threads.get(name)[0]).accecptSuicide();
        ((Thread) threads.get(name)[1]).interrupt();
    }

    public static void disturbNap(String name) {
        if (threads.get(name) == null) {
            LoggerUtil.log(Level.WRAN, "disturbNap faild!can not find WatchMan named [{0}]", name);
            return;
        }
        ((BraveWatchMan) threads.get(name)[0]).disturbNap();
        ((Thread) threads.get(name)[1]).interrupt();
    }

    public static void wakeupSleep(String name) {
        if (threads.get(name) == null) {
            LoggerUtil.log(Level.WRAN, "awakeSleep faild!can not find WatchMan named [{0}]", name);
            return;
        }
        ((BraveWatchMan) threads.get(name)[0]).wakeupSleep();
        ((Thread) threads.get(name)[1]).interrupt();
    }

    public static void setNap1st(String name) {
        if (threads.get(name) == null) {
            LoggerUtil.log(Level.WRAN, "setNap1st faild!can not find WatchMan named [{0}]", name);
            return;
        }
        ((BraveWatchMan) threads.get(name)[0]).setNap1st();
    }

    public static void setPatrol1st(String name) {
        if (threads.get(name) == null) {
            LoggerUtil.log(Level.WRAN, "setPatrol1st faild!can not find WatchMan named [{0}]", name);
            return;
        }
        ((BraveWatchMan) threads.get(name)[0]).setPatrol1st();
    }

    public static void massSuicide() {
        for (String name : threads.keySet()) {
            accecptSuicide(name);
        }
    }

    public static Map<String, Object> getWatchManInfo(String name) {
        Map<String, Object> allInfo = new HashMap<String, Object>();
        allInfo.putAll(((BraveWatchMan) threads.get(name)[0]).getInfo());
        Thread thread = (Thread) threads.get(name)[1];
        allInfo.put("tid", thread.getId());
        allInfo.put("priority", thread.getPriority());
        allInfo.put("state", thread.getState());
        allInfo.put("stackTraces", Arrays.asList(thread.getStackTrace()).toString());
        return allInfo;
    }

    public static List<Map<String, Object>> getAllWatchMenInfo(final String sort, final String order) {
        List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
        for (String name : threads.keySet()) {
            infoList.add(getWatchManInfo(name));
        }

        return infoList;
    }

    private static Queue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

    public static void produce(Runnable run) {
        queue.offer(run);
    }

    public static Runnable consume() {
        return queue.poll();
    }

}
