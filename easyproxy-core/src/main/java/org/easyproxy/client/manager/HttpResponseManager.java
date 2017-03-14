package org.easyproxy.client.manager;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.AttributeKey;
import org.easyproxy.client.future.ResponseFuture;

/**
 * Description :
 * Created by xingtianyu on 16-12-11
 * 上午2:10
 */

public class HttpResponseManager {
    private final static AttributeKey<ResponseFuture<FullHttpResponse>> futureKey = AttributeKey.valueOf("FUTUREKEY");

    public static void setAttr(Channel channel, ResponseFuture<FullHttpResponse> future){
        channel.attr(futureKey).set(future);
    }
    public static ResponseFuture<FullHttpResponse> getAttr(Channel channel){
        return channel.attr(futureKey).get();
    }
}
