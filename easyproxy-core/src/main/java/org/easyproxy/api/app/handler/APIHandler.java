package org.easyproxy.api.app.handler;

import org.easyarch.netcat.http.request.HandlerRequest;
import org.easyarch.netcat.http.response.HandlerResponse;
import org.easyarch.netcat.mvc.action.handler.HttpHandler;
import org.easyarch.netcat.mvc.entity.Json;
import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigFactory;

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
