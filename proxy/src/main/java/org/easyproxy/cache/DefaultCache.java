package org.easyproxy.cache;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午10:47
 */

import org.easyproxy.constants.Const;
import org.easyproxy.pojo.AccessRecord;
import org.easyproxy.util.codec.EncryptUtil;
import org.easyproxy.util.mem.MemoryUtil;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:47
 */

public class DefaultCache {

    private MemoryUtil util;
    public DefaultCache(MemoryUtil util){
       this.util = util;
    }

    public void save(String url, String param, String data) {
        String key = EncryptUtil.hash(url + param, EncryptUtil.SHA1);
        util.set(key, data);
        System.out.println("保存完，key=" + key );
    }
    public String get(String url, String param) {
        String key = EncryptUtil.hash(url + param,EncryptUtil.SHA1);
        return util.get(key);
    }
    public void addAccessRecord(String host){
        if (util.exists(host))
            return;
        util.set(host, String.valueOf(1),false);
    }
    public void incrAccessRecord(String host){
        util.incr(host);
    }
    public void decrAccessRecord(String host){
        util.decr(host);
    }
    public int getAccessRecord(String host){
        return Integer.parseInt(util.get(host));
    }
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
