package org.easyproxy.web.handler;

import org.easyarch.netpet.asynclient.AsyncHttpClient;
import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;

/**
 * Created by xingtianyu on 17-4-2
 * 下午2:24
 * description:
 */

public class HostHandler implements HttpHandler {

    private String ip;

    public HostHandler(String remoteIp){
        this.ip = remoteIp;
    }

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        AsyncHttpClient client = new AsyncHttpClient(ip);
        byte[] bytes = client.getc();
        System.out.println("json data:"+new String(bytes));
        response.json(new String(bytes));

    }
}
