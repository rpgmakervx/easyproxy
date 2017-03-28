package org.easyproxy.web.handler;


import org.easyarch.netcat.asynclient.AsyncHttpClient;
import org.easyarch.netcat.http.request.HandlerRequest;
import org.easyarch.netcat.http.response.HandlerResponse;
import org.easyarch.netcat.mvc.action.handler.HttpHandler;

/**
 * Created by xingtianyu on 17-3-26
 * 下午6:49
 * description:
 */

public class IndexHandler implements HttpHandler {

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        AsyncHttpClient client = new AsyncHttpClient("https://www.baidu.com");
        byte[] content = client.getc();
        response.write(content);
    }
}