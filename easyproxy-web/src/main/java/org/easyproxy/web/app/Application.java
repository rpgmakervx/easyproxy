package org.easyproxy.web.app;

import org.easyarch.netpet.web.server.App;
import org.easyproxy.web.handler.ConfigHandler;
import org.easyproxy.web.handler.ConfigHostHandler;
import org.easyproxy.web.handler.HostHandler;
import org.easyproxy.web.handler.IndexHandler;

/**
 * Created by xingtianyu on 17-3-27
 * 下午2:36
 * description:
 */

public class Application {

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.get("/index",new IndexHandler())
                .get("/config",new ConfigHandler())
                .post("/config",new ConfigHostHandler())
                .get("/hosts",new HostHandler("http://127.0.0.1:7000"))
                .start(9000);
//        AsyncHttpClient client = new AsyncHttpClient("http://localhost:8080/config");
//        String json = Json.stringify(new Json("code",200));
//        client.post(json.getBytes());
    }
}
