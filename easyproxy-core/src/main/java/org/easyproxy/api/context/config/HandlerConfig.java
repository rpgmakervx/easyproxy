package org.easyproxy.api.context.config;

import org.easyproxy.api.context.HandlerContext;
import org.easyproxy.api.kits.StringKits;

import java.io.File;

/**
 * Created by xingtianyu on 17-3-14
 * 下午4:32
 * description:
 */

public class HandlerConfig {

    private HandlerContext context;

    public HandlerConfig(HandlerContext context){
        this.context = context;
    }

    public HandlerConfig webView(String webView){
        if (!webView.startsWith("/")){
            webView = "/" + webView;
        }
        context.setWebView(webView);
        return this;
    }

    public HandlerConfig viewPrefix(String viewPrefix){
        if (StringKits.isNotEmpty(viewPrefix)&&!viewPrefix.startsWith(File.separator)){
            viewPrefix = "/" + viewPrefix;
        }
        context.setViewPrefix(viewPrefix);
        return this;
    }

    public HandlerConfig viewSuffix(String viewSuffix){
        context.setViewSuffix(viewSuffix);
        return this;
    }

    public HandlerConfig contextPath(String contextPath){
        if (!contextPath.startsWith("/")){
            contextPath = "/" + contextPath;
        }
        if (!contextPath.endsWith("/")){
            contextPath = contextPath + "/";
        }
        context.setContextPath(contextPath);
        return this;
    }

    public HandlerConfig cacheMaxAge(int maxAge){
        context.setMaxAge(maxAge);
        return this;
    }

    public HandlerConfig useCache(){
        context.setNegoCache(true);
        return this;
    }

    public HandlerConfig maxFileUpload(long maxFileUpload){
        context.setMaxFileUpload(maxFileUpload);
        return this;
    }
}
