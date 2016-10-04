package org.easyproxy.cache.ehcache;/**
 * Description : 
 * Created by YangZH on 16-10-4
 *  下午10:33
 */

import org.easyproxy.cache.Cache;
import org.easyproxy.pojo.AccessRecord;

import java.util.List;

/**
 * Description :
 * Created by YangZH on 16-10-4
 * 下午10:33
 */

public class EHcache extends Cache {
    @Override
    public void save(String url, String param, String data) {

    }

    @Override
    public String get(String url, String param) {
        return null;
    }

    @Override
    public void addAccessRecord(String host) {

    }

    @Override
    public void incrAccessRecord(String host) {

    }

    @Override
    public void decrAccessRecord(String host) {

    }

    @Override
    public int getAccessRecord(String host) {
        return 0;
    }

    @Override
    public List<AccessRecord> getAllAccessRecord() {
        return null;
    }
}
