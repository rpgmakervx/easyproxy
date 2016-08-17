package org.easyproxy.handler.http;/**
 * Description : SocksServerHandler
 * Created by YangZH on 16-5-25
 *  上午9:18
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.easyproxy.client.ProxyClient;
import org.easyproxy.constants.Const;
import org.easyproxy.manager.HostManager;
import org.easyproxy.util.Config;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Description : SocksServerHandler
 * Created by YangZH on 16-5-25
 * 上午9:18
 */

public class AntiLeechHandler extends ChannelInboundHandlerAdapter {

    private InetSocketAddress address;
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * 每次请求都重新获取一次地址
     */
    public void fetchInetAddress() {
        this.address = Config.roundRobin();
        System.out.println("新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageReceived(ctx, msg);
    }

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        threadPool.submit(new Task(ctx, msg));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void response(ChannelHandlerContext ctx, byte[] contents, Header[] headers) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, byteBuf);
//        System.out.println("response header ---------------");
//        for (Header header : headers) {
//            response.headers().set(header.getName(), header.getValue());
//            System.out.println(header.getName() + "::" + header.getValue());
//        }
//        System.out.println("end header ---------------");
        ctx.channel().writeAndFlush(response);
//        ctx.close();
    }

    private void response(ChannelHandlerContext ctx, byte[] contents, HttpResponseStatus status) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status, byteBuf);
        ctx.channel().writeAndFlush(response);
//        ctx.close();
    }

    /**
     * 根据请球头的referer校验是否是盗链者
     *
     * @param headers
     * @return
     * @throws Exception
     */
    public boolean antiLeechCheckUp(HttpHeaders headers) throws Exception {
        String referername = headers.get(HttpHeaderNames.REFERER);
        System.out.println("refer --> " + referername + "  localhost --> " + Config.getString(Const.LOCALHOST));
        String localhost = Config.getString(Const.LOCALHOST);
        Pattern pattern = Pattern.compile(".*" + localhost + ".*");
        return referername != null && pattern.matcher(referername).matches();

    }

    class Task implements Runnable {

        Object msg;
        ChannelHandlerContext ctx;

        public Task(ChannelHandlerContext ctx, Object msg) {
            this.msg = msg;
            this.ctx = ctx;
        }

        @Override
        public void run() {
            HttpRequest request = (HttpRequest) msg;
            try {
                if (request.method().equals(HttpMethod.GET)) {
                    Pattern pattern = Pattern.compile(".+\\.(" + Const.IMAGE + ").*");
                    byte[] bytes = null;
                    CloseableHttpResponse response = null;
                    HttpHeaders headers = request.headers();
                    //读取图片
                    if (pattern.matcher(request.uri()).matches()) {
                        //防盗链
                        if (!antiLeechCheckUp(headers)) {
                            response(ctx, "access deny!".getBytes(), HttpResponseStatus.FORBIDDEN);
                            return;
                        }
//                        fetchInetAddress();

                        InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
                        String ip = addr.getHostString();
                        address = HostManager.getHosts(ip);

                        ProxyClient client = new ProxyClient(address, Const.ROOT.equals(request.uri()) ? "" : request.uri());
                        //在这里强转类型，如果使用了聚合器，就会被阻塞
                        System.out.println("读取到图片 " + request.uri());
                        response = client.makeResponse(headers);
                        bytes = client.getByteResponse(response);
                        response(ctx, bytes, response.getAllHeaders());
                    } else {
                        ctx.fireChannelRead(request);
                    }
                } else {
                    ctx.fireChannelRead(request);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
