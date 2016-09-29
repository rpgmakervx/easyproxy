package org.easyproxy.handler.http;/**
 * Description : 
 * Created by YangZH on 16-9-27
 *  下午8:19
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

/**
 * Description :
 * Created by YangZH on 16-9-27
 * 下午8:19
 * 抽象了众多handler的相似方法
 */

public abstract class CommonHandler extends ChannelInboundHandlerAdapter {

    //业务核心写在messageReceived中
    public abstract void messageReceived(ChannelHandlerContext ctx, Object msg);
    //后置处理
    public abstract void before(ChannelHandlerContext ctx, Object msg);
    //前置处理
    public abstract void after(ChannelHandlerContext ctx, Object msg);
    //释放资源等等
    public abstract void complete();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        before(ctx,msg);
        messageReceived(ctx,msg);
        after(ctx,msg);
        complete();
    }

    private void responseOK(ChannelHandlerContext ctx, byte[] contents, Header[] headers) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, byteBuf);
        for (Header header : headers) {
            response.headers().set(header.getName(), header.getValue());
        }
        ctx.channel().writeAndFlush(response);
        ctx.close();
    }

    private void responseNoHeader(ChannelHandlerContext ctx, byte[] contents) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, byteBuf);
        ctx.channel().writeAndFlush(response);
        ctx.close();
    }
}
