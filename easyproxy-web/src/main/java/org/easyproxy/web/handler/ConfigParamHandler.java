package org.easyproxy.web.handler;

import org.easyarch.netpet.asynclient.client.AsyncHttpClient;
import org.easyarch.netpet.asynclient.handler.callback.AsyncResponseHandlerAdapter;
import org.easyarch.netpet.asynclient.http.response.AsyncHttpResponse;
import org.easyarch.netpet.web.context.HandlerContext;
import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;

/**
 * Created by xingtianyu on 17-4-3
 * 下午1:10
 * description:
 */

public class ConfigParamHandler implements HttpHandler {

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        Json json = request.getJson();
        HandlerContext context = request.getContext();
        String host = String.valueOf(context.globalConfig("remoteAddress"));
        AsyncHttpClient client = new AsyncHttpClient(host);
        System.out.println("post config json:"+json);
        client.postJson("/config", json, new AsyncResponseHandlerAdapter() {
            @Override
            public void onSuccess(AsyncHttpResponse asyncHttpResponse) {
                response.json(new Json("code",200,"message","success","data",asyncHttpResponse.getJson()));
            }

            @Override
            public void onFailure(int statusCode, Object o) {
                System.out.println("post config fail:"+new String((byte[]) o));
                response.json(new Json("code",statusCode,"message",String.valueOf(o)));
            }
        });

    }
}
