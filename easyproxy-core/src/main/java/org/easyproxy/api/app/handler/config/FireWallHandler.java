package org.easyproxy.api.app.handler.config;

import org.easyarch.netpet.kits.StringKits;
import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;
import org.easyproxy.api.app.pojo.FireWallHost;
import org.easyproxy.api.app.pojo.ResponseEntity;
import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by xingtianyu on 17-4-8
 * 下午4:29
 * description:
 */

public class FireWallHandler implements HttpHandler {
    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        Config config = ConfigFactory.getConfig();
        Set<String> fws = config.getForbiddenHosts();
        List<FireWallHost> hosts = new ArrayList<>();
        for (String ip:fws){
            FireWallHost host = new FireWallHost(StringKits.uuid(),ip);
            hosts.add(host);
        }
        ResponseEntity<FireWallHost> entity = new ResponseEntity<>(hosts.size(),hosts);
        String json = Json.stringify(entity);
        System.out.println("response entity:"+json);
        response.json(json);
    }
}
