package org.easyproxy.cache;/**
 * Description : 
 * Created by YangZH on 16-10-4
 *  下午9:43
 */

import org.easyproxy.cache.ehcache.EHcache;
import org.easyproxy.cache.redis.RedisCache;
import org.easyproxy.config.ConfigEnum;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.config.PropertyConfig;

import java.net.InetSocketAddress;

import static org.easyproxy.constants.Const.*;

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
            cacheType = ConfigFactory.getConfig().getString(ConfigEnum.CACHE_TYPE.key);
        }else {
            cacheType = ConfigFactory.getConfig().getString(CACHE_TYPE);
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

    /**
     * 初始化缓存中的值，现阶段只有 每个ip对应的连接数
     */
    public static void initCacheValue(){
        cache = CacheManager.getCache();
        for (InetSocketAddress address:ConfigFactory.getConfig().getServers()){
            cache.addAccessRecord(address.getHostString()+":"+address.getPort()+ACCESSRECORD);
        }
    }

    public static DefaultCache getCache(){
        return cache;
    }

    public static void setCache(DefaultCache cache){
        CacheManager.cache = cache;
    }
}
