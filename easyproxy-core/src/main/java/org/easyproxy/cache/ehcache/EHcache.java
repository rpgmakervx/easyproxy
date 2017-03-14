package org.easyproxy.cache.ehcache;/**
 * Description : 
 * Created by YangZH on 16-10-4
 *  下午10:33
 */

import org.easyproxy.cache.DefaultCache;
import org.easyproxy.util.mem.EHcacheUtil;

/**
 * Description :
 * Created by YangZH on 16-10-4
 * 下午10:33
 */

public class EHcache extends DefaultCache {

    public EHcache(){
        super(new EHcacheUtil());
    }

}
