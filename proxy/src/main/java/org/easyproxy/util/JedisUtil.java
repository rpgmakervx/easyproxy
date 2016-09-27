package org.easyproxy.util;/**
 * Description : 
 * Created by YangZH on 16-6-4
 *  上午10:50
 */

import org.easyproxy.constants.Const;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description :
 * Created by YangZH on 16-6-4
 * 上午10:50
 */

public class JedisUtil {


    public Jedis jedisClient;
    public JedisUtil(){
        jedisClient = new Jedis("localhost");
    }

    public boolean exists(String key){
        return jedisClient.exists(key);
    }

    public String get(String key) {
        System.out.println("jedis get("+key+") ");
        return jedisClient.get(key);
    }

    public String hget(String key,String field){
        return jedisClient.hget(key,field);
    }

    public Map<String,String> hgetAll(String key){
        return jedisClient.hgetAll(key);
    }

    public Set<String> keys(String pattern){
        return jedisClient.keys(pattern);
    }


    public void set(String key, String value,boolean expire) {
        jedisClient.set(key, value);
        if (expire)
            jedisClient.expire(key, Config.getInt(Const.CACHE_TTL));
    }
    public void set(String key, String value) {
        set(key,value,true);
    }

    public void setList(String listname, List<String> list) {
        for (String item : list) {
            jedisClient.lpush(listname, item);
        }
    }

    public List<String> getList(String listname) {
        return jedisClient.lrange(listname, 0, jedisClient.strlen(listname));
    }

    public float getMemoryUsed(){
        System.out.println(jedisClient.info(Const.MEMORY));
        String [] line = jedisClient.info(Const.MEMORY).split("\n");
        String used_memory = line[1].split(":")[1];
        System.out.println("used_memory : "+used_memory);
        return Float.parseFloat(used_memory)/1024;
    }

    public void incr(String key){
        jedisClient.incr(key);
    }

    public void clear(String key){
        jedisClient.del(key);
    }

    public void clear(){
        jedisClient.flushAll();
    }
}
