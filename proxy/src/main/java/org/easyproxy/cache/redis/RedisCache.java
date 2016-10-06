package org.easyproxy.cache.redis;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午10:47
 */

import org.easyproxy.cache.DefaultCache;
import org.easyproxy.util.mem.JedisUtil;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:47
 */

public class RedisCache extends DefaultCache {

    public RedisCache(){
        super(new JedisUtil());
    }

}
