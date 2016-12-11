package org.easyproxy.handler.http.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import org.easyproxy.cache.DefaultCache;
import org.easyproxy.cache.redis.RedisCache;
import org.easyproxy.client.future.ResponseFuture;
import org.easyproxy.client.manager.HttpResponseManager;

import java.net.InetSocketAddress;

import static org.easyproxy.constants.Const.ACCESSRECORD;

/**
 * Description :
 * Created by xingtianyu on 16-12-8
 * 上午2:03
 */

public class ProxyHandler extends ChannelInboundHandlerAdapter {
    private DefaultCache cache = new RedisCache();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpResponse response = (FullHttpResponse) msg;
        channelRead0(ctx, response);
        complete((InetSocketAddress)ctx.channel().remoteAddress());
    }

    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
        ResponseFuture<FullHttpResponse> future =
                HttpResponseManager.getAttr(ctx.channel());
        System.out.println("recive channel : "+ctx.channel().id()+"  future is null?"+(future == null));
        if (future == null) {
            future = new ResponseFuture<FullHttpResponse>();
            HttpResponseManager.setAttr(ctx.channel(), future);
        }
        future.set(response);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
//        DefaultFullHttpRequest request =
//                new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
//        HttpHeaders headers = new DefaultHttpHeaders();
//        headers.set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=utf-8");
//        headers.set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
//        request.headers().set(headers);
//        ctx.channel().writeAndFlush(request);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    public void complete(InetSocketAddress address) {
        accessRecord(address.getHostString(), address.getPort(), false);
    }

    private void accessRecord(String realserver, int port, boolean incr) {
        if (incr)
            cache.incrAccessRecord(realserver + ":" + port + ACCESSRECORD);
        else cache.decrAccessRecord(realserver + ":" + port + ACCESSRECORD);
    }
}
