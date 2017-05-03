package org.easyproxy.web.app;

import org.easyarch.netpet.web.server.App;
import org.easyproxy.web.handler.*;

/**
 * Created by xingtianyu on 17-3-27
 * 下午2:36
 * description:
 */

public class Application {

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.config()
                .globalConfig("remoteAddress","http://127.0.0.1:9999");
        app.get("/index",new IndexHandler())
                .get("/config",new ConfigHandler())
                .post("/config",new ConfigParamHandler())
                .get("/hosts",new HostHandler())
                .get("/firewall",new FireWallHandler())
                .get("/config/params",new GetConfigHandler())
                .start(9000);
//        AsyncHttpClient client = new AsyncHttpClient("http://localhost:8080/config");
//        String json = Json.stringify(new Json("code",200));
//        client.post(json.getBytes());
    }
}
