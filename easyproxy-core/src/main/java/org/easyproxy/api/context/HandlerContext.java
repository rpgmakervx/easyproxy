package org.easyproxy.api.context;


import org.easyproxy.api.http.session.HttpSession;
import org.easyproxy.api.kits.file.FileKits;

import java.io.File;

/**
 * Description :
 * Created by xingtianyu on 17-2-28
 * 上午10:51
 * description:
 */

public class HandlerContext {
    public static final String DEFAULT_CONTEXT = File.separator;
    public static final String DEFAULT_SUFFIX = "html";

    public static final String DEFAULT_ERRORPAGE = "error";

    public static final String WEB_INF = "/WEB-INF/";

    private static final int DEFAULT_PORT = 8080;

    public static final String DEFAULT_RESOURCE = HandlerContext.class.getResource("/").getPath();

    private int remotePort = DEFAULT_PORT;

    private int maxAge = 3600;

    private long maxFileUpload = FileKits.ONE_MB * 128;

    private int sessionAge = Integer.MAX_VALUE;

    public boolean negoCache = false;
    public boolean strongCache = true;


    public String contextPath = File.separator;

    public String errorPage = "error";
    /**
     * web资源路径
     */
    public String webView = DEFAULT_RESOURCE;
    /**
     * 视图资源路径前缀
     */
    public String viewPrefix = WEB_INF;
    /**
     * 视图资源文件后缀（默认html）
     */
    public String viewSuffix = DEFAULT_SUFFIX;

    public SessionHolder sessionHolder;

    public HandlerContext(){
        sessionHolder = new SessionHolder();
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        if (remotePort < 1){
            remotePort = DEFAULT_PORT;
        }
        this.remotePort = remotePort;
    }

    public String getContextPath(){
        return contextPath;
    }

    public String getWebView(){
        return webView;
    }

    public void setWebView(String root){
        this.webView = root;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getViewPrefix() {
        return viewPrefix;
    }

    public void setViewPrefix(String viewPrefix) {
        this.viewPrefix = viewPrefix;
    }

    public String getViewSuffix() {
        return viewSuffix;
    }

    public void setViewSuffix(String viewSuffix) {
        this.viewSuffix = viewSuffix;
    }

    public HttpSession getSession(String sessionId){
        return sessionHolder.getSession(sessionId);
    }

    public void addSession(String sessionId, HttpSession session){
        sessionHolder.addSession(sessionId,session);
    }

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public int getMaxAge(){
        return maxAge;
    }

    public void setMaxAge(int maxAge){
        this.maxAge = maxAge;
    }

    public int getSessionAge() {
        return sessionAge;
    }

    public void setSessionAge(int sessionAge) {
        this.sessionAge = sessionAge;
    }

    public boolean isNegoCache() {
        return negoCache;
    }

    public void setNegoCache(boolean negoCache) {
        this.negoCache = negoCache;
    }

    public boolean isStrongCache() {
        return strongCache;
    }

    public void setStrongCache(boolean strongCache) {
        this.strongCache = strongCache;
    }

    public long getMaxFileUpload() {
        return maxFileUpload;
    }

    public void setMaxFileUpload(long maxFileUpload) {
        this.maxFileUpload = maxFileUpload;
    }
}
