package org.easyproxy.cache;/**
 * Description : 
 * Created by YangZH on 16-10-4
 *  下午9:43
 */

import org.easyproxy.cache.ehcache.EHcache;
import org.easyproxy.cache.redis.RedisCache;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.config.PropertyConfig;

import static org.easyproxy.config.ConfigEnum.CACHE_TYPE;


/**
 * Description :
 * Created by YangZH on 16-10-4
 * 下午9:43
 */

public class CacheManager {

    private static DefaultCache cache;

    public static void init(){
        System.out.println("config type --> "+ConfigFactory.getConfig());
        String cacheType = "redis";
        if (ConfigFactory.getConfig() instanceof PropertyConfig){
            cacheType = ConfigFactory.getConfig().getString(CACHE_TYPE.key);
        }else {
            cacheType = ConfigFactory.getConfig().getString(CACHE_TYPE.key);
        }
        switch (cacheType){
            case REDIS:
                cache = new RedisCache();
                break;
            case EHCACHE:
                cache = new EHcache();
                break;
            case JAVA:
                break;
        }
    }

    public static DefaultCache getCache(){
        return cache;
    }

    public static void setCache(DefaultCache cache){
        CacheManager.cache = cache;
    }
}
