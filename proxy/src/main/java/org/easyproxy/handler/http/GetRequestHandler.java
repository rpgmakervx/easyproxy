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
import org.easyproxy.cache.redis.RedisCache;
import org.easyproxy.client.ProxyClient;
import org.easyproxy.selector.IPSelector;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static org.easyproxy.constants.Const.*;

/**
 * Description : CSSFilterHandler
 * Created by YangZH on 16-5-26
 * 下午5:01
 */

public class GetRequestHandler extends ChannelInboundHandlerAdapter {
    private InetSocketAddress address;
    private Cache cache = new RedisCache();
//    private ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * 每次请求都重新获取一次地址
     */
    public void chooseAddress(String ip) {
        IPSelector selector = new IPSelector(ip);
        this.address = selector.select();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageReceived(ctx, msg);
    }

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
//        threadPool.submit(new Task(ctx, msg));
        String context = "";
        byte[] bytes = null;
        CloseableHttpResponse response = null;
        HttpRequest request = (HttpRequest) msg;
        boolean isGet = request.method().equals(HttpMethod.GET);
        boolean isJSON = APP_JSON.equals(request.headers().get(CONTENTTYPE));
        try {
            if (isGet) {
                InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = addr.getHostString();
                chooseAddress(ip);
                accessRecord(address.getHostString(),address.getPort(),true);
                ProxyClient client = new ProxyClient(address, ROOT.equals(request.uri()) ? "" : request.uri());
                String cacheStr = cache.get(request.uri(), "");
                if (cacheStr != null && !cacheStr.isEmpty()) {
                    response(ctx, cacheStr.getBytes());
                    return ;
                }
                response = client.makeResponse(request.headers());
                if (isJSON) {
                    //redis缓存
                    context = client.getResponse(response);
                    cache.save(request.uri(), "", context);
                    bytes = context.getBytes();
                    response(ctx, bytes, response.getAllHeaders());
                } else {
                    Pattern pattern = Pattern.compile(APP_JSON + ".*");
                    boolean isjson = false;
                    String respnseType = "no type";
                    if (response.getHeaders(CONTENTTYPE).length != 0) {
                        respnseType = response.getHeaders(CONTENTTYPE)[0].getValue();
                        isjson = pattern.matcher(respnseType).matches();
                    }
                    //非图片的 text/html请求,返回值是json
                    if (isjson) {
                        response = client.makeResponse(request.headers());
                        context = client.getResponse(response);
                        cache.save(request.uri(), "", context);
                        bytes = context.getBytes();
                        response(ctx, bytes, response.getAllHeaders());
                    }
                    bytes = client.getByteResponse(response);
                    //CDN缓存
                    response(ctx, bytes, response.getAllHeaders());
                }
            } else {
//                    System.out.println("非GET请求或JSON类型  " + request.uri());
                ctx.fireChannelRead(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        complete();
    }

    public void complete(){
        accessRecord(address.getHostString(),address.getPort(),false);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    private void response(ChannelHandlerContext ctx, byte[] contents, Header[] headers) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, byteBuf);
        for (Header header : headers) {
            response.headers().set(header.getName(), header.getValue());
        }
        ctx.channel().writeAndFlush(response);
        ctx.close();
    }

    private void response(ChannelHandlerContext ctx, byte[] contents) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, byteBuf);
        ctx.channel().writeAndFlush(response);
        ctx.close();
    }

    private void accessRecord(String realserver,int port,boolean incr){
        if (incr)
            cache.incrAccessRecord(realserver+":"+port+ACCESSRECORD);
        else cache.decrAccessRecord(realserver+":"+port+ACCESSRECORD);
    }


}
