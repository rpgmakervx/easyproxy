package org.easyproxy.api.app.handler;

import org.easyarch.netpet.kits.file.FileKits;
import org.easyarch.netpet.web.http.protocol.HttpHeaderValue;
import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyproxy.constants.Const;

/**
 * Created by xingtianyu on 17-4-1
 * 上午11:39
 * description:
 */

public class LogDownLoadHandler implements HttpHandler{

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        byte[] logData = FileKits.readx(Const.ACCESSLOG);
        response.download(logData,"accesslog.log", HttpHeaderValue.TEXT_PLAIN);
    }
}
