package org.easyproxy.api.app.handler;

import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;
import org.easyproxy.api.app.pojo.ConfigVO;
import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigEnum;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.handler.http.server.AccessLogHandler;
import org.easyproxy.handler.http.server.BaseServerChildHandler;
import org.easyproxy.handler.http.server.IPFilterHandler;

import java.util.List;
import java.util.Set;

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

    private void resetHandler(Config config){
        boolean ipFilterOpen = config.getBoolean(ConfigEnum.FIREWALL_OPEN.key);
        boolean logOpen = config.getBoolean(ConfigEnum.LOG_OPEN.key);
        List<String> names = BaseServerChildHandler.PIPELINE.names();
        System.out.println("handler names:"+names);
        if (names.contains(BaseServerChildHandler.FILTERHANDLER)){
            System.out.println("-------------filter handler removed");
            BaseServerChildHandler.PIPELINE.remove(BaseServerChildHandler.FILTERHANDLER);
        }
        if (names.contains(BaseServerChildHandler.LOGHANDLER)){
            BaseServerChildHandler.PIPELINE.remove(BaseServerChildHandler.LOGHANDLER);
        }
        if (ipFilterOpen){
            Set<String> forbiddenHosts = ConfigFactory.getConfig().getForbiddenHosts();
            System.out.println("-------------new forbidden list:"+forbiddenHosts);
            BaseServerChildHandler.PIPELINE.addAfter(
                    BaseServerChildHandler.AGGREGATOR,
                    BaseServerChildHandler.FILTERHANDLER,
                    new IPFilterHandler(forbiddenHosts));
            if (logOpen){
                BaseServerChildHandler.PIPELINE.addAfter(
                        BaseServerChildHandler.FILTERHANDLER,
                        BaseServerChildHandler.LOGHANDLER,
                        new AccessLogHandler());
            }
        }else{
            if (logOpen){
                BaseServerChildHandler.PIPELINE.addAfter(
                        BaseServerChildHandler.AGGREGATOR,
                        BaseServerChildHandler.LOGHANDLER,
                        new AccessLogHandler());
            }
        }

    }
}
