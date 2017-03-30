package org.easyproxy.cache;

/**
 * Created by xingtianyu on 17-3-30
 * 下午6:41
 * description:
 */

public enum CacheType {
    REDIS("redis"),
    EHCACHE("ehcache");

    private String name;

    CacheType(String name) {
        this.name = name;
    }

    public static CacheType getCache(String name){
        for (CacheType type:values()){
            if (type.name.equals(name)){
                return REDIS;
            }
        }
        return null;
    }
}
