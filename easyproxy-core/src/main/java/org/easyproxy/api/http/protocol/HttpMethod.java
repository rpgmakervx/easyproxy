package org.easyproxy.api.http.protocol;

/**
 * Description :
 * Created by xingtianyu on 17-2-23
 * 下午8:48
 * description:
 */

public enum HttpMethod {
    GET, POST, PUT, DELETE, HEAD, TRACE, CONNECT, OPTIONS;

    public static HttpMethod getMethod(io.netty.handler.codec.http.HttpMethod method){
        for (HttpMethod m:values()){
            if (m.name().equals(method.name())){
                return m;
            }
        }
        return GET;
    }
}
