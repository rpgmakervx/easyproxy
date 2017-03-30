package org.easyproxy.api.app.handler;

import org.easyarch.netcat.web.http.request.HandlerRequest;
import org.easyarch.netcat.web.http.response.HandlerResponse;
import org.easyarch.netcat.web.mvc.action.handler.HttpHandler;
import org.easyarch.netcat.web.mvc.entity.Json;
import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.pojo.ConfigEntity;

/**
 * Created by xingtianyu on 17-3-30
 * 下午11:40
 * description:
 */

public class ConfigHandler implements HttpHandler{

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        Config config = ConfigFactory.getConfig();
        String data = Json.stringify(request.getJson());
        config.buildConfig(Json.parse(data,ConfigEntity.class));
        response.json(new Json("message","config complete","code",200));
    }
}
