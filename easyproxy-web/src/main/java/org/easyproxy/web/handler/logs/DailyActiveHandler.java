package org.easyproxy.web.handler.logs;

import org.easyarch.netpet.asynclient.client.AsyncHttpClient;
import org.easyarch.netpet.asynclient.handler.callback.AsyncResponseHandlerAdapter;
import org.easyarch.netpet.asynclient.http.response.AsyncHttpResponse;
import org.easyarch.netpet.web.context.HandlerContext;
import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;

/**
 * Created by xingtianyu on 17-5-5
 * 下午9:39
 * description:
 */

public class DailyActiveHandler implements HttpHandler {

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        HandlerContext context = request.getContext();
        String host = String.valueOf(context.globalConfig("remoteAddress"));
        AsyncHttpClient client = new AsyncHttpClient(host);
        client.get("/dailyActive", new AsyncResponseHandlerAdapter() {
            @Override
            public void onSuccess(AsyncHttpResponse asyncHttpResponse) throws Exception {
                Json json = asyncHttpResponse.getJson();
                json.put("code",200);
                json.put("message","success");
                response.json(json);
            }

            @Override
            public void onFailure(int statusCode, Object message) throws Exception {
                Json json = new Json("code",statusCode,"message","success");
                response.json(json);
            }
        });
    }
}
