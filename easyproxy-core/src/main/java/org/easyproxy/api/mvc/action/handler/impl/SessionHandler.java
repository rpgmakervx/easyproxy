package org.easyproxy.api.mvc.action.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import org.easyproxy.api.context.HandlerContext;
import org.easyproxy.api.http.cookie.HttpCookie;
import org.easyproxy.api.http.request.HandlerRequest;
import org.easyproxy.api.http.response.HandlerResponse;
import org.easyproxy.api.kits.HashKits;
import org.easyproxy.api.mvc.action.handler.HttpHandler;

import java.net.InetSocketAddress;

import static org.easyproxy.api.http.Const.NETCATID;


/**
 * Created by xingtianyu on 17-3-17
 * 上午12:43
 * description:
 */

public class SessionHandler implements HttpHandler {

    private ChannelHandlerContext ctx;

    public SessionHandler(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        String sessionId = HashKits
                .sha1(ctx.channel().id().asLongText());
        if (request.getCookies().isEmpty()){
            createCookie(sessionId,response);
        }
        for (HttpCookie cookie:request.getCookies()){
            if (cookie.name().equals(NETCATID)
                    &&!sessionId.equals(cookie.value())){
                createCookie(sessionId,response);
            }
        }
    }

    private void createCookie(String sessionId, HandlerResponse response){
        HandlerContext context = response.getContext();
        HttpCookie cookie = new HttpCookie(NETCATID,sessionId);
        InetSocketAddress hostAddress =
                (InetSocketAddress) ctx.channel().remoteAddress();
        cookie.setDomain(hostAddress.getHostName());
        cookie.setPath(context.getContextPath());
        response.addCookie(cookie);
    }
}
