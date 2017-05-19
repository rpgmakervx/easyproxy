package org.easyproxy.handler.http.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.easyproxy.config.ConfigEnum;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.log.Logger;
import org.easyproxy.resources.Resource;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Set;

import static org.easyproxy.constants.Const.CODE_FORBIDDEN;
import static org.easyproxy.constants.Const.TEXT_HTML;

/**
 * Created by xingtianyu on 17-4-28
 * 下午11:25
 * description:
 */

public class FireWallHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = Logger.getLogger();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean hasIPFilter = ConfigFactory.getConfig().getBoolean(ConfigEnum.FIREWALL_OPEN.key);
        System.out.println("has ip filter:"+hasIPFilter);
        if (hasIPFilter){
            messageReceived(ctx, msg);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        Set<String> forbiddenHosts = ConfigFactory.getConfig().getForbiddenHosts();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String remoteHost = remoteAddress.getHostName();
        String remoteIp = remoteAddress.getHostString();
        if (forbiddenHosts.contains(remoteIp)
                ||forbiddenHosts.contains(remoteHost)){
            response(ctx, Resource.getResource(CODE_FORBIDDEN));
            logger.accessLog(request,getRemoteIp(ctx),403);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private void response(ChannelHandlerContext ctx, byte[] contents) throws UnsupportedEncodingException {
        if (contents==null){
            ctx.close();
        }
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.NOT_FOUND, byteBuf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, TEXT_HTML);
        ctx.channel().writeAndFlush(response);
    }

    private String getRemoteIp(ChannelHandlerContext ctx){
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        return address.getHostString();
    }
}
