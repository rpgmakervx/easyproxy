package org.easyproxy.selector;/**
 * Description : 
 * Created by YangZH on 16-8-17
 *  下午10:44
 */

import org.easyproxy.util.Config;

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
            case IP_HASH:
                return Config.ip_hash(ip);
            default:
                return Config.roundRobin();
        }
    }
}
