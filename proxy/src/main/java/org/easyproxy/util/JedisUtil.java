package org.easyproxy.util;/**
 * Description : 
 * Created by YangZH on 16-6-4
 *  上午10:50
 */

import org.easyproxy.constants.Const;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description :
 * Created by YangZH on 16-6-4
 * 上午10:50
 */

public class JedisUtil {


//    public Jedis jedisClient;
    private JedisPool pool = null;
    public JedisUtil(){
        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(100);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(1000 * 30);
        config.setMaxTotal(1000);
        pool = new JedisPool(config, "127.0.0.1", 6379);


    }

    public void recoverJedis(Jedis jedis){
        System.out.println("jedis 回收");
        pool.returnResourceObject(jedis);
    }

    public boolean exists(String key){
        Jedis jedis = pool.getResource();
        boolean exists = jedis.exists(key);
        recoverJedis(jedis);
        return exists;
    }

    public String get(String key) {
        System.out.println("jedis get("+key+") ");
        Jedis jedis = pool.getResource();
        String value = jedis.get(key);
        recoverJedis(jedis);
        return value;
    }

    public String hget(String key,String field){
        Jedis jedis = pool.getResource();
        String value = jedis.hget(key, field);
        recoverJedis(jedis);
        return value;
    }

    public Map<String,String> hgetAll(String key){
        Jedis jedis = pool.getResource();
        Map<String,String> value = jedis.hgetAll(key);
        recoverJedis(jedis);
        return value;
    }

    public Set<String> keys(String pattern){
        Jedis jedis = pool.getResource();
        Set<String> value = jedis.keys(pattern);
        recoverJedis(jedis);
        return value;
    }


    public void set(String key, String value,boolean expire) {
        Jedis jedis = pool.getResource();
        jedis.set(key, value);
        if (expire)
            jedis.expire(key, Config.getInt(Const.CACHE_TTL));
        recoverJedis(jedis);
    }
    public void set(String key, String value) {
        set(key,value,true);
    }

    public void setList(String listname, List<String> list) {
        Jedis jedis = pool.getResource();
        for (String item : list) {
            jedis.lpush(listname, item);
        }
        recoverJedis(jedis);
    }

    public List<String> getList(String listname) {
        Jedis jedis = pool.getResource();
        List<String> value = jedis.lrange(listname, 0, jedis.strlen(listname));
        recoverJedis(jedis);
        return value;
    }

    public float getMemoryUsed(){
        Jedis jedis = pool.getResource();
        System.out.println(jedis.info(Const.MEMORY));
        String [] line = jedis.info(Const.MEMORY).split("\n");
        String used_memory = line[1].split(":")[1];
        System.out.println("used_memory : "+used_memory);
        recoverJedis(jedis);
        return Float.parseFloat(used_memory)/1024;
    }

    public void incr(String key){
        Jedis jedis = pool.getResource();
        jedis.incr(key);
        recoverJedis(jedis);
    }

    public void clear(String key){
        Jedis jedis = pool.getResource();
        jedis.del(key);
        recoverJedis(jedis);
    }

    public void clear(){
        Jedis jedis = pool.getResource();
        jedis.flushAll();
        recoverJedis(jedis);
    }
}
