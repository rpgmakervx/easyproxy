package org.easyproxy.constants;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午10:53
 */

import java.io.File;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午10:53
 */

public class Const {

    public static final String INDEX = "index";
    public static final String CSS_FILE = "css";
    public static final String JS_FILE = "js";
    public static final String IMAGE = "(png|ico|gif|jpg|jpeg|bmp|swf|swf)";
    public static final String ROOT = "/";

    //http header params
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_CSS = "text/css";
    public static final String TEXT_HTML = "text/html";
    public static final String APP_JSON = "application/json";
    public static final String IMAGE_PNG = "image/png";
    public static final String CONTENTTYPE = "Content-Type";

    //protocol
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

    //filter
    public static final String INNER_IMAGE = "url\\(.*\\.(png|ico|jpg|jpeg|bmp|swf|swf)\\)";

    public static final String DEFAULT_CONFIGPATH = "/proxy.xml";

    //config file
    public static final String PROTOCOLNAME = "http";
    public static final String PROXY = "proxy";
    public static final String PROXY_PASS = "proxy_pass";
    public static final String LOCALHOST = "localhost";
    public static final String LISTEN = "listen";
    public static final String WEIGHT = "weight";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String TTL = "ttl";

    //redis:
    public static final String MEMORY = "Memory";

    //structure
    public static final String DIR = System.getProperty("user.dir")+ File.separator+".."+File.separator;
    public static final String LOGS = DIR+"logs"+File.separator;
    public static final String ACCESSLOG = LOGS+"access.log";
}
