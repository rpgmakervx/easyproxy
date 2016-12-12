package org.easyproxy.handler.http.server;/**
 * Description : 
 * Created by YangZH on 16-9-27
 *  下午1:48
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.easyproxy.constants.Const;
import org.easyproxy.resources.Resource;
import org.easyproxy.config.Config;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import static org.easyproxy.constants.Const.*;

/**
 * Description :
 * Created by YangZH on 16-9-27
 * 下午1:48
 */

public class PersonalPageHandler extends ChannelInboundHandlerAdapter {

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpRequest request = (HttpRequest) msg;
        String uri = request.uri();
        Pattern pattern = Pattern.compile(Config.getString(PERSONAL_URL));
        boolean isProxy = Boolean.valueOf(Config.getString(Const.PROXY_SERVER));
        if (!pattern.matcher(uri).matches()&&isProxy){
            ctx.fireChannelRead(request);
            return;
        }
        if (uri.equals(File.separator)){
            uri = "static/index.html";
        }
        response(ctx, Resource.getPage(RESOURCES+uri));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("APIHandler");
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
