package org.easyproxy.api.mvc.action.handler.impl;


import org.easyproxy.api.context.HandlerContext;
import org.easyproxy.api.http.request.HandlerRequest;
import org.easyproxy.api.http.response.HandlerResponse;
import org.easyproxy.api.mvc.action.handler.HttpHandler;

import static org.easyproxy.api.http.Const.HTTPSTATUS;
import static org.easyproxy.api.http.Const.MESSAGE;
import static org.easyproxy.api.http.Const.REASONPHASE;

/**
 * Created by xingtianyu on 17-3-9
 * 下午3:51
 * description:
 */

public class ErrorHandler implements HttpHandler {

    private int code;

    private String reasonPhase;

    private String message = "";

    public ErrorHandler(int code, String reasonPhase) {
        this.code = code;
        this.reasonPhase = reasonPhase;
    }

    public void setMessage(String message){
        if (message != null){
            this.message = message;
        }
    }

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        HandlerContext context = new HandlerContext();
        response.addModel(HTTPSTATUS,code);
        response.addModel(REASONPHASE,reasonPhase);
        response.addModel(MESSAGE,message);
        response.html(context.getErrorPage(), code);
    }
}
