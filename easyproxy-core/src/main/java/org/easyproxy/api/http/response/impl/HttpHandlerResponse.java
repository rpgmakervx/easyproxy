package org.easyproxy.api.http.response.impl;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import org.easyproxy.api.context.HandlerContext;
import org.easyproxy.api.http.cookie.HttpCookie;
import org.easyproxy.api.http.protocol.HttpHeaderName;
import org.easyproxy.api.http.protocol.HttpHeaderValue;
import org.easyproxy.api.http.protocol.HttpStatus;
import org.easyproxy.api.http.response.HandlerResponse;
import org.easyproxy.api.kits.ByteKits;
import org.easyproxy.api.kits.StringKits;
import org.easyproxy.api.kits.file.FileKits;
import org.easyproxy.api.mvc.entity.Json;
import org.easyproxy.api.mvc.temp.TemplateParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.easyproxy.api.http.Const.*;
import static org.easyproxy.api.http.protocol.HttpHeaderName.SET_COOKIE;


/**
 * Description :
 * Created by xingtianyu on 17-2-24
 * 下午5:23
 * description:
 */

public class HttpHandlerResponse implements HandlerResponse {

    private FullHttpResponse response;
    private Channel channel;
    private HttpHeaders headers;
    private String charset;

    private TemplateParser tmpParser;

    private Map<String,Object> models = new ConcurrentHashMap<>();

    public HandlerContext context;

    private ServerCookieEncoder encoder;

    public HttpHandlerResponse(FullHttpResponse response, HandlerContext context, Channel channel) throws IOException {
        init(response,context,channel);
    }

    private void init(FullHttpResponse response, HandlerContext context, Channel channel) throws IOException {
        this.context = context;
        this.response = response;
        this.tmpParser = new TemplateParser(context);
        if (response == null){
            this.headers = new DefaultHttpHeaders();
        }else {
            this.headers = this.response.headers();
        }
        this.charset = "UTF-8";
        this.channel = channel;
        this.encoder = ServerCookieEncoder.LAX;
    }

    @Override
    public HandlerContext getContext() {
        return context;
    }
    @Override
    public void setContext(HandlerContext context) {
        this.context = context;
    }

    @Override
    public void addCookie(HttpCookie cookie) {
        this.headers.add(SET_COOKIE, encoder.encode(cookie.getWrapper()));
    }

    @Override
    public void setDateHeader(String name, long date) {
        this.headers.set(name, date);
    }

    @Override
    public void setHeader(String name, String value) {
        this.headers.set(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        this.headers.add(name,value);
    }

    @Override
    public void setStatus(int code) {
        response.setStatus(HttpResponseStatus.valueOf(code));
    }

    @Override
    public void setStatus(int code, String msg) {
        response.setStatus(new HttpResponseStatus(code, msg));
    }

    @Override
    public int getStatus() {
        return response.status().code();
    }


    @Override
    public String getHeader(String name) {
        String headr = this.headers.get(name);
        return encode(headr);
    }


    @Override
    public Collection<String> getHeaderNames() {
        return this.headers.names();
    }


    @Override
    public String getCharacterEncoding() {
        return this.charset;
    }


    @Override
    public String getContentType() {
        return this.headers.get(HttpHeaderNames.CONTENT_TYPE);
    }


    @Override
    public void setCharacterEncoding(String charset) {
        this.charset = charset;
    }
    @Override
    public Object getModel(String name) {
        return models.get(name);
    }

    @Override
    public Map<String, Object> getModel() {
        return models;
    }

    @Override
    public Collection<String> getModelNames() {
        return models.keySet();
    }

    @Override
    public void addModel(String name, Object value) {
        System.out.println("name:"+ models +",value:"+value);
        models.put(name,value);
    }

    @Override
    public void removeModel(String name) {
        models.remove(name);
    }
    @Override
    public void setContentLength(int len) {
        this.headers.set(HttpHeaderNames.CONTENT_LENGTH, len);
    }
    @Override
    public void setContentType(String type) {
        this.headers.set(HttpHeaderNames.CONTENT_TYPE, type);
    }

    @Override
    public void write(byte[] content, String contentType,int statusCode) {
        setHeader(HttpHeaderName.CONTENT_TYPE ,contentType);
        setHeader(HttpHeaderName.CONTENT_LENGTH, String.valueOf(content.length));
        response = response.copy(ByteKits.toByteBuf(content));
        response.setStatus(HttpResponseStatus.valueOf(statusCode));
        channel.writeAndFlush(response);
    }
    @Override
    public void write(byte[] content, String contentType) {
        setHeader(HttpHeaderName.CONTENT_TYPE, contentType);
        setHeader(HttpHeaderName.CONTENT_LENGTH, String.valueOf(content.length));
        response = response.copy(ByteKits.toByteBuf(content));
        channel.writeAndFlush(response);
    }

    @Override
    public void write(byte[] content) {
        setHeader(HttpHeaderName.CONTENT_LENGTH, String.valueOf(content.length));
        response = response.copy(ByteKits.toByteBuf(content));
        channel.writeAndFlush(response);
    }

    @Override
    public void write() {
        channel.writeAndFlush(response);
    }

    @Override
    public void text(String content) {
        write(encode(content).getBytes(), HttpHeaderValue.TEXT_PLAIN);
    }

    @Override
    public void json(byte[] json) {
        write(json,HttpHeaderValue.APPLICATION_JSON);
    }
    @Override
    public void json(String json) {
        write(json.getBytes(), HttpHeaderValue.APPLICATION_JSON);
    }
    @Override
    public void json(Map<String,Object> json) {
        json(encode(Json.stringify(json)));
    }
    @Override
    public void json(Json json) {
        json(json.getJsonMap());
    }

    /**
     * 视图不存在返回404,404如果还不存在，只能报错
     * @param view
     * @param statusCode
     */
    @Override
    public void html(String view,int statusCode) throws Exception {
        StringBuffer pathBuffer = new StringBuffer();
        pathBuffer.append(view).append(POINT)
                .append(context.getViewSuffix());
        String wholePath = context.getWebView()+context.getViewPrefix()+pathBuffer.toString();
        System.out.println("whole path:"+wholePath);
        if (!FileKits.exists(wholePath)){
            notFound();
            return;
        }
        tmpParser.addParam(models);
        byte[] content = tmpParser.getTemplate(pathBuffer.toString()).getBytes();
        write(content,HttpHeaderValue.TEXT_HTML,statusCode);
    }

    @Override
    public void html(String view) throws Exception {
        html(view,HttpResponseStatus.OK.code());
    }

    @Override
    public void notFound(String view) throws Exception {
        html(view, HttpStatus.NOT_FOUND);
    }

    @Override
    public void serverError(String view) throws Exception {
        html(view,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void image(byte[] bytes){
        write(bytes,HttpHeaderValue.IMAGE);
    }

    @Override
    public void redirect(String url){
        response.setStatus(HttpResponseStatus.FOUND);
        setHeader(HttpHeaderNames.LOCATION.toString(),url);
        write();
    }

    @Override
    public void download(byte[] bytes,String filename,String contentType){
        String fn = filename;
        try {
            fn = new String(filename.getBytes("GB2312"),"ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        setHeader(HttpHeaderNames.CONTENT_DISPOSITION.toString(),HttpHeaderValue.ATTACHMENT + fn);
        write(bytes,contentType.toString());
    }

    private String encode(String content) {
        if (StringKits.isEmpty(content)) {
            return "";
        }
        try {
            return new String(content.getBytes(), charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return content;
        }
    }

    private void notFound() throws Exception {
        int statusCode = HttpResponseStatus.NOT_FOUND.code();
        StringBuffer pathBuffer = new StringBuffer();
        pathBuffer.append(context.getErrorPage())
                .append(POINT)
                .append(context.getViewSuffix());
        addModel(HTTPSTATUS,statusCode);
        addModel(REASONPHASE, HttpResponseStatus.NOT_FOUND.reasonPhrase());
        addModel(MESSAGE,"");
        TemplateParser parser = new TemplateParser(HandlerContext.DEFAULT_RESOURCE);
        parser.addParam(models);
        byte[] content = parser.getTemplate(pathBuffer.toString()).getBytes();
        write(content,HttpHeaderValue.TEXT_HTML,statusCode);
    }
}
