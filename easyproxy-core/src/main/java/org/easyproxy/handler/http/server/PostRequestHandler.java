package org.easyproxy.handler.http.server;/**
 * Description : 
 * Created by YangZH on 16-6-3
 *  下午10:23
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.easyproxy.cache.DefaultCache;
import org.easyproxy.cache.redis.RedisCache;
import org.easyproxy.client.HttpUtils;
import org.easyproxy.selector.IPSelector;

import java.net.InetSocketAddress;

import static io.netty.handler.codec.http.HttpMethod.POST;
import static org.easyproxy.constants.Const.ACCESSRECORD;

/**
 * Description :
 * Created by YangZH on 16-6-3
 * 下午10:23
 */

public class PostRequestHandler extends ChannelInboundHandlerAdapter {

    private InetSocketAddress address;
    private DefaultCache cache = new RedisCache();

    /**
     * 每次请求都重新获取一次地址
     */
    public void allocAdress(String ip) {
        IPSelector selector = new IPSelector(ip);
        this.address = selector.select();
        System.out.println(Thread.currentThread().getName()+" 新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageReceived(ctx, msg);
    }

    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        if (!request.method().equals(POST)){
            ctx.fireChannelRead(request);
            return;
        }
        InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = addr.getHostString();
        allocAdress(ip);
        HttpUtils client = new HttpUtils(address);
        client.connect();
        client.send(request);
        FullHttpResponse response = client.getWholeResponse();
        ctx.writeAndFlush(response);
        complete();
    }

    //    @Override
//    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
////        threadPool.submit(new Task(ctx, msg));
//        HttpRequest request = (HttpRequest) msg;
//        try {
//            if (request.method().equals(POST)) {
//                InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
//                String ip = addr.getHostString();
//                allocAdress(ip);
////                accessRecord(address.getHostString(),address.getPort());
//                CloseableHttpResponse response = null;
//                ProxyClient client = new ProxyClient(address, ROOT.equals(request.uri()) ? "" : request.uri());
//                byte[] bytes = null;
//                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
//                if (decoder.isMultipart()) {
//                    try {
//                        List<InterfaceHttpData> postList = decoder.getBodyHttpDatas();
//                        // 读取从客户端传过来的参数
//                        Map<String,Object> params = new HashMap<String,Object>();
//                        File hasFile = null;
//                        for (InterfaceHttpData data : postList) {
//                            String name = data.getName();
//                            if (InterfaceHttpData.HttpDataType.Attribute == data.getHttpDataType()) {
//                                MemoryAttribute attribute = (MemoryAttribute) data;
//                                attribute.setCharset(CharsetUtil.UTF_8);
//                                params.put(name,attribute.getValue());
//                            }else if (InterfaceHttpData.HttpDataType.FileUpload == data.getHttpDataType()){
//                                MemoryFileUpload fileUpload = (MemoryFileUpload) data;
//                                hasFile = FileUtil.tempFile(fileUpload.get(),fileUpload.getFilename());
//                                params.put(name, hasFile);
//                            }
//                        }
//                        response = client.postMultipartEntityRequest(params, request.headers());
//                        //删除临时文件
//                        if (hasFile != null){
//                            FileUtil.delete(hasFile);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else if (request instanceof HttpContent) {
//                    HttpContent httpContent = (HttpContent) request;
//                    ByteBuf content = httpContent.content();
//                    String message = content.toString(CharsetUtil.UTF_8);
//                    if (JSONUtil.isJson(message)) {
//                        response = client.postJsonRequest(message, request.headers());
//                    } else {
//                        response = client.postEntityRequest(ParamGetter.getRequestParams(msg), request.headers());
//                    }
//                }
//                String responseStr = client.getResponse(response);
//                System.out.println(responseStr);
//                bytes = responseStr.getBytes();
//                response(ctx, bytes, response.getAllHeaders());
//                complete();
//            } else {
//                ctx.fireChannelRead(msg);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void complete() {
        accessRecord(address.getHostString(), address.getPort(), false);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private void accessRecord(String realserver, int port, boolean incr) {
        if (incr)
            cache.incrAccessRecord(realserver + ":" + port + ACCESSRECORD);
        else cache.decrAccessRecord(realserver + ":" + port + ACCESSRECORD);
    }

}
