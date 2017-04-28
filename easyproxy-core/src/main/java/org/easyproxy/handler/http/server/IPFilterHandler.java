package org.easyproxy.handler.http.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import org.easyproxy.resources.Resource;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Collection;

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

    public IPFilterHandler(Collection<String> col){
        IpSubnetFilterRule[] rules = new IpSubnetFilterRule[col.size()];
        int index = 0;
        for (String host:col) {
            rules[index] = new IpSubnetFilterRule(host, 32, IpFilterRuleType.REJECT);
            index++;
        }
        this.rules = rules;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        if(this.handleForbidden(ctx)) {
            ctx.fireChannelRead(msg);
            return ;
        }
        response(ctx, Resource.getResource(CODE_FORBIDDEN));
    }

    private boolean handleForbidden(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        System.out.println("firewall access adress:"+remoteAddress);
        if(remoteAddress == null) {
            return false;
        } else {
            return this.accept(remoteAddress);
        }
    }

    protected boolean accept(InetSocketAddress remoteAddress) throws Exception {
        IpFilterRule[] arr = this.rules;
        int len = arr.length;
        System.out.println("ip filter arr:"+arr.length);
        for(int index = 0; index < len; index++) {
            System.out.println("filted ip is:"+arr[index]);
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
