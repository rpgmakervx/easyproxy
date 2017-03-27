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

    public static final String IMAGE = "(png|ico|gif|jpg|jpeg|bmp|swf|swf)";

    //http header params
    public static final String TEXT_HTML = "text/html";
    public static final String HEADERS = "headers";



    public static final String XML_CONFIGFILE = "proxy.xml";
    public static final String DEFAULT_CONFIGFILE = "proxy.properties";

    //real server settings
    //proxy_server 配置文件不存在，是检测到proxy_pass下没有配置后中间件自己设置的
    public static final String WEIGHT = "weight";
    public static final String IP = "ip";
    public static final String PORT = "port";
    public static final String FILTERIP = "filterip";


    public static final String ISPROXY = "isproxy";

    //cache_strategy
    //lb_strategy
    public static final String LB_STRATEGY = "proxy.server.lb_strategy";
    public static final String ROUNDROBIN = "roundrobin";
    public static final String WEIGHT_ROUNDROBIN = "weight";
    public static final String IP_HASH = "iphash";
    public static final String LEAST_CONNECT = "least_connection";
    //cache_type
    public static final String REDIS = "redis";
    public static final String EHCACHE = "ehcache";
    public static final String JAVA = "java";


    //redis:
    public static final String MEMORY = "Memory";

    //directory structure
//    public static final String DIR = System.getProperty("user.dir")+ File.separator+".."+File.separator;
    public static final String DIR = System.getProperty("easyproxy.home")+File.separator;
    public static final String CONF = DIR+"conf"+File.separator;
    public static final String LOGS = DIR+"logs"+File.separator;
    public static final String TMP = DIR+"tmp"+File.separator;
    public static final String RESOURCES = DIR+"resources"+File.separator;

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
