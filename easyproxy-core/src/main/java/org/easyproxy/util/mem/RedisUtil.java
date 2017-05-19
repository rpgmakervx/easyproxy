package org.easyproxy.util.mem;/**
 * Description : 
 * Created by YangZH on 16-6-4
 *  上午10:50
 */

import org.easyproxy.config.ConfigFactory;
import org.easyproxy.constants.Const;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.easyproxy.config.ConfigEnum.CACHE_TTL;


/**
 * Description :
 * Created by YangZH on 16-6-4
 * 上午10:50
 */

public class RedisUtil implements MemoryUtil{

    private static final String AUTH = "code4jcentos6xty";

//    public Jedis jedisClient;
    private JedisPool pool = null;
    public RedisUtil(){
        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。.
        config.setMaxIdle(100);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(1000 * 30);
        config.setMaxTotal(1000);
        pool = new JedisPool(config, "127.0.0.1", 6379);
    }

    private Jedis getJedis(){
        Jedis jedis = pool.getResource();
        jedis.auth(AUTH);
        return jedis;
    }

    public void recoverJedis(Jedis jedis){
//        System.out.println("jedis 回收");
        pool.returnResourceObject(jedis);
    }

    public boolean exists(String key){
        Jedis jedis = getJedis();
        boolean exists = jedis.exists(key);
        recoverJedis(jedis);
        return exists;
    }

    public String get(String key) {
//        System.out.println("jedis get("+key+") ");
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        recoverJedis(jedis);
        return value;
    }

    public String hget(String key,String field){
        Jedis jedis = getJedis();
        String value = jedis.hget(key, field);
        recoverJedis(jedis);
        return value;
    }

    public Map<String,String> hgetAll(String key){
        Jedis jedis = getJedis();
        Map<String,String> value = jedis.hgetAll(key);
        recoverJedis(jedis);
        return value;
    }

    public Set<String> keys(String pattern){
        Jedis jedis = getJedis();
        Set<String> value = jedis.keys(pattern);
        recoverJedis(jedis);
        return value;
    }


    public void set(String key, String value,boolean expire) {
        Jedis jedis = getJedis();
        jedis.set(key, value);
        if (expire){
            int ttl = ConfigFactory.getConfig().getInt(CACHE_TTL.key);
            if (ttl < -1)
                ttl = -1;
            jedis.expire(key, ttl);
        }
        recoverJedis(jedis);
    }
    public void set(String key, String value) {
        set(key,value,true);
    }

    public void setList(String listname, List<String> list) {
        Jedis jedis = getJedis();
        for (String item : list) {
            jedis.lpush(listname, item);
        }
        recoverJedis(jedis);
    }

    public List<String> getList(String listname) {
        Jedis jedis = getJedis();
        List<String> value = jedis.lrange(listname, 0, jedis.strlen(listname));
        recoverJedis(jedis);
        return value;
    }


    public void incr(String key){
        Jedis jedis = getJedis();
        jedis.incr(key);
        recoverJedis(jedis);
    }

    public void decr(String key){
        Jedis jedis = getJedis();
        jedis.decr(key);
        recoverJedis(jedis);
    }

    public void clear(String key){
        Jedis jedis = getJedis();
        jedis.del(key);
        recoverJedis(jedis);
    }

    public void clear(){
        Jedis jedis = getJedis();
        jedis.flushAll();
        recoverJedis(jedis);
    }

    public float getMemoryUsed(){
        Jedis jedis = getJedis();
        System.out.println(jedis.info(Const.MEMORY));
        String [] line = jedis.info(Const.MEMORY).split("\n");
        String used_memory = line[1].split(":")[1];
        System.out.println("used_memory : "+used_memory);
        recoverJedis(jedis);
        return Float.parseFloat(used_memory)/1024;
    }
}
