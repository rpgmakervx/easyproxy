package org.easyproxy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.ArrayUtils;
import org.easyproxy.client.future.ResponseFuture;
import org.easyproxy.client.manager.HttpResponseManager;
import org.easyproxy.handler.http.client.BaseClientChildHandler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Description :
 * Created by xingtianyu on 16-12-8
 * 上午1:35
 */

public class HttpUtils {

    private String ip;
    private int port;
    private EventLoopGroup workerGroup;
    private Bootstrap b;
    private ChannelFuture future;
    private Channel channel;

    public HttpUtils(InetSocketAddress address) {
        try {
            this.ip = address.getHostString();
            this.port = address.getPort();
            workerGroup = new NioEventLoopGroup(32, Executors.newCachedThreadPool());
            b = new Bootstrap();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HttpUtils(String ip, int port) {
        try {
            this.ip = ip;
            this.port = port;
            workerGroup = new NioEventLoopGroup();
            b = new Bootstrap();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(ip, port))
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new BaseClientChildHandler());
    }

    public void connect() {
        try {
            future = b.connect();
            future.sync();
            channel = future.channel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("channel id:" + channel.id());
    }

    public void send(FullHttpRequest request) throws Exception {
        doRequest(request.uri(),request.method()
                ,request.headers(),request.content());
    }

    public void get(String uri, HttpHeaders headers) throws Exception {
        doRequest(uri,HttpMethod.GET,headers,Unpooled.EMPTY_BUFFER);
    }

    public void post(String uri, HttpHeaders headers, byte[] bytes) throws Exception {
        if (ArrayUtils.isEmpty(bytes)){
            bytes = new byte[0];
        }
        doRequest(uri,HttpMethod.POST,headers,Unpooled.wrappedBuffer(bytes));
    }

    public void put(String uri, HttpHeaders headers, byte[] bytes) throws Exception {
        if (ArrayUtils.isEmpty(bytes)){
            bytes = new byte[0];
        }
        doRequest(uri,HttpMethod.PUT,headers,Unpooled.wrappedBuffer(bytes));
    }

    public void delete(String uri, HttpHeaders headers, byte[] bytes) throws Exception {
        if (ArrayUtils.isEmpty(bytes)){
            bytes = new byte[0];
        }
        doRequest(uri,HttpMethod.DELETE,headers,Unpooled.wrappedBuffer(bytes));
    }



    private void doRequest(String uri, HttpMethod method,HttpHeaders headers, ByteBuf buf){
        DefaultFullHttpRequest request =
                new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uri,buf);
        if (headers == null){
            headers = new DefaultHttpHeaders();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
            headers.set(HttpHeaderNames.HOST, channel.localAddress());
        }
        request.headers().set(headers);
        request.headers().set(HttpHeaderNames.USER_AGENT,"java/HttpUtils");
        HttpResponseManager.setAttr(channel,new ResponseFuture<FullHttpResponse>());
        channel.writeAndFlush(request);
    }

    public FullHttpResponse getWholeResponse() throws Exception {
        ResponseFuture<FullHttpResponse> responseFuture =
                HttpResponseManager.getAttr(channel);
        return responseFuture.get();
    }

    public byte[] getContentAsStream(){
        ResponseFuture<FullHttpResponse> responseFuture =
                HttpResponseManager.getAttr(channel);
        try {
            return getContent(responseFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            close();
        }
    }
    public HttpHeaders getHeaders(){
        ResponseFuture<FullHttpResponse> responseFuture =
                HttpResponseManager.getAttr(channel);
        try {
            return getHeaders(responseFuture.get());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String,Object> getHeadersAsMap(){
        ResponseFuture<FullHttpResponse> responseFuture =
                HttpResponseManager.getAttr(channel);
        Map<String,Object> headMap = new HashMap<>();
        try {
            HttpHeaders headers = getHeaders(responseFuture.get());
            for (Map.Entry<String,String> entry : headers.entries()){
                headMap.put(entry.getKey(),entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headMap;
    }

    private void close() {
        workerGroup.shutdownGracefully();
    }

    private byte[] getContent(FullHttpResponse response) {
        ByteBuf buf = response.content();
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return bytes;
    }

    private HttpHeaders getHeaders(FullHttpResponse response){
        return response == null?null:response.headers();
    }
}
