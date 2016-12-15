package org.easyproxy.selector;/**
 * Description : 
 * Created by YangZH on 16-8-17
 *  下午10:44
 */

import org.easyproxy.config.XmlConfig;

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
        switch (XmlConfig.getString(LB_STRATEGY)){
            case ROUNDROBIN:
                return XmlConfig.roundRobin();
            case WEIGHT_ROUNDROBIN:
                return XmlConfig.weight();
            case IP_HASH:
                return XmlConfig.ip_hash(ip);
            case LESS_CONNECT:
                return XmlConfig.less_connect();
            default:
                return XmlConfig.roundRobin();
        }
    }
}
