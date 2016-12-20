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
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String HEADERS = "headers";

    //protocol
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

    //filter
    public static final String INNER_IMAGE = "url\\(.*\\.(png|ico|jpg|jpeg|bmp|swf|swf)\\)";

    public static final String XML_CONFIGFILE = "proxy.xml";
    public static final String DEFAULT_CONFIGFILE = "proxy.properties";

    //config file
    public static final String PROTOCOLNAME = "http";
    //server itself
    public static final String PROXY = "proxy";
    public static final String LOCALHOST = "proxy.server.localhost";
    public static final String LISTEN = "proxy.server.listen";
    //real server settings
    //proxy_server 配置文件不存在，是检测到proxy_pass下没有配置后中间件自己设置的
    public static final String PROXY_SERVER = "proxy_server";
    public static final String PROXY_PASS = "proxy_pass";
    public static final String WEIGHT = "proxy.server.nodes.weight";
    public static final String HOST = "proxy.server.nodes.ip";
    public static final String PORT = "proxy.server.nodes.port";

    //cache_strategy
    public static final String CACHE_TTL = "proxy.cache.ttl";
    public static final String CACHE_SIZE = "cache_size";
    public static final String CACHE_TYPE = "proxy.cache.type";
    //lb_strategy
    public static final String LB_STRATEGY = "proxy.server.lb_strategy";
    public static final String ROUNDROBIN = "roundrobin";
    public static final String WEIGHT_ROUNDROBIN = "weight";
    public static final String IP_HASH = "ip_hash";
    public static final String LESS_CONNECT = "least_connection";
    //cache_type
    public static final String REDIS = "redis";
    public static final String EHCACHE = "ehcache";
    public static final String JAVA = "java";


    //cache_url
    public static final String CACHE_URL = "cache_url";
    public static final String URL = "url";
    public static final String METHOD = "method";
    //ip_filter
    public static final String IP_FILTER = "ip_filter";
    public static final String FILTERED_IP = "proxy.firewall.filter";
    //static resource page
    public static final String NOTFOUND_PAGE = "proxy.firewall.notfound_page";
    public static final String ERROR_PAGE = "proxy.firewall.error_page";
    public static final String FORBIDDEN_PAGE = "proxy.firewall.forbidden_page";
    public static final String BADREQUEST_PAGE = "proxy.firewall.bad_request";

    //API
    public static final String APIOPEN = "proxy.api.open";
    public static final String API_URI = "proxy.api.uri";
    //log open
    public static final String LOGOPEN = "proxy.log.logopen";
    //anti-leech open
    public static final String ANTILEECH_OPEN = "proxy.antileech.open";

    //personal uri
    public static final String PERSONAL_URL = "proxy.resource.static_uri";


    //redis:
    public static final String MEMORY = "Memory";

    //directory structure
//    public static final String DIR = System.getProperty("user.dir")+ File.separator+".."+File.separator;
    public static final String DIR = System.getProperty("easyproxy.home")+File.separator;
    public static final String CONF = DIR+"conf"+File.separator;
    public static final String LOGS = DIR+"logs"+File.separator;
    public static final String TMP = DIR+"tmp"+File.separator;
    public static final String RESOURCES = DIR+"resources"+File.separator;
    public static final String PERSONAL_PAGE = RESOURCES+"static"+File.separator;

    //file name
    public static final String ACCESSLOG = LOGS+"access.log";
    public static final String NULLVALUE = "-";
    //配置文件前缀名
    public static final String CONFIGNAME = "proxy";
    //配置文件后缀名
    public static final String XMLCONFIG = "xml";
    public static final String PROPCONFIG = "properties";
    public static final String JSONCONFIG = "json";
    public static final String YAMLCONFIG = "yaml";

    //redis static key
    public static final String LIKE = "*";
    public static final String ACCESSRECORD = "-access_record";


    public static final int CODE_OK = 200;
    public static final int CODE_BADREQUEST= 400;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_NOTFOUND = 404;
    public static final int CODE_SERVERERROR = 500;


    public static final String API_ACK = "{'message':'action complete!','code':200}";

}
