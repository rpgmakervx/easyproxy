package org.easyproxy.api.http.cookie;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

/**
 * Created by xingtianyu on 17-3-16
 * 上午1:13
 * description:
 */

public class HttpCookie {

    private Cookie cookie;

    public HttpCookie(String name,String value){
        cookie = new DefaultCookie(name,value);
    }

    public HttpCookie(Cookie cookie){
        this.cookie = cookie;
        cookie.setHttpOnly(true);
    }

    public String name() {
        return cookie.name();
    }

    public String value() {
        return cookie.value();
    }

    public void setValue(String value) {
        cookie.setValue(value);
    }

    public String domain() {
        return cookie.domain();
    }

    public void setDomain(String domain) {
        cookie.setDomain(domain);
    }

    public String path() {
        return cookie.path();
    }

    public void setPath(String path) {
        cookie.setPath(path);
    }

    public long maxAge() {
        return cookie.maxAge();
    }

    public void setMaxAge(long maxAge) {
        cookie.setMaxAge(maxAge);
    }

    public boolean isSecure() {
        return cookie.isSecure();
    }

    public void setSecure(boolean secure) {
        cookie.setSecure(secure);
    }

    public boolean isHttpOnly() {
        return cookie.isHttpOnly();
    }

    public void setHttpOnly(boolean httpOnly) {
        cookie.setHttpOnly(httpOnly);
    }

    public int compareTo(Cookie o) {
        return cookie.compareTo(o);
    }

    public Cookie getWrapper(){
        return this.cookie;
    }
}
