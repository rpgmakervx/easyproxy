package org.easyproxy.selector;/**
 * Description : 
 * Created by YangZH on 16-8-17
 *  下午10:44
 */

import org.easyproxy.config.Config;

import java.net.InetSocketAddress;

import static org.easyproxy.constants.Const.*;

/**
 * Description :
 * Created by YangZH on 16-8-17
 * 下午10:44
 */

public class IPSelector {

    private String ip;

    public IPSelector(String ip){
        this.ip = ip;
    }

    public InetSocketAddress select(){
        switch (Config.getString(LB_STRATEGY)){
            case ROUNDROBIN:
                return Config.roundRobin();
            case WEIGHT_ROUNDROBIN:
                return Config.weight();
            case IP_HASH:
                return Config.ip_hash(ip);
            case LESS_CONNECT:
                return Config.less_connect();
            default:
                return Config.roundRobin();
        }
    }
}
