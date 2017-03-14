package org.easyproxy.util.mem;/**
 * Description : 
 * Created by YangZH on 16-10-5
 *  上午12:40
 */

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description :
 * Created by YangZH on 16-10-5
 * 上午12:40
 */

public class EHcacheUtil implements MemoryUtil{

    private String name;
    private int maxElementsInMemory;
    private boolean overflowToDisk;
    private boolean eternal;
    private boolean copyOnWrite;
    private boolean copyOnRead;
    private int timeToLiveSeconds;
    private int timeToIdleSeconds;
    private String policy;
    public CacheManager manager;
    public CacheConfiguration cacheConfig;
    public Cache cache;
    public EHcacheUtil(){
        cacheConfig = new CacheConfiguration();
        cacheConfig.setName(name);
        cacheConfig.setCopyOnWrite(copyOnWrite);
        cacheConfig.setCopyOnRead(copyOnRead);
        cacheConfig.setEternal(eternal);
        cacheConfig.setTimeToIdleSeconds(timeToIdleSeconds);
        cacheConfig.setTimeToLiveSeconds(timeToLiveSeconds);
        cacheConfig.setMemoryStoreEvictionPolicy(policy);
        manager = CacheManager.create();
        cache = manager.getCache(name);
    }

    public boolean exists(String key){
        return cache==null?false:cache.get(key)!=null;
    }
    public String get(String key) {
        return (String) cache.get(key).getObjectValue();
    }

    public void set(String key, String value,boolean expire) {
        Element ele = new Element(key,value);
        if (expire){
            ele.setTimeToIdle(timeToIdleSeconds);
            ele.setTimeToLive(timeToLiveSeconds);
        }
        cache.put(ele);
        manager.addCache(cache);
    }
    public void set(String key, String value) {
        set(key,value,true);
    }

    public Set<String> keys(String regx){
        Pattern pattern = Pattern.compile(regx);
        Set<String> result = new HashSet<String>();
        List<String> keys = cache.getKeys();
        for (String key:keys){
            Matcher matcher = pattern.matcher(key);
            if (matcher.matches()){
                result.add(key);
            }
        }
        return result;
    }

    public void incr(String key){
        Element ele = cache.get(key);
        int num = (Integer) ele.getObjectKey()+1;
        Element newele = new Element(ele.getObjectKey(),num);
        cache.remove(ele.getObjectKey());
        cache.put(newele);
    }

    public void decr(String key){
        Element ele = cache.get(key);
        int num = (Integer) ele.getObjectKey()-1;
        Element newele = new Element(ele.getObjectKey(),num);
        cache.remove(ele.getObjectKey());
        cache.put(newele);
    }

    public void clear(String key){
        cache.remove(key);
    }

    public void clear(){
        cache.removeAll();
    }
}
