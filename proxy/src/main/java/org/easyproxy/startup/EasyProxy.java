package org.easyproxy.startup;/**
 * Description : 
 * Created by YangZH on 16-5-25
 *  下午2:28
 */

import org.easyproxy.constants.Const;
import org.easyproxy.server.ProxyServer;
import org.easyproxy.util.Config;

import java.io.File;
import java.io.IOException;

/**
 * Description :
 * Created by YangZH on 16-5-25
 * 下午2:28
 */

public class EasyProxy {


    public static void main(final String[] args) throws IOException {
        String home = System.getProperty("easyproxy.home");
        if (home == null){
            System.setProperty("easyproxy.home",System.getProperty("user.dir")+File.separator);
        }
        System.out.println("easyproxy.home --> "+System.getProperty("easyproxy.home"));
        String config = Const.DEFAULT_CONFIGPATH;
        if (args.length>0){
            config = args[0];
        }
        System.out.println("config path-->"+config);
        ProxyServer server = new ProxyServer(config);
        System.out.println("负载均衡策略:"+ Config.getString(Const.LB_STRATEGY));
        server.startup();
    }

}
