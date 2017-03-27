package org.easyproxy.api.http.response;


import org.easyproxy.api.context.HandlerContext;
import org.easyproxy.api.http.cookie.HttpCookie;
import org.easyproxy.api.mvc.entity.Json;

import java.util.Collection;
import java.util.Map;

/**
 * Created by xingtianyu on 17-3-9
 * 下午3:32
 * description:
 */

public interface HandlerResponse {

    public HandlerContext getContext();

    public void setContext(HandlerContext context);

    public void addCookie(HttpCookie cookie);

    public void setDateHeader(String name, long date);

    public void setHeader(String name, String value);

    public void addHeader(String name, String value);

    public void setStatus(int code);

    public void setStatus(int code, String msg);

    public int getStatus();

    public String getHeader(String name);

    public Collection<String> getHeaderNames();

    public String getCharacterEncoding();

    public String getContentType();

    public void setCharacterEncoding(String charset);

    public void setContentLength(int len);

    public Object getModel(String name);

    public Map<String, Object> getModel();

    public Collection<String> getModelNames();

    public void addModel(String name, Object value) ;

    public void removeModel(String name);

    public void setContentType(String type);

    public void write(byte[] content, String headerValue, int statusCode);

    public void write(byte[] content, String headerValue);

    public void write(byte[] content);

    public void write();

    public void text(String content);

    public void json(byte[] json);

    public void json(String json);

    public void json(Map<String, Object> json);

    public void json(Json json);

    public void html(String view) throws Exception;
    public void html(String view, int statusCode) throws Exception;

    public void notFound(String view) throws Exception;

    public void serverError(String view) throws Exception;

    public void image(byte[] bytes);

    public void redirect(String url);

    public void download(byte[] bytes, String filename, String headerValue);

}
