package org.easyproxy.handler.http.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.easyproxy.cache.DefaultCache;
import org.easyproxy.cache.redis.RedisCache;
import org.easyproxy.client.HttpUtils;
import org.easyproxy.selector.IPSelector;
import org.easyproxy.util.codec.EncryptUtil;
import org.easyproxy.util.struct.JSONUtil;

import java.net.InetSocketAddress;
import java.util.Map;

import static org.easyproxy.constants.Const.ACCESSRECORD;
import static org.easyproxy.constants.Const.HEADERS;

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
    public void allocAdress(String ip) {
        IPSelector selector = new IPSelector(ip);
        this.address = selector.select();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageRece(ctx, msg);
    }

    protected void messageRece(ChannelHandlerContext ctx, Object msg) throws Exception{
        String context = "";
        byte[] bytes = null;
        FullHttpRequest request = (FullHttpRequest) msg;
        boolean isGet = request.method().equals(HttpMethod.GET);
        if (!isGet) {
            ctx.fireChannelRead(request);
            return;
        }
        InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = addr.getHostString();
        allocAdress(ip);
        accessRecord(address.getHostString(), address.getPort(), true);
        HttpUtils client = new HttpUtils(address);
        client.connect();
        byte[] data = EncryptUtil.decodeBase64(cache.get(request.uri(), ""));
        String headerStr = cache.get(request.uri() + HEADERS,"");
        if (ArrayUtils.isNotEmpty(data)&&
                StringUtils.isNotEmpty(headerStr)) {
            Map<String,Object> headMap = JSONUtil.json2Map(headerStr);
//                    List<Header> headers = new ArrayList<>();
            for (Map.Entry<String,Object> entry:headMap.entrySet()){
                request.headers().set(entry.getKey(),
                        String.valueOf(entry.getValue()));
            }
            ByteBuf byteBuf = Unpooled.wrappedBuffer(data);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, byteBuf);
            ctx.channel().writeAndFlush(response);
            return;
        }
        client.send(request);
        String headString = JSONUtil.map2Json(client.getHeadersAsMap());
        cache.save(request.uri(), "", EncryptUtil.encodeBase64(bytes));
        cache.save(request.uri() + HEADERS, "", headString);
        ctx.writeAndFlush(client.getWholeResponse());
        complete();
    }

    public void complete() {
        System.out.println("address:"+address);
        accessRecord(address.getHostString(), address.getPort(), false);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    private void accessRecord(String realserver, int port, boolean incr) {
        if (incr)
            cache.incrAccessRecord(realserver + ":" + port + ACCESSRECORD);
        else cache.decrAccessRecord(realserver + ":" + port + ACCESSRECORD);
    }


}
