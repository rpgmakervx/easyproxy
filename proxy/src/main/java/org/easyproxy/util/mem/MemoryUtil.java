package org.easyproxy.util.mem;/**
 * Description : 
 * Created by YangZH on 16-10-6
 *  下午3:52
 */

import java.util.Set;

/**
 * Description :
 * Created by YangZH on 16-10-6
 * 下午3:52
 */

public interface MemoryUtil {

    public boolean exists(String key);
    public String get(String key);

    public void set(String key, String value,boolean expire);
    public void set(String key, String value);

    public Set<String> keys(String regx);

    public void incr(String key);

    public void decr(String key);
    public void clear(String key);
    public void clear();
}
