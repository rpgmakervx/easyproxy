package org.easyproxy.api.app.handler;

import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;
import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigFactory;

/**
 * Created by xingtianyu on 17-3-26
 * 下午6:07
 * description:
 */

public class LBStrategyHandler implements HttpHandler {

    private static final String STRATEGY = "strategy";
    private static final String CODE = "code";
    private static final String MESSAGE = "message";
    private static final String OK = "strategy change complete";
    private static final String ERRORMSG = "load balance strategy not found, go to default strategy:roundrobin";

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        Config config = ConfigFactory.getConfig();
        response.json(new Json(CODE,200,MESSAGE,config.getLBStrategy().key));
    }
}
