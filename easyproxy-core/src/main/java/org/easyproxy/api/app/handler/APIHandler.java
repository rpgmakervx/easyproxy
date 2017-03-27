package org.easyproxy.api.app.handler;

import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.api.http.request.HandlerRequest;
import org.easyproxy.api.http.response.HandlerResponse;
import org.easyproxy.api.mvc.action.handler.HttpHandler;
import org.easyproxy.api.mvc.entity.Json;

/**
 * Created by xingtianyu on 17-3-26
 * 下午6:07
 * description:
 */

public class APIHandler implements HttpHandler {

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        Config config = ConfigFactory.getConfig();
        config.setLBStrategy("iphash");
        response.json(new Json("strategy","iphash"));
    }
}
