package org.easyproxy.web.app;

import org.easyarch.netpet.web.server.App;
import org.easyproxy.web.handler.ConfigHandler;
import org.easyproxy.web.handler.IndexHandler;

/**
 * Created by xingtianyu on 17-3-27
 * 下午2:36
 * description:
 */

public class Application {

    public static void main(String[] args) {
        App app = new App();
//        app.config().webView("/home/code4j/dumps").notFound("notfound2");
        app.get("/index",new IndexHandler())
                .get("/config",new ConfigHandler())
                .start(9000);
    }
}
