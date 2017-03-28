package org.easyproxy.api.app.handler;

import org.easyarch.netcat.web.http.request.HandlerRequest;
import org.easyarch.netcat.web.http.response.HandlerResponse;
import org.easyarch.netcat.web.mvc.action.handler.HttpHandler;
import org.easyarch.netcat.web.mvc.entity.Json;
import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.constants.LBStrategy;

/**
 * Created by xingtianyu on 17-3-27
 * 下午11:21
 * description:
 */

public class GetStrategyHandler implements HttpHandler {

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        Config config = ConfigFactory.getConfig();
        LBStrategy strategy = config.getLBStrategy();
        response.json(new Json("strategy",strategy.key));
    }
}
