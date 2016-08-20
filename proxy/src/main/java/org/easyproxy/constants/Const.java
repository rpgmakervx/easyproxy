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
    //server itself
    public static final String PROXY = "proxy";
    public static final String LOCALHOST = "localhost";
    public static final String LISTEN = "listen";
    //real server settings
    public static final String PROXY_PASS = "proxy_pass";
    public static final String WEIGHT = "weight";
    public static final String HOST = "host";
    public static final String PORT = "port";

    //cache_strategy
    public static final String CACHE_TTL = "cache_ttl";
    public static final String CACHE_SIZE = "cache_size";
    public static final String CACHE_TYPE = "cache_type";
    //lb_strategy
    public static final String LB_STRATEGY = "lb_strategy";
    public static final String ROUNDROBIN = "roundrobin";
    public static final String WEIGHT_ROUNDROBIN = "weight";
    public static final String IP_HASH = "ip_hash";
    public static final String LESS_CONNECT = "less_connection";
    //cache_url
    public static final String CACHE_URL = "cache_url";
    public static final String URL = "url";
    public static final String METHOD = "method";
    //ip_filter
    public static final String IP_FILTER = "ip_filter";
    public static final String IP = "ip";


    //redis:
    public static final String MEMORY = "Memory";

    //structure
    public static final String DIR = System.getProperty("user.dir")+ File.separator+".."+File.separator;
    public static final String LOGS = DIR+"logs"+File.separator;
    public static final String ACCESSLOG = LOGS+"access.log";
    public static final String NULLVALUE = "-";

    //redis static key
    public static final String LIKE = "*";
    public static final String ACCESSRECORD = "-access_record";
}
