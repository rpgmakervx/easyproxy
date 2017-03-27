package org.easyproxy.handler.http.server;/**
 * Description : 
 * Created by YangZH on 16-6-3
 *  下午10:23
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.easyproxy.cache.DefaultCache;
import org.easyproxy.cache.redis.RedisCache;
import org.easyproxy.client.HttpUtils;
import org.easyproxy.selector.IPSelector;

import java.net.InetSocketAddress;

import static org.easyproxy.constants.Const.ACCESSRECORD;

/**
 * Description :
 * Created by YangZH on 16-6-3
 * 下午10:23
 */

public class PostRequestHandler extends ChannelInboundHandlerAdapter {

    private InetSocketAddress address;
    private DefaultCache cache = new RedisCache();

    /**
     * 每次请求都重新获取一次地址
     */
    public void allocAdress(String ip) {
        IPSelector selector = new IPSelector(ip);
        this.address = selector.select();
        System.out.println(Thread.currentThread().getName()+" 新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageReceived(ctx, msg);
    }

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        if (!request.method().equals(HttpMethod.POST)){
            ctx.fireChannelRead(request);
            return;
        }
        InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = addr.getHostString();
        allocAdress(ip);
        HttpUtils client = new HttpUtils(address);
        client.connect();
        client.send(request);
        FullHttpResponse response = client.getWholeResponse();
        ctx.writeAndFlush(response);
        complete();
    }

    public void complete() {
        accessRecord(address.getHostString(), address.getPort(), false);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private void accessRecord(String realserver, int port, boolean incr) {
        if (incr)
            cache.incrAccessRecord(realserver + ":" + port + ACCESSRECORD);
        else cache.decrAccessRecord(realserver + ":" + port + ACCESSRECORD);
    }

}
