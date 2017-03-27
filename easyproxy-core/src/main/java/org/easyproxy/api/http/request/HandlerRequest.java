package org.easyproxy.api.http.request;


import org.easyproxy.api.context.HandlerContext;
import org.easyproxy.api.http.cookie.HttpCookie;
import org.easyproxy.api.http.session.HttpSession;
import org.easyproxy.api.mvc.entity.UploadFile;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by xingtianyu on 17-3-9
 * 下午3:29
 * description:
 */

public interface HandlerRequest {
    public HandlerContext getContext();

    public Set<HttpCookie> getCookies();


    public String getHeader(String name);

    public Collection<String> getHeaderNames();


    public Long getDateHeader(String name);


    public String getMethod();

    public String getContextPath();

    public UploadFile file(String name);

    public<T> T body(Class<T> cls) throws Exception;

    public String getQueryString();

    public String getSessionId();

    public SocketAddress getRemoteAddress();

    public String getRequestURI();

    public HttpSession getSession();

    public String getCharacterEncoding();

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException;

    public int getContentLength();

    public String getContentType();

    public String getParam(String name);

    public Integer getIntParam(String name);

    public Collection<String> getParamNames();

    public Collection<String> getParamValues(String name);

    public Map getParamMap();

    public String getProtocol();

    public String getRemoteAddr();

}
