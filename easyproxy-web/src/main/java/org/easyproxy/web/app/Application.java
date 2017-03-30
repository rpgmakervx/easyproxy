package org.easyproxy.web.app;

import org.easyarch.netcat.web.context.HandlerContext;
import org.easyarch.netcat.web.server.App;
import org.easyproxy.web.handler.IndexHandler;

/**
 * Created by xingtianyu on 17-3-27
 * 下午2:36
 * description:
 */

public class Application {

    public static void main(String[] args) {
        System.out.println("resource:"+HandlerContext.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        App app = new App();
//        app.config().webView("/home/code4j/dumps").notFound("notfound2");
        app.get("/index",new IndexHandler())
                .start(9000);
    }
}
