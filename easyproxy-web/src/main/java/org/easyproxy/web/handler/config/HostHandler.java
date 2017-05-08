package org.easyproxy.web.handler.config;

import org.easyarch.netpet.asynclient.client.AsyncHttpClient;
import org.easyarch.netpet.asynclient.handler.callback.AsyncResponseHandlerAdapter;
import org.easyarch.netpet.asynclient.http.response.AsyncHttpResponse;
import org.easyarch.netpet.web.context.HandlerContext;
import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;

/**
 * Created by xingtianyu on 17-4-2
 * 下午2:24
 * description:
 */

public class HostHandler implements HttpHandler {


    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        HandlerContext context = request.getContext();
        String ip = String.valueOf(context.globalConfig("remoteAddress"));
        AsyncHttpClient client = new AsyncHttpClient(ip);
        client.get("/hosts", new AsyncResponseHandlerAdapter() {
            @Override
            public void onSuccess(AsyncHttpResponse asyncHttpResponse) {
                System.out.println("json data:"+asyncHttpResponse.getJson());
                response.json(asyncHttpResponse.getJson());
            }

            @Override
            public void onFailure(int statusCode, Object o) {
                System.out.println("fail");
                response.json(new Json("code",statusCode,"message",o));
            }
        });

    }
}
