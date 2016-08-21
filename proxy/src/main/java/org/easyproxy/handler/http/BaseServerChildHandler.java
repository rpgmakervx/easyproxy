package org.easyproxy.handler.http;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午10:28
 */

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:28
 */

public class BaseServerChildHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        System.out.println("new request");
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("compress", new HttpContentCompressor(9));
        pipeline.addLast("decompress", new HttpContentDecompressor());
        pipeline.addLast("aggregator", new HttpObjectAggregator(1024000));
        pipeline.addLast(new AccessLogHandler());
        pipeline.addLast(new AntiLeechHandler());
        pipeline.addLast(new GetRequestHandler());
        pipeline.addLast(new PostRequestHandler());

    }
}
