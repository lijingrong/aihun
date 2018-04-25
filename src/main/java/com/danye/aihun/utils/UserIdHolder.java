package com.danye.aihun.utils;

public final class UserIdHolder {

    private static ThreadLocal<String> userIdHolder = new ThreadLocal<>();

    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    public static String getUserId() {
        return userIdHolder.get();
    }

    public static void empty() {
        userIdHolder.remove();
    }
}
