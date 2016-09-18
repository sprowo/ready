package com.prowo.ymlchain.thread;

import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadManager {
    private static Map<String, Thread> threads = new ConcurrentHashMap<String, Thread>();
    private static Logger logger = Logger.getLogger(ThreadManager.class);

    public static void registerRunnable(NamedTask runnable) {
        if (threads.get(runnable.getName()) != null) {
            threads.get(runnable.getName()).interrupt();
        }
        Thread thread = new Thread(runnable, runnable.getName());
        threads.put(runnable.getName(), thread);
        logger.info(runnable.getName() + " start");
        thread.start();
    }

    public static void interruptAll() {
        for (Thread thread : threads.values()) {
            thread.interrupt();
        }
    }

    public static Map<String, Object> getThreadInfo(String name) {
        Map<String, Object> allInfo = new HashMap<String, Object>();
        Thread thread = (Thread) threads.get(name);
        allInfo.put("name", thread.getName());
        allInfo.put("tid", thread.getId());
        allInfo.put("priority", thread.getPriority());
        allInfo.put("state", thread.getState());
        allInfo.put("stackTraces", Arrays.asList(thread.getStackTrace()).toString());
        return allInfo;
    }

    public static List<Map<String, Object>> getAllThreadInfo() {
        List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
        for (String name : threads.keySet()) {
            infoList.add(getThreadInfo(name));
        }

        return infoList;
    }
}
