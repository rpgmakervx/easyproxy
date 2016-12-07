package org.easyproxy.handler.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;
import org.easyproxy.cache.DefaultCache;
import org.easyproxy.cache.redis.RedisCache;
import org.easyproxy.client.ProxyClient;
import org.easyproxy.selector.IPSelector;
import org.easyproxy.util.codec.EncryptUtil;
import org.easyproxy.util.struct.JSONUtil;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easyproxy.constants.Const.*;

/**
 * Description : CSSFilterHandler
 * Created by YangZH on 16-5-26
 * 下午5:01
 */

public class GetRequestHandler extends ChannelInboundHandlerAdapter {
    private InetSocketAddress address;
    private DefaultCache cache = new RedisCache();
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
//        boolean isJSON = APP_JSON.equals(request.headers().get(CONTENT_TYPE));
        try {
            if (isGet) {
                InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = addr.getHostString();
                chooseAddress(ip);
                accessRecord(address.getHostString(), address.getPort(), true);
                ProxyClient client = new ProxyClient(address, ROOT.equals(request.uri()) ? "" : request.uri());
                byte[] data = EncryptUtil.decodeBase64(cache.get(request.uri(), ""));
                String headerStr = cache.get(request.uri() + HEADERS,"");
                System.out.println("sha:"+ EncryptUtil.hash(request.uri() + HEADERS,EncryptUtil.SHA1)+"\nheadString:"+headerStr);
                System.out.println("has cached header:"+(ArrayUtils.isNotEmpty(data)&&
                        StringUtils.isNotEmpty(headerStr)));
                if (ArrayUtils.isNotEmpty(data)&&
                        StringUtils.isNotEmpty(headerStr)) {
                    Map<String,Object> headMap = JSONUtil.json2Map(headerStr);
                    List<Header> headers = new ArrayList<>();
                    for (Map.Entry<String,Object> entry:headMap.entrySet()){
                        Header header = new BasicHeader(entry.getKey(),
                                String.valueOf(entry.getValue()));
                        headers.add(header);
                        System.out.println("header:  key->"+entry.getKey()+" , value->"+entry.getValue());
                    }
                    response(ctx, data, headers.toArray(new Header[headers.size()]));
                    return;
                }
                response = client.makeResponse(request.headers());
                bytes = client.getByteResponse(response);
                Map<String,Object> headerMap = new HashMap<>();
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    System.out.println("headkey:"+header.getName()+" , headvalue:"+header.getValue());
                    headerMap.put(header.getName(),header.getValue());
                }
                String headString = JSONUtil.map2Json(headerMap);
                cache.save(request.uri(), "", EncryptUtil.encodeBase64(bytes));
                cache.save(request.uri() + HEADERS, "", headString);
                System.out.println("headers:"+headers.length);
                System.out.println("http head:"+headString);
                response(ctx, bytes, headers);
                complete();
//                response = client.makeResponse(request.headers());
//                if (isJSON) {
//                    //redis缓存
//                    context = client.getResponse(response);
//                    cache.save(request.uri(), "", context);
//                    bytes = context.getBytes();
//                    response(ctx, bytes, response.getAllHeaders());
//                } else {
//                    Pattern pattern = Pattern.compile(APP_JSON + ".*");
//                    boolean isjson = false;
//                    String respnseType = "";
//                    if (response.getHeaders(CONTENT_TYPE).length != 0) {
//                        respnseType = response.getHeaders(CONTENT_TYPE)[0].getValue();
//                        isjson = pattern.matcher(respnseType).matches();
//                    }
//                    //非图片的 text/html请求,返回值是json
//                    if (isjson) {
//                        response = client.makeResponse(request.headers());
//                        context = client.getResponse(response);
//                        cache.save(request.uri(), "", context);
//                        bytes = context.getBytes();
//                        response(ctx, bytes, response.getAllHeaders());
//                    }else{
//                        bytes = client.getByteResponse(response);
//                        //CDN缓存
//                        response(ctx, bytes, response.getAllHeaders());
//                    }
//                }
            } else {
                ctx.fireChannelRead(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void complete() {
        accessRecord(address.getHostString(), address.getPort(), false);
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

    private void accessRecord(String realserver, int port, boolean incr) {
        if (incr)
            cache.incrAccessRecord(realserver + ":" + port + ACCESSRECORD);
        else cache.decrAccessRecord(realserver + ":" + port + ACCESSRECORD);
    }


}
