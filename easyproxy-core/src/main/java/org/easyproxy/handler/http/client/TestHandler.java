package org.easyproxy.handler.http.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * Description :
 * Created by xingtianyu on 16-12-8
 * 上午2:03
 */

public class TestHandler extends ChannelInboundHandlerAdapter {


    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpResponse response = (FullHttpResponse) msg;
        ByteBuf buf = response.content();
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        System.out.println("response : "+new String(req));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageReceived(ctx, msg);
    }

}
