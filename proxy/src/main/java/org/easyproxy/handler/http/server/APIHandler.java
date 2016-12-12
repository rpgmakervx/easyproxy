package org.easyproxy.handler.http.server;/**
 * Description : 
 * Created by YangZH on 16-8-23
 *  下午2:30
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.easyproxy.handler.http.param.ParamGetter;
import org.easyproxy.config.Config;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.regex.Pattern;

import static org.easyproxy.constants.Const.*;

/**
 * Description :
 * Created by YangZH on 16-8-23
 * 下午2:30
 */

public class APIHandler extends ChannelInboundHandlerAdapter {


    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpRequest request = (HttpRequest) msg;
        String uri = request.uri();
        Pattern pattern = Pattern.compile(Config.getString(API_URI));
        if (!pattern.matcher(uri).matches()){
            ctx.fireChannelRead(request);
            return;
        }
        Map<String,Object> map = ParamGetter.getRequestParams(request);
        String strategy = (String) map.get(LB_STRATEGY);
        Config.setLB_Strategy(strategy);
        Config.listAll();
        response(ctx, API_ACK.getBytes());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageReceived(ctx,msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private void response(ChannelHandlerContext ctx, byte[] contents) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, byteBuf);
        ctx.channel().writeAndFlush(response);
    }
}
