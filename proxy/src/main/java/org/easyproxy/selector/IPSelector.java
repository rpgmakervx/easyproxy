package org.easyproxy.selector;/**
 * Description : 
 * Created by YangZH on 16-8-17
 *  下午10:44
 */

import org.easyproxy.config.ConfigEnum;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.config.PropertyConfig;

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
        String lb_strategy = "";
        if (ConfigFactory.getConfig() instanceof PropertyConfig){
            lb_strategy = ConfigFactory.getConfig().getString(ConfigEnum.LB_STRATEGY.key);
        }else{
            lb_strategy = ConfigFactory.getConfig().getString(LB_STRATEGY);
        }
        switch (lb_strategy){
            case ROUNDROBIN:
                return ConfigFactory.getConfig().roundRobin();
            case WEIGHT_ROUNDROBIN:
                return ConfigFactory.getConfig().weight();
            case IP_HASH:
                return ConfigFactory.getConfig().ip_hash(ip);
            case LESS_CONNECT:
                return ConfigFactory.getConfig().least_connect();
            default:
                return ConfigFactory.getConfig().roundRobin();
        }
    }
}
