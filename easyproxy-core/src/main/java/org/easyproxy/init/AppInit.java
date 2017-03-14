package org.easyproxy.init;

import org.easyproxy.cache.CacheManager;
import org.easyproxy.config.ConfigFactory;

import java.io.File;

/**
 * Description :
 * Created by xingtianyu on 16-12-18
 * 下午3:52
 * 用来初始化配置信息等等
 */

public class AppInit {

    public void initial(){
        String home = System.getProperty("easyproxy.home");
        if (home == null){
            System.setProperty("easyproxy.home",System.getProperty("user.dir")+ File.separator);
        }
        ConfigFactory.init();
        CacheManager.init();
    }
}
