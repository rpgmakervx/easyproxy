package org.easyproxy.api.mvc.action.filter;


import org.easyproxy.api.http.request.impl.HttpHandlerRequest;
import org.easyproxy.api.http.response.impl.HttpHandlerResponse;
import org.easyproxy.api.mvc.action.Action;

/**
 * Created by xingtianyu on 17-3-2
 * 下午5:01
 * description:
 * before返回true会继续执行后续的action,返回false则不执行
 */

public interface HttpFilter extends Action {

    boolean before(HttpHandlerRequest request, HttpHandlerResponse response) throws Exception;
    void after(HttpHandlerRequest request, HttpHandlerResponse response) throws Exception;
}
