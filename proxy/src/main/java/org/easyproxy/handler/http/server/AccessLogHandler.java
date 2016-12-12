package org.easyproxy.handler.http.server;/**
 * Description : 
 * Created by YangZH on 16-8-16
 *  下午2:19
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import org.easyproxy.constants.Const;
import org.easyproxy.log.Logger;
import org.easyproxy.util.time.TimeUtil;

import java.net.InetSocketAddress;

/**
 * Description :
 * Created by code4j on 16-8-16
 * 下午2:19
 */

public class AccessLogHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = new Logger();

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        InetSocketAddress addr = getAddress(ctx);
        String ip = addr.getHostString();
        //选择路由
        //记录真实节点的访问量
        HttpRequest request = (HttpRequest) msg;
        generateLog(request,ip);
        ctx.fireChannelRead(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageReceived(ctx, msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    private InetSocketAddress getAddress(ChannelHandlerContext ctx){
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        return address;
    }
    private void generateLog(HttpRequest request,String client_ip){
        HttpHeaders headers = request.headers();
        StringBuffer buffer = new StringBuffer();
        String referername = headers.get(HttpHeaderNames.REFERER);
        referername = referername==null? Const.NULLVALUE:referername;
        String userAgent = headers.get(HttpHeaderNames.USER_AGENT);
        HttpContent httpContent = (HttpContent) request;
        ByteBuf content = httpContent.content();

        String message = content.toString(CharsetUtil.UTF_8);
        buffer.append("method:").append(request.method().toString()).append("\n");
        buffer.append("visit time:").append(TimeUtil.getNowTime()).append("\n");
        buffer.append("remote-ip:").append(client_ip).append("\n");
        buffer.append("uri:").append(request.uri()).append("\n");
        buffer.append("referer:").append(referername).append("\n");
        buffer.append("user-agent:").append(userAgent).append("\n");
        buffer.append("body:\n").append(message).append("\n");
        logger.accessLog(buffer.toString() + "\n");
    }
}
