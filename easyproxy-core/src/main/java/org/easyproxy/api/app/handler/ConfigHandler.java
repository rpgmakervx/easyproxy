package org.easyproxy.api.app.handler;

import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;
import org.easyproxy.api.app.pojo.ConfigVO;
import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigFactory;

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
        System.out.println("data:"+data);
        ConfigVO vo = Json.parse(data,ConfigVO.class);
        System.out.println("vo:"+vo);
        config.buildConfig(vo.convert());
//        resetHandler(config);
        response.json(new Json("message","config complete","code",200));
    }

}
