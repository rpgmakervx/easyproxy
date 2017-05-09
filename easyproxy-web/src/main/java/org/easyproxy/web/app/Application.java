package org.easyproxy.web.app;

import org.easyarch.netpet.web.server.App;
import org.easyproxy.web.handler.IndexPageHandler;
import org.easyproxy.web.handler.config.*;
import org.easyproxy.web.handler.logs.DailyActiveHandler;
import org.easyproxy.web.handler.logs.LogsPageHandler;

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
        app.get("/index",new IndexPageHandler())
                .get("/config",new ConfigPageHandler())
                .get("/hosts",new HostHandler())
                .get("/accesslog",new LogsPageHandler())
                .get("/firewall",new FireWallHandler())
                .get("/config/params",new GetConfigHandler())
                .get("/dailyActive",new DailyActiveHandler())
                .post("/config",new ConfigParamHandler())
                .start(9000);
//        AsyncHttpClient client = new AsyncHttpClient("http://localhost:8080/config");
//        String json = Json.stringify(new Json("code",200));
//        client.post(json.getBytes());
    }
}
