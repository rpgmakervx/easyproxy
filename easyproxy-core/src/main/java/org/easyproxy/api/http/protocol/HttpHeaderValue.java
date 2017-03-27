package org.easyproxy.api.http.protocol;


import org.easyproxy.api.kits.file.FileFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xingtianyu on 17-3-4
 * 下午5:45
 * description:
 */

public class HttpHeaderValue {

    public static final String TEXT_HTML = "text/html;charset=utf-8";
    public static final String TEXT_PLAIN = "text/plain;charset=utf-8";
    public static final String APPLICATION_JSON = "application/json;charset=utf-8";
    public static final String JS = "application/javascript";
    public static final String CSS = "text/css";
    public static final String SVG = "image/svg+xml";
    public static final String WOFF = "application/x-font-woff";
    public static final String TTF_EOT = "application/octet-stream";
    public static final String ZIP = "application/zip";
    public static final String MP3 = "audio/mpeg";
    public static final String MP4 = "video/mp4";

    public static final String IMAGE = "image/png";
    public static final String PDF = "application/pdf;charset=utf-8";
    public static final String DOC = "application/msword;charset=utf-8";
    public static final String XLS = "application/vnd.ms-excel;charset=utf-8";

    public static final String ATTACHMENT = "attachment;filename=";

    public static final String HOST = "Host";

    public static final String MAXAGE = "max-age=";

    private static Map<String,String> typeMapper = new HashMap<>();

    static {
        typeMapper.put(FileFilter.HTML,TEXT_HTML);
        typeMapper.put(FileFilter.JS,JS);
        typeMapper.put(FileFilter.JSON,APPLICATION_JSON);
        typeMapper.put(FileFilter.CSS,CSS);
        typeMapper.put(FileFilter.SVG,SVG);
        typeMapper.put(FileFilter.WOFF,WOFF);
        typeMapper.put(FileFilter.TTF,TTF_EOT);
        typeMapper.put(FileFilter.EOT,TTF_EOT);
        typeMapper.put(FileFilter.PNG,IMAGE);
        typeMapper.put(FileFilter.JPEG,IMAGE);
        typeMapper.put(FileFilter.JPG,IMAGE);
        typeMapper.put(FileFilter.GIF,IMAGE);
        typeMapper.put(FileFilter.TXT,TEXT_PLAIN);
    }

    public static String getContentType(String fileType){
        return typeMapper.get(fileType);
    }
}
