package org.easyproxy.handler.http;/**
 * Description : CSSFilterHandler
 * Created by YangZH on 16-5-26
 *  下午5:01
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.easyproxy.cache.Cache;
import org.easyproxy.client.ProxyClient;
import org.easyproxy.selector.manager.HostManager;
import org.easyproxy.util.Config;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import static org.easyproxy.constants.Const.*;
/**
 * Description : CSSFilterHandler
 * Created by YangZH on 16-5-26
 * 下午5:01
 */

public class GetRequestHandler extends ChannelInboundHandlerAdapter {
    private InetSocketAddress address;
    private Cache cache = new Cache();
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * 每次请求都重新获取一次地址
     */
    public void fetchInetAddress() {
        this.address = Config.roundRobin();

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
        ctx.channel().flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("CSS 解析异常");
        cause.printStackTrace();
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

    private void response(ChannelHandlerContext ctx, byte[] contents) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, byteBuf);
        ctx.channel().writeAndFlush(response);
//        ctx.close();
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
            String context = "";
            byte[] bytes = null;
            CloseableHttpResponse response = null;
            HttpRequest request = (HttpRequest) msg;
            boolean isGet = request.method().equals(HttpMethod.GET);
            boolean isJSON = APP_JSON.equals(request.headers().get(CONTENTTYPE));
            System.out.println("uri --> " + request.uri());
            try {
                if (isGet) {
//                    fetchInetAddress();
                    InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
                    String ip = addr.getHostString();
                    address = HostManager.getHosts(ip);
                    ProxyClient client = new ProxyClient(address, ROOT.equals(request.uri()) ? "" : request.uri());
                    if (isJSON) {
                        System.out.println("GET 业务请求");
                        //redis缓存
                        String cacheStr = cache.get(request.uri(), "");
                        if (cacheStr == null || cacheStr.isEmpty()) {
                            System.out.println("未命中,走一次网络io并缓存");
                            response = client.makeResponse(request.headers());
                            context = client.getResponse(response);
                            cache.save(request.uri(), "", context);
                            bytes = context.getBytes();
                            response(ctx, bytes, response.getAllHeaders());
                        } else {
                            System.out.println("缓存命中,直接从缓存获取");
                            response(ctx, cacheStr.getBytes());
                        }
                    } else {
                        response = client.makeResponse(request.headers());
                        Pattern pattern = Pattern.compile(APP_JSON + ".*");
                        boolean isjson = false;
                        String respnseType = "no type";
                        if (response.getHeaders(CONTENTTYPE).length != 0) {
                            respnseType = response.getHeaders(CONTENTTYPE)[0].getValue();
                            isjson = pattern.matcher(respnseType).matches();
                        }
                        System.out.println(isjson + "非json请求的响应类型:" + respnseType);
                        //非图片的 text/html请求,返回值是json
                        if (isjson) {
                            System.out.println("request非json的请求");
                            String cacheStr = cache.get(request.uri(), "");
                            if (cacheStr == null || cacheStr.isEmpty()) {
                                System.out.println("未命中,走一次网络io并缓存");
                                response = client.makeResponse(request.headers());
                                context = client.getResponse(response);
                                cache.save(request.uri(), "", context);
                                bytes = context.getBytes();
                                response(ctx, bytes, response.getAllHeaders());
                            } else {
                                System.out.println("缓存命中,直接从缓存获取");
                                response(ctx, cacheStr.getBytes());
                            }
                        }
                        bytes = client.getByteResponse(response);
                        //CDN缓存
                        response(ctx, bytes, response.getAllHeaders());
                    }
                } else {
                    System.out.println("非GET请求或JSON类型  " + request.uri());
                    ctx.fireChannelRead(request);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
