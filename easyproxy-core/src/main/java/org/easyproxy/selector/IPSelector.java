package org.easyproxy.selector;/**
 * Description : 
 * Created by YangZH on 16-8-17
 *  下午10:44
 */

import org.easyproxy.config.ConfigFactory;
import org.easyproxy.config.PropertyConfig;
import org.easyproxy.constants.Const;

import java.net.InetSocketAddress;

import static org.easyproxy.config.ConfigEnum.LB_STRATEGY;
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
            lb_strategy = ConfigFactory.getConfig().getString(LB_STRATEGY.key);
        }else{
            lb_strategy = ConfigFactory.getConfig().getString(LB_STRATEGY.key);
        }
        switch (lb_strategy){
            case Const.ROUNDROBIN:
                return ConfigFactory.getConfig().roundRobin();
            case WEIGHT_ROUNDROBIN:
                return ConfigFactory.getConfig().weight();
            case IP_HASH:
                return ConfigFactory.getConfig().ipHash(ip);
            case LEAST_CONNECT:
                return ConfigFactory.getConfig().leastConnect();
            default:
                return ConfigFactory.getConfig().roundRobin();
        }
    }
}
