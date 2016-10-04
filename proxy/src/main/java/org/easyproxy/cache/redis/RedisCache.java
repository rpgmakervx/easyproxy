package org.easyproxy.cache.redis;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午10:47
 */

import org.easyproxy.cache.Cache;
import org.easyproxy.constants.Const;
import org.easyproxy.pojo.AccessRecord;
import org.easyproxy.util.EncryptUtil;
import org.easyproxy.util.JedisUtil;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:47
 */

public class RedisCache extends Cache{

    private JedisUtil util;
    public RedisCache(){
        util = new JedisUtil();
    }

    @Override
    public void save(String url, String param, String data) {
        String key = EncryptUtil.hash(url + param,EncryptUtil.SHA1);
        util.set(key, data);
        System.out.println("保存完，key=" + key );
    }
    @Override
    public String get(String url, String param) {
        String key = EncryptUtil.hash(url + param,EncryptUtil.SHA1);
        return util.get(key);
    }
    @Override
    public void addAccessRecord(String host){
        if (util.exists(host))
            return;
        util.set(host, String.valueOf(1),false);
    }
    @Override
    public void incrAccessRecord(String host){
        util.incr(host);
    }
    @Override
    public void decrAccessRecord(String host){
        util.decr(host);
    }
    @Override
    public int getAccessRecord(String host){
        return Integer.parseInt(util.get(host));
    }
    @Override
    public List<AccessRecord> getAllAccessRecord(){
        Set<String> keys = util.keys(Const.LIKE+Const.ACCESSRECORD);
        System.out.println("realserver的数量: "+keys.size());
        List<AccessRecord> records = new CopyOnWriteArrayList<AccessRecord>();
        for (String k:keys){
            int num = Integer.parseInt(util.get(k));
            AccessRecord ar = new AccessRecord(k,num);
            records.add(ar);
        }
        return records;
    }


}
