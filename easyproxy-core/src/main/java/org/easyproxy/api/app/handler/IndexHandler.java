package org.easyproxy.api.app.handler;

import org.easyproxy.api.http.request.HandlerRequest;
import org.easyproxy.api.http.response.HandlerResponse;
import org.easyproxy.api.mvc.action.handler.HttpHandler;

/**
 * Created by xingtianyu on 17-3-26
 * 下午6:49
 * description:
 */

public class IndexHandler implements HttpHandler {

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        response.html("index");
    }
}
