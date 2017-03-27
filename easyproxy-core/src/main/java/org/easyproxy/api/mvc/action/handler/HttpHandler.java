package org.easyproxy.api.mvc.action.handler;


import org.easyproxy.api.http.request.HandlerRequest;
import org.easyproxy.api.http.response.HandlerResponse;
import org.easyproxy.api.mvc.action.Action;

/**
 * Description :
 * Created by xingtianyu on 17-2-27
 * 上午12:11
 * description:
 */

public interface HttpHandler extends Action {

    public void handle(HandlerRequest request, HandlerResponse response) throws Exception;
}
