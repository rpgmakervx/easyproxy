package org.easyproxy.api.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.easyproxy.api.context.ActionHolder;
import org.easyproxy.api.context.HandlerContext;

/**
 * Description :
 * Created by xingtianyu on 17-2-23
 * 下午4:36
 * description:
 */

public class BaseChildHandler extends ChannelInitializer<SocketChannel> {

    private HandlerContext context;
    private ActionHolder holder;

    public BaseChildHandler(HandlerContext context,ActionHolder holder) {
        this.context = context;
        this.holder = holder;
    }

    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("compress", new HttpContentCompressor(9));
        pipeline.addLast("aggregator", new HttpObjectAggregator((int)context.getMaxFileUpload()));
        pipeline.addLast("decompress", new HttpContentDecompressor());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new StaticDispatcherHandler(context,holder));
        pipeline.addLast(new HttpDispatcherHandler(context,holder));
    }
}
