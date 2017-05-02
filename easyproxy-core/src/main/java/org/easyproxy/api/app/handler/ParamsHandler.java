package org.easyproxy.api.app.handler;

import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;
import org.easyproxy.api.app.pojo.ConfigVO;
import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.pojo.ConfigEntity;

/**
 * Created by xingtianyu on 17-4-18
 * 下午6:14
 * description:
 */

public class ParamsHandler implements HttpHandler {

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        Config config = ConfigFactory.getConfig();
        ConfigEntity entity = config.getConfigEntity();
        ConfigVO vo = new ConfigVO();
        vo.convert(entity);
        Json json = Json.parse(vo);
        System.out.println("current config json:\n"+json);
        response.json(json);
    }
}
