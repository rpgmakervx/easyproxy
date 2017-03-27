package org.easyproxy.api.mvc.action.handler.impl;


import org.easyproxy.api.context.HandlerContext;
import org.easyproxy.api.http.protocol.HttpHeaderName;
import org.easyproxy.api.http.protocol.HttpHeaderValue;
import org.easyproxy.api.http.protocol.HttpStatus;
import org.easyproxy.api.http.request.HandlerRequest;
import org.easyproxy.api.http.response.HandlerResponse;
import org.easyproxy.api.kits.TimeKits;
import org.easyproxy.api.kits.file.FileKits;
import org.easyproxy.api.mvc.action.handler.HttpHandler;

import java.io.File;
import java.util.regex.Pattern;


/**
 * Created by xingtianyu on 17-3-9
 * 下午3:53
 * description:处理静态资源，以及强缓存和协商缓存
 * 目前静态资源用过滤器处理
 */
//@Deprecated
public class StaticHttpHandler implements HttpHandler {

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        HandlerContext context = request.getContext();
        String webView = context.getWebView();
        String uri = request.getRequestURI();
        Pattern cachedPattern = Pattern.compile(CACHEPATTERN);

        StringBuffer resourcePath = new StringBuffer();
        resourcePath.append(webView).append(uri);
        int point = uri.lastIndexOf(".");
        String suffix = "";
        String filename = "";
        if (point != -1) {
            suffix = uri.substring(point, uri.length());
            filename = uri.substring(uri.lastIndexOf(File.separator), point);
        } else {
            filename = uri.substring(uri.lastIndexOf(File.separator), uri.length());
        }
        if (cachedPattern.matcher(suffix).matches()){
            checkStrongCache(request,response);
            boolean cached = checkNagoCache(request,response,resourcePath.toString());
            if (cached){
                response.write();
                return;
            }
            response.write(FileKits.read(resourcePath.toString())
                    , HttpHeaderValue.getContentType(suffix));
        }else if (suffix.endsWith(DOCX)||suffix.endsWith(DOC)){
            response.download(FileKits.read(resourcePath.toString()),filename,HttpHeaderValue.DOC);
        }else if (suffix.endsWith(XLS)||suffix.endsWith(XLSX)){
            response.download(FileKits.read(resourcePath.toString()),filename,HttpHeaderValue.XLS);
        }else if (suffix.endsWith(PDF)){
            response.download(FileKits.read(resourcePath.toString()),filename,HttpHeaderValue.PDF);
        }else{
            response.write(FileKits.read(resourcePath.toString()));
        }
    }

    private void checkStrongCache(HandlerRequest request, HandlerResponse response){
        if (!request.getContext().isStrongCache()){
            return ;
        }
        response.setHeader(HttpHeaderName.CACHE_CONTROL,
                HttpHeaderValue.MAXAGE+String.valueOf(request.getContext().getMaxAge()));
    }

    private boolean checkNagoCache(HandlerRequest request, HandlerResponse response,String resourcePath){
        if (!request.getContext().isNegoCache()){
            return false;
        }
        String ifNoneMatch = request.getHeader(HttpHeaderName.IF_NONE_MATCH);
        String etag = FileKits.md5(resourcePath);
        String lastModify = TimeKits.getGMTTime(FileKits.getLastModifyTime(resourcePath));
        String lastModifySince = request.getHeader(HttpHeaderName.IF_MODIFIED_SINCE);
        if (etag.equals(ifNoneMatch)){
            if (lastModify.equals(lastModifySince)){
                response.setStatus(HttpStatus.NOT_MODIFIED);
                return true;
            }
        }
        response.setHeader(HttpHeaderName.LAST_MODIFIED,lastModify);
        response.setHeader(HttpHeaderName.ETAG,etag);
        return false;
    }

}
