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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.easyproxy.resources.Resource;
import org.easyproxy.util.Config;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import static org.easyproxy.constants.Const.*;

/**
 * Description : SocksServerHandler
 * Created by YangZH on 16-5-25
 * 上午9:18
 */

public class AntiLeechHandler extends ChannelInboundHandlerAdapter {
//    private ExecutorService threadPool = Executors.newCachedThreadPool();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageReceived(ctx, msg);
    }

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
//        threadPool.submit(new Task(ctx, msg));
        HttpRequest request = (HttpRequest) msg;
        try {
            if (request.method().equals(HttpMethod.GET)) {
                Pattern pattern = Pattern.compile(".+\\.(" + IMAGE + ").*");
                byte[] bytes = null;
                CloseableHttpResponse response = null;
                HttpHeaders headers = request.headers();
                //读取图片
                if (pattern.matcher(request.uri()).matches()&&!antiLeechCheckUp(headers)) {
                    //防盗链
                    response(ctx, Resource.getResource(CODE_FORBIDDEN), HttpResponseStatus.FORBIDDEN);
                    return;
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

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void response(ChannelHandlerContext ctx, byte[] contents, HttpResponseStatus status) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(contents, 0, contents.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status, byteBuf);
        ctx.channel().writeAndFlush(response);
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
//        System.out.println("refer --> " + referername + "  localhost --> " + Config.getString(LOCALHOST));
        String localhost = Config.getString(LOCALHOST);
        Pattern pattern = Pattern.compile(".*" + localhost + ".*");
        return referername != null && pattern.matcher(referername).matches();

    }


}
