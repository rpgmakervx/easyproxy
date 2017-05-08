package org.easyproxy.api.app.handler.config;

import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;
import org.easyproxy.api.app.pojo.HostVO;
import org.easyproxy.api.app.pojo.ResponseEntity;
import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigFactory;

import java.util.List;

/**
 * Created by xingtianyu on 17-3-27
 * 下午11:21
 * description:
 */

public class HostHandler implements HttpHandler {

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        Config config = ConfigFactory.getConfig();
        List<HostVO> hosts = config.getLBHosts();
        ResponseEntity<HostVO> entity = new ResponseEntity(hosts.size(),hosts);
        String json = Json.stringify(entity);
        System.out.println("response entity:"+json);
        response.json(json);
    }
}
