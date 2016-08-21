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
import org.easyproxy.constants.Const;
import org.easyproxy.log.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description :
 * Created by YangZH on 16-8-16
 * 下午2:19
 */

public class AccessLogHandler extends ChannelInboundHandlerAdapter {
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Logger logger = new Logger();
//    private Cache cache = new Cache();
//    public void chooseAddress(String ip) {
//        System.out.println("");
//        IPSelector selector = new IPSelector(ip);
//        this.address = selector.select();
//        System.out.println(Thread.currentThread().getName()+" 新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
//    }
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
        buffer.append("client-ip:").append(client_ip).append("\n");
        buffer.append("path:").append(request.uri()).append("\n");
        buffer.append("referer:").append(referername).append("\n");
        buffer.append("User-Agent:").append(userAgent).append("\n");
        buffer.append("body:").append(message).append("\n");
        logger.accessLog(buffer.toString() + "\n");
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
            InetSocketAddress addr = getAddress(ctx);
            String ip = addr.getHostString();
            //选择路由
            //记录真实节点的访问量
//            System.out.println("client ip address: "+ip+" , port is: "+port);
            HttpRequest request = (HttpRequest) msg;
//            generateLog(request,ip);
            ctx.fireChannelRead(request);
        }
    }
}
