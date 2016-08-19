package org.easyproxy.cache;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午10:47
 */

import org.easyproxy.constants.Const;
import org.easyproxy.pojo.AccessRecord;
import org.easyproxy.util.EncryptUtil;
import org.easyproxy.util.JedisUtil;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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

    public void addAccessRecord(String host){
        if (JedisUtil.exists(host))
            return;
        JedisUtil.set(host, String.valueOf(1),false);
    }

    public void incrAccessRecord(String host){
        JedisUtil.incr(host);
    }

    public int getAccessRecord(String host){
        return Integer.parseInt(JedisUtil.get(host));
    }

    public List<AccessRecord> getAllAccessRecord(){
        Set<String> keys = JedisUtil.keys(Const.LIKE+Const.ACCESSRECORD);
        System.out.println("realserver的数量: "+keys.size());
        List<AccessRecord> records = new CopyOnWriteArrayList<AccessRecord>();
//        Map<String,Integer> result = new HashMap<String,Integer>();
        for (String k:keys){
            int num = Integer.parseInt(JedisUtil.get(k));
            AccessRecord ar = new AccessRecord(k,num);
            records.add(ar);
        }
//        AccessRecord maxRecord = Collections.max(records);
        return records;
    }



}
