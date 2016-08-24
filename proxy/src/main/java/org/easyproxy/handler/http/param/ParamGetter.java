package org.easyproxy.handler.http.param;/**
 * Description : 
 * Created by YangZH on 16-8-23
 *  下午4:42
 */

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import org.easyproxy.util.JSONUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description :
 * Created by YangZH on 16-8-23
 * 下午4:42
 */

public class ParamGetter {

    public static Map<String, Object> getRequestParams(Object msg){
        HttpRequest req = (HttpRequest) msg;
        Map<String, Object>requestParams=new HashMap<>();
        // 处理get请求
        if (req.method() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
            Map<String, List<String>> parame = decoder.parameters();
            for(Map.Entry<String, List<String>> entry:parame.entrySet()){
                requestParams.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        // 处理POST请求
        if (req.method() == HttpMethod.POST) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            String message = content.toString(CharsetUtil.UTF_8);
            if (JSONUtil.isJson(message)){
                return JSONUtil.strToMap(message);
            }
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(
                    new DefaultHttpDataFactory(false), req);
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas(); //
            for(InterfaceHttpData data:postData){
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    MemoryAttribute attribute = (MemoryAttribute) data;
                    requestParams.put(attribute.getName(), attribute.getValue());
                }
            }
        }
        return requestParams;
    }
}
