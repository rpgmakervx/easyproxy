package org.easyproxy.web.app;

import org.easyarch.netcat.web.server.App;
import org.easyproxy.web.handler.IndexHandler;

/**
 * Created by xingtianyu on 17-3-27
 * 下午2:36
 * description:
 */

public class Application {

    public static void main(String[] args) {
        App app = new App();
        app.get("/index",new IndexHandler())
                .start(9000);
    }
}
