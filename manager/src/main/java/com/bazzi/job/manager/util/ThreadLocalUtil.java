package com.bazzi.job.manager.util;

import java.util.Map;

public final class ThreadLocalUtil {
    private static final ThreadLocal<Map<String, Object>> parameterThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Object> resultThreadLocal = new ThreadLocal<>();

    public static void setParameter(Map<String, Object> parameter) {
        parameterThreadLocal.set(parameter);
    }

    public static Map<String, Object> getParameter() {
        return parameterThreadLocal.get();
    }

    public static void setResult(Object result) {
        resultThreadLocal.set(result);
    }

    public static Object getResult() {
        return resultThreadLocal.get();
    }

    public static void clearThreadLocal() {
        setParameter(null);
        setResult(null);
    }

}
