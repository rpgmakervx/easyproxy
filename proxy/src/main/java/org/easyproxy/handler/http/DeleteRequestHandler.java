package org.easyproxy.handler.http;/**
 * Description : 
 * Created by YangZH on 16-9-27
 *  上午2:18
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
import org.easyproxy.resources.Resource;
import org.easyproxy.selector.IPSelector;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import static io.netty.handler.codec.http.HttpMethod.DELETE;
import static org.easyproxy.constants.Const.*;

/**
 * Description :
 * Created by YangZH on 16-9-27
 * 上午2:18
 */

public class DeleteRequestHandler extends ChannelInboundHandlerAdapter {

    private InetSocketAddress address;
    private Cache cache = new Cache();

    public void chooseAddress(String ip) {
        IPSelector selector = new IPSelector(ip);
        this.address = selector.select();
        System.out.println(Thread.currentThread().getName()+" 新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageReceived(ctx, msg);
    }

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
//        threadPool.submit(new Task(ctx, msg));
        HttpRequest request = (HttpRequest) msg;
        try {
            if (request.method().equals(DELETE)) {
                InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = addr.getHostString();
                chooseAddress(ip);
                accessRecord(address.getHostString(),address.getPort());
                CloseableHttpResponse response = null;
                ProxyClient client = new ProxyClient(address, ROOT.equals(request.uri()) ? "" : request.uri());
                response = client.deleteRequest(request.headers());
                String responseStr = client.getResponse(response);
                byte[] bytes = responseStr.getBytes();
                response(ctx, bytes, response.getAllHeaders());
            } else {
                System.out.println("请求不是GET POST PUT DELETE之一，不合法");
                response(ctx, Resource.getResource(CODE_BADREQUEST),HttpResponseStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
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
    private void response(ChannelHandlerContext ctx, byte[] contents,HttpResponseStatus status) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status, byteBuf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, TEXT_HTML);
        ctx.channel().writeAndFlush(response);
        ctx.close();
    }

    private void accessRecord(String realserver,int port){
        System.out.println("access record---> "+realserver+":"+port+ACCESSRECORD);
        cache.incrAccessRecord(realserver+":"+port+ACCESSRECORD);
    }
}
