package org.easyproxy.selector;/**
 * Description : 
 * Created by YangZH on 16-8-17
 *  下午10:44
 */

import org.easyproxy.config.ConfigFactory;
import org.easyproxy.config.PropertyConfig;
import org.easyproxy.constants.LBStrategy;

import java.net.InetSocketAddress;

import static org.easyproxy.config.ConfigEnum.LB_STRATEGY;


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
        LBStrategy lb_strategy = null;
        if (ConfigFactory.getConfig() instanceof PropertyConfig){
            lb_strategy = LBStrategy.getStrategy(ConfigFactory.getConfig().getString(LB_STRATEGY.key));
        }else{
            lb_strategy = LBStrategy.getStrategy(ConfigFactory.getConfig().getString(LB_STRATEGY.key));
        }
        switch (lb_strategy){
            case ROUNDROBIN:
                return ConfigFactory.getConfig().roundRobin();
            case WEIGHT_ROUNDROBIN:
                return ConfigFactory.getConfig().weight();
            case IP_HASH:
                return ConfigFactory.getConfig().ipHash(ip);
            case LESS_CONNECT:
                return ConfigFactory.getConfig().leastConnect();
            default:
                return ConfigFactory.getConfig().roundRobin();
        }
    }
}
