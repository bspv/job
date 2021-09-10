package com.bazzi.job.manager.util;

public final class Constant {
    //用户信息在redis中的key
    public static final String SESSION_USER_KEY = "%s:user:%s";
    //用户在redis中的有效时间，30分钟
    public static final int USER_EXPIRE_TIME = 30 * 60;
    //token对应的请求头name
    public static final String TOKEN_HEADER = "DYNAMIC-TOKEN";

    //管理员用户名
    public static final String ADMIN_NAME = "admin";


    //同步总计的锁key
    public static final String SYNC_TOTAL_LOCK_KEY = "%s:sync_total_lock";
    //同步项目统计的锁key
    public static final String SYNC_PROJECT_LOCK_KEY = "%s:sync_project_lock";
    //同步数据锁的时间，5分钟
    public static final long SYNC_LOCK_TIME = 5 * 60 * 1000;


    //项目在redis中缓存的key
    public static final String PROJECT_CACHE_KEY = "%s:project_in_cache:%s";

}
