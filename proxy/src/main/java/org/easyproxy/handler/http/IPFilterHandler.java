package org.easyproxy.handler.http;/**
 * Description : 
 * Created by YangZH on 16-8-22
 *  上午12:56
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;
import org.easyproxy.resources.Resource;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import static org.easyproxy.constants.Const.CODE_FORBIDDEN;
import static org.easyproxy.constants.Const.TEXT_HTML;

/**
 * Description :
 * Created by YangZH on 16-8-22
 * 上午12:56
 */

public class IPFilterHandler extends ChannelInboundHandlerAdapter {

    private final IpFilterRule[] rules;

    public IPFilterHandler(IpFilterRule... rules) {
        if(rules == null||rules.length==0) {
            throw new NullPointerException("rules");
        } else {
            this.rules = rules;
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        HttpRequest request = (HttpRequest) msg;
        System.out.printf("access: "+request.uri());
        if(this.handleForbidden(ctx)) {
            ctx.fireChannelRead(msg);
            return ;
        }
        response(ctx, Resource.getResource(CODE_FORBIDDEN));
    }

    private boolean handleForbidden(ChannelHandlerContext ctx) throws Exception {
        System.out.println("localAddress: "+ctx.channel().localAddress());
        System.out.println("remoteAddress: "+ctx.channel().remoteAddress());
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        if(remoteAddress == null) {
            return false;
        } else {
//            ctx.pipeline().remove(this);
            if(!this.accept(remoteAddress)) {
                return false;
            }
            return true;
        }
    }

    protected boolean accept(InetSocketAddress remoteAddress) throws Exception {
        IpFilterRule[] arr = this.rules;
        int len = arr.length;

        for(int index = 0; index < len; index++) {
            IpFilterRule rule = arr[index];
            if(rule == null) {
                break;
            }
            if(rule.matches(remoteAddress)) {
                return rule.ruleType() == IpFilterRuleType.ACCEPT;
            }
        }
        return true;
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
}
