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


    public static Jedis jedisClient = new Jedis("localhost");

    static {
        System.out.println("Server is running: " + jedisClient.ping());
    }

    public static boolean exists(String key){
        return jedisClient.exists(key);
    }

    public static String get(String key) {
        return jedisClient.get(key);
    }

    public static String hget(String key,String field){
        return jedisClient.hget(key,field);
    }

    public static Map<String,String> hgetAll(String key){
        return jedisClient.hgetAll(key);
    }

    public static Set<String> keys(String pattern){
        return jedisClient.keys(pattern);
    }


    public static void set(String key, String value,boolean expire) {
        jedisClient.set(key, value);
        if (expire)
            jedisClient.expire(key, Config.getInt(Const.CACHE_TTL));
    }
    public static void set(String key, String value) {
        jedisClient.set(key, value);
        jedisClient.expire(key, Config.getInt(Const.CACHE_TTL));
    }

    public static void setList(String listname, List<String> list) {
        for (String item : list) {
            jedisClient.lpush(listname, item);
        }
    }

    public static List<String> getList(String listname) {
        return jedisClient.lrange(listname, 0, jedisClient.strlen(listname));
    }

    public static float getMemoryUsed(){
        System.out.println(jedisClient.info(Const.MEMORY));
        String [] line = jedisClient.info(Const.MEMORY).split("\n");
        String used_memory = line[1].split(":")[1];
        System.out.println("used_memory : "+used_memory);
        return Float.parseFloat(used_memory)/1024;
    }

    public static void incr(String key){
        jedisClient.incr(key);
    }

}
