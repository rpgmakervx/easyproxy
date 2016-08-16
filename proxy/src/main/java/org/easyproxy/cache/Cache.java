package org.easyproxy.cache;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午10:47
 */

import org.easyproxy.util.EncryptUtil;
import org.easyproxy.util.JedisUtil;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:47
 */

public class Cache {

    public void save(String url, String param, String data) {
        long key = EncryptUtil.hash(url + param);
        JedisUtil.set(String.valueOf(key), data);
        System.out.println("保存完，key=" + key + " , value=" + data);
    }

    private boolean isHit(String key) {
        String value = JedisUtil.get(key);
        if (value == null || value.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean isHit(String url, String param) {
        String key = EncryptUtil.hash(url + param, EncryptUtil.SHA1);
        String value = JedisUtil.get(key);
        if (value == null || value.isEmpty()) {
            return false;
        }
        return true;
    }

    public String get(String url, String param) {
        String key = EncryptUtil.hash(url + param, EncryptUtil.SHA1);
        return JedisUtil.get(key);
    }
}
