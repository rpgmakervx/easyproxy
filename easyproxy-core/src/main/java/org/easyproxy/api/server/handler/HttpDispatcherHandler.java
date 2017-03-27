package org.easyproxy.api.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.easyproxy.api.context.ActionHolder;
import org.easyproxy.api.context.HandlerContext;
import org.easyproxy.api.http.protocol.HttpMethod;
import org.easyproxy.api.http.protocol.HttpStatus;
import org.easyproxy.api.http.request.impl.HttpHandlerRequest;
import org.easyproxy.api.http.response.impl.HttpHandlerResponse;
import org.easyproxy.api.mvc.action.Action;
import org.easyproxy.api.mvc.action.ActionType;
import org.easyproxy.api.mvc.action.ActionWrapper;
import org.easyproxy.api.mvc.action.filter.HttpFilter;
import org.easyproxy.api.mvc.action.handler.HttpHandler;
import org.easyproxy.api.mvc.action.handler.impl.ErrorHandler;
import org.easyproxy.api.mvc.action.handler.impl.SessionHandler;
import org.easyproxy.api.mvc.router.Router;

import java.util.List;


/**
 * Description :
 * Created by xingtianyu on 17-2-23
 * 下午4:52
 * description:
 * 1.请求路由
 * 2.判断要执行的处理器
 * 3.执行对应处理器
 */

public class HttpDispatcherHandler extends BaseDispatcherHandler {

    private ErrorHandler errorHandler;

    public HttpDispatcherHandler(HandlerContext context, ActionHolder holder) {
        super(context, holder);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        Router router = new Router(request.uri(),
                ActionType.HANDLER, HttpMethod.getMethod(request.method()));
        ActionWrapper wrapper = holder.getAction(router);
        List<HttpFilter> filters;
        filters = holder.getFilters(router);
        Action action = null;
        //直接从wrapper获取比遍历一遍快
        if (wrapper != null) {
            action = wrapper.getAction();
        }
        HttpHandlerRequest req = new HttpHandlerRequest(request, router, context, ctx.channel());
        HttpHandlerResponse resp = new HttpHandlerResponse(response, context, ctx.channel());

        if (filters == null || filters.isEmpty() && action == null) {
            this.errorHandler = new ErrorHandler(HttpResponseStatus.NOT_FOUND.code(), HttpResponseStatus.NOT_FOUND.reasonPhrase());
            errorHandler.handle(req, resp);
            return;
        }
        if (wrapper.getStatus() == HttpStatus.METHOD_NOT_ALLOWED) {
            this.errorHandler = new ErrorHandler(HttpResponseStatus.METHOD_NOT_ALLOWED.code(), HttpResponseStatus.METHOD_NOT_ALLOWED.reasonPhrase());
            errorHandler.handle(req, resp);
            return;
        }
        SessionHandler sessionHandler = new SessionHandler(ctx);
        HttpHandler handler = (HttpHandler) action;
        if (!filters.isEmpty()) {
            for (HttpFilter filter : filters) {
                if (!filter.before(req, resp)) {
                    return;
                }
            }
        }
        sessionHandler.handle(req,resp);
        handler.handle(req, resp);
        if (!filters.isEmpty()) {
            for (HttpFilter filter : filters) {
                filter.after(req, resp);
            }
        }
    }

}
