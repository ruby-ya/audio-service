package com.example.audioservice.util;

/**
 * @author Ruby
 * @create 2020-10-02 13:09:51
 */

public class TaskHolder {

    private volatile static boolean isRun = false;

    public static boolean isIsRun() {
        return isRun;
    }

    public static synchronized void setIsRun(boolean isRun) {
        TaskHolder.isRun = isRun;
    }
}
