package com.sky.context;

public class BaseContext {
    // 当前线程的id
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    // 设置当前线程的id
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
    // 获取当前线程的id
    public static Long getCurrentId() {
        return threadLocal.get();
    }
    // 移除当前线程的id
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
