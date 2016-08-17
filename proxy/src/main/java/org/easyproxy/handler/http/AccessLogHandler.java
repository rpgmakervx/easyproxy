package org.easyproxy.handler.http;/**
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
import org.easyproxy.cache.Cache;
import org.easyproxy.manager.HostManager;
import org.easyproxy.util.Config;
import org.easyproxy.log.LogUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description :
 * Created by YangZH on 16-8-16
 * 下午2:19
 */

public class AccessLogHandler extends ChannelInboundHandlerAdapter {
    private InetSocketAddress address;
    private Cache cache = new Cache();
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private LogUtil logger = new LogUtil();
    public void fetchInetAddress() {
        this.address = Config.roundRobin();
        System.out.println("新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
    }
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        threadPool.submit(new Task(ctx, msg));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageReceived(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private class Task implements Runnable {
        Object msg;
        ChannelHandlerContext ctx;

        public Task(ChannelHandlerContext ctx, Object msg) {
            this.msg = msg;
            this.ctx = ctx;
        }

        @Override
        public void run() {
            fetchInetAddress();
            InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
            String ip = address.getHostString();
            int port = address.getPort();
            HostManager.setHosts(ip, AccessLogHandler.this.address);
            System.out.println("client ip address: "+ip+" , port is: "+port);
            HttpRequest request = (HttpRequest) msg;
            HttpHeaders headers = request.headers();
            StringBuffer buffer = new StringBuffer();
            String referername = headers.get(HttpHeaderNames.REFERER);
            referername = referername==null?"":referername;
            String userAgent = headers.get(HttpHeaderNames.USER_AGENT);
            HttpContent httpContent = (HttpContent) request;
            ByteBuf content = httpContent.content();
            String message = content.toString(CharsetUtil.UTF_8);
            buffer.append("method:" + request.method().toString()+"\n");
            buffer.append("client-ip:" + ip + "\n");
            buffer.append("path:"+request.uri() + "\n");
            buffer.append("referer:"+referername + "\n");
            buffer.append("User-Agent:"+userAgent + "\n");
            buffer.append("body:"+message + "\n");
            logger.accessLog(buffer.toString() + "\n");
            ctx.fireChannelRead(request);
        }
    }
}
