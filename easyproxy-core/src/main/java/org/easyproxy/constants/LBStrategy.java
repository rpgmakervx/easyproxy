package org.easyproxy.constants;/**
 * Description : 
 * Created by YangZH on 16-8-23
 *  下午3:00
 */

/**
 * Description :
 * Created by YangZH on 16-8-23
 * 下午3:00
 */

public enum LBStrategy {
    ROUNDROBIN("roundrobin"),
    WEIGHT_ROUNDROBIN("weight_roundrobin"),
    IP_HASH("iphash"),
    LESS_CONNECT("less_connection");

    public String key;

    LBStrategy(String key) {
        this.key = key;
    }

    public static LBStrategy getStrategy(String key){
        for (LBStrategy value:values()){
            if (value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
