package org.easyproxy.handler.http.server;/**
 * Description : 
 * Created by YangZH on 16-8-16
 *  下午2:19
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import org.easyproxy.config.ConfigEnum;
import org.easyproxy.config.ConfigFactory;
import org.easyproxy.log.Logger;
import org.easyproxy.util.time.TimeUtil;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;

/**
 * Description :
 * Created by code4j on 16-8-16
 * 下午2:19
 */

public class AccessLogHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = new Logger();

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        InetSocketAddress addr = getAddress(ctx);
        String ip = addr.getHostString();
        //选择路由
        //记录真实节点的访问量
        HttpRequest request = (HttpRequest) msg;
        generateLog(request,ip);
        ctx.fireChannelRead(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean isLogOpen = Boolean.valueOf(ConfigFactory.getConfig().getString(ConfigEnum.LOG_OPEN.key));
        if (isLogOpen){
            messageReceived(ctx, msg);
        }else{
            ctx.fireChannelRead(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    private InetSocketAddress getAddress(ChannelHandlerContext ctx){
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        return address;
    }
    private void generateLog(HttpRequest request,String client_ip){
        HttpHeaders headers = request.headers();
        StringBuffer buffer = new StringBuffer();
        buffer.append("currentTime:"+ TimeUtil.getFormattedDate(new Date())+"\n");
        for (Map.Entry<String, String> entry:headers.entries()){
            buffer.append(entry.getKey()+":"+entry.getValue()+"\n");
        }
        logger.accessLog(buffer.toString() + "\n");
    }
}
