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
 * Created by xingtianyu on 17-4-8
 * 下午4:42
 * description:
 */

public class FireWallHandler implements HttpHandler {

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        HandlerContext context = request.getContext();
        String host = String.valueOf(context.globalConfig("remoteAddress"));
        AsyncHttpClient client = new AsyncHttpClient(host);
        client.get("/firewall", new AsyncResponseHandlerAdapter() {
            @Override
            public void onSuccess(AsyncHttpResponse asyncHttpResponse) throws Exception {
                System.out.println("json data:"+asyncHttpResponse.getJson());
                response.json(asyncHttpResponse.getJson());
            }

            @Override
            public void onFailure(int statusCode, Object o) throws Exception {
                System.out.println("fail");
                response.json(new Json("code",statusCode,"message",o));
            }
        });
    }
}
