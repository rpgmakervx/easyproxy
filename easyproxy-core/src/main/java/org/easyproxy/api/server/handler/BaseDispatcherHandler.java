package org.easyproxy.api.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.easyproxy.api.context.ActionHolder;
import org.easyproxy.api.context.HandlerContext;
import org.easyproxy.api.kits.ByteKits;

/**
 * Created by xingtianyu on 17-3-14
 * 下午6:42
 * description:
 */

public class BaseDispatcherHandler extends ChannelInboundHandlerAdapter {

    protected HandlerContext context;
    protected ActionHolder holder;

    public BaseDispatcherHandler(HandlerContext context, ActionHolder holder) {
        this.context = context;
        this.holder = holder;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        HttpResponseStatus status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
        StringBuffer errorBuffer = new StringBuffer();
        errorBuffer.append("<h1 align='center'>"+status.code()+" "+status.reasonPhrase()+"</h1>");
        errorBuffer.append("<p>"+cause.toString());
        StackTraceElement[] elements = cause.getStackTrace();
        for (StackTraceElement e : elements) {
            errorBuffer.append("<br/><span style='margin-left:50px'>at  ").append(e).append("</span>");
        }
        errorBuffer.append("</p><hr/>");
        errorBuffer.append("<div align='center'><span>netcat/1.0</span></div>");
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,status);
        response = response.copy(ByteKits.toByteBuf(errorBuffer.toString()));
        ctx.channel().writeAndFlush(response);
//        errorHandler = new ErrorHandler(status.code(), status.reasonPhrase());
//        errorHandler.setMessage(errorBuffer.toString());
//        HttpHandlerRequest req = new HttpHandlerRequest(null, new Router(null), context, ctx.channel());
//        HttpHandlerResponse resp = new HttpHandlerResponse(response,context, ctx.channel());
//        errorHandler.handle(req,resp);
    }
}
