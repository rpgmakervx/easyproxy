package org.easyproxy.cache;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午10:47
 */

import org.easyproxy.pojo.AccessRecord;
import org.easyproxy.util.JedisUtil;

import java.util.List;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:47
 */

public abstract class Cache {

    private JedisUtil util;
    public Cache(){
        util = new JedisUtil();
    }

    public abstract void save(String url, String param, String data);

    public abstract String get(String url, String param);

    public abstract void addAccessRecord(String host);

    public abstract void incrAccessRecord(String host);
    public abstract void decrAccessRecord(String host);

    public abstract int getAccessRecord(String host);

    public abstract List<AccessRecord> getAllAccessRecord();


}
