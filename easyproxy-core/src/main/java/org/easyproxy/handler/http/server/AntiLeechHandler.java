package org.easyproxy.handler.http.server;/**
 * Description : SocksServerHandler
 * Created by YangZH on 16-5-25
 *  上午9:18
 */

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.easyproxy.config.ConfigEnum;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.resources.Resource;

import java.util.regex.Pattern;

import static org.easyproxy.config.ConfigEnum.LOCALHOST;
import static org.easyproxy.constants.Const.CODE_FORBIDDEN;
import static org.easyproxy.constants.Const.IMAGE;

/**
 * Description : SocksServerHandler
 * Created by YangZH on 16-5-25
 * 上午9:18
 */

public class AntiLeechHandler extends ChannelInboundHandlerAdapter {
//    private ExecutorService threadPool = Executors.newCachedThreadPool();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean isAntileechOpen = Boolean.valueOf(ConfigFactory.getConfig().getString(ConfigEnum.ANTILEECH_OPEN.key));
        if (isAntileechOpen){
            messageReceived(ctx, msg);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
//        threadPool.submit(new Task(ctx, msg));
        FullHttpRequest request = (FullHttpRequest) msg;
        if (!request.method().equals(HttpMethod.GET)) {
            ctx.fireChannelRead(request);
        } else {
            Pattern pattern = Pattern.compile(".+\\.(" + IMAGE + ").*");
            //读取图片
            if (pattern.matcher(request.uri()).matches()&&!antiLeechCheckUp(request)) {
                //防盗链
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                        HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer(Resource.getResource(CODE_FORBIDDEN)));
                ctx.channel().writeAndFlush(response);
                return;
            } else {
                ctx.fireChannelRead(request);
            }
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

    /**
     * 根据请球头的referer校验是否是盗链者
     *
     * @return
     * @throws Exception
     */
    private boolean antiLeechCheckUp(FullHttpRequest request) throws Exception {
        String referername = request.headers().get(HttpHeaderNames.REFERER);
        String localhost = ConfigFactory.getConfig().getString(LOCALHOST.key);
        Pattern pattern = Pattern.compile(".*" + localhost + ".*");
        return referername != null && pattern.matcher(referername).matches();

    }


}
