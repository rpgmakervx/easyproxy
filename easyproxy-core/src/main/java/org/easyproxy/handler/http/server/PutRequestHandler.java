package org.easyproxy.handler.http.server;/**
 * Description : 
 * Created by YangZH on 16-9-27
 *  上午2:10
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.easyarch.netpet.asynclient.client.AsyncHttpClient;
import org.easyarch.netpet.asynclient.handler.callback.AsyncResponseHandler;
import org.easyarch.netpet.asynclient.http.response.AsyncHttpResponse;
import org.easyproxy.cache.DefaultCache;
import org.easyproxy.cache.redis.RedisCache;
import org.easyproxy.selector.IPSelector;

import java.net.InetSocketAddress;

/**
 * Description :
 * Created by YangZH on 16-9-27
 * 上午2:10
 */

public class PutRequestHandler extends ChannelInboundHandlerAdapter {
    private InetSocketAddress address;
    private DefaultCache cache = new RedisCache();

    public void allocAddress(String ip) {
        IPSelector selector = new IPSelector(ip);
        this.address = selector.select();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageReceived(ctx, msg);
        complete();
    }

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        if (!request.method().equals(HttpMethod.PUT)) {
            System.out.println("不是put请求");
            ctx.fireChannelRead(msg);
            return;
        }
        InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = addr.getHostString();
        allocAddress(ip);
        AsyncHttpClient client = new AsyncHttpClient("http",address);
        client.send(request, new AsyncResponseHandler() {
            @Override
            public void onSuccess(AsyncHttpResponse asyncHttpResponse) {
                ctx.writeAndFlush(asyncHttpResponse.getResponse());
            }

            @Override
            public void onFailure(int i, Object o) {

            }

            @Override
            public void onFinally(AsyncHttpResponse asyncHttpResponse) {
                ctx.writeAndFlush(asyncHttpResponse.getResponse());
            }
        });
    }

    public void complete(){
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
