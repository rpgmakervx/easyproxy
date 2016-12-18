package org.easyproxy.startup;/**
 * Description : 
 * Created by YangZH on 16-5-25
 *  下午2:28
 */

import org.easyproxy.config.ConfigEnum;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.init.AppInit;
import org.easyproxy.server.ProxyServer;

import java.io.IOException;

/**
 * Description :
 * Created by YangZH on 16-5-25
 * 下午2:28
 */

public class EasyProxy {


    public static void main(final String[] args) throws IOException {
        String config = "";
        if (args.length>0){
            config = args[0];
        }
        ConfigFactory.setConfigDir(config);
        //初始化配置信息
        AppInit init = new AppInit();
        init.initial();
        System.out.println("easyproxy.home --> "+System.getProperty("easyproxy.home"));
        System.out.println("config path-->"+ConfigFactory.getConfig().getConfigPath());
        ProxyServer server = new ProxyServer();
        System.out.println("负载均衡策略:"+ ConfigFactory.getConfig().getString(ConfigEnum.LB_STRATEGY.key));
        server.startup();
    }

}
