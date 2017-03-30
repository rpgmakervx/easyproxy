package org.easyproxy.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * Created by xingtianyu on 16-12-12
 * 下午8:08
 */

public enum ConfigEnum {

    LISTEN("proxy.server.listen", 9999, "服务器监听的端口"),
    LOCALHOST("proxy.server.localhost", "", "服务器ip地址"),
    LB_STRATEGY("proxy.server.lb_strategy", "roundrobin", "负载均衡策略"),
    NODES("proxy.server.nodes", "", "负载节点ip"),

    BACKLOG("proxy.tcp.backlog", 512, "tcp backlog"),
    NODELAY("proxy.tcp.nodely", true, "tcp nodely"),
    REUSEADDR("proxy.tcp.reuseaddr", true, "tcp reuse addr"),
    KEEPALIVE("proxy.tcp.keepalive", true, "tcp keepalive"),
    SOLINGER("proxy.tcp.solinger", -1, "tcp solinger"),
    SNDBUF("proxy.tcp.sndbuf", 4096, "tcp send buffer"),
    RCVBUF("proxy.tcp.rcvbuf", 1024, "tcp recieve buffer"),

    CACHE_OPEN("proxy.cache.open", false, "缓存是否打开"),
    CACHE_TTL("proxy.cache.ttl", 30, "缓存失效时间"),
    CACHE_TYPE("proxy.cache.type", "redis", "缓存类型"),

    STATIC_URI("proxy.resource.staticUri", "/easyproxy/.*", "静态资源uri"),
    NOTFOUND_PAGE("proxy.resource.notfoundPage", "404page.html", "404页面"),
    BADREQUEST_PAGE("proxy.resource.badRequestPage", "badrequest.html", "400页面"),
    FORBIDDEN_PAGE("proxy.resource.forbidPage", "forbidden.html", "禁止访问页面"),
    ERROR_PAGE("proxy.resource.errorPage", "error.html", "服务端错误页面"),

    LOG_OPEN("proxy.log.logopen", false, "accesslog是否打开"),

    ANTILEECH_OPEN("proxy.antileech.open", false, "防盗链是否打开"),

    FIREWALL_OPEN("proxy.firewall.open", false, "防火墙是否打开"),
    FIREWALL_FILTER("proxy.firewall.filter", "", "过滤的ip组"),

    API_OPEN("proxy.api.open", false, "easyproxy的api接口"),
    APIURI("proxy.api.uri", "", "easyproxy的api接口");

    public String key;
    public Object defVal;
    public String note;

    ConfigEnum(String key, Object defVal, String note) {
        this.key = key;
        this.defVal = defVal;
        this.note = note;
    }

    public List<String> keys() {
        List<String> keys = new ArrayList<>();
        for (ConfigEnum enums : ConfigEnum.values()) {
            keys.add(enums.key);
        }
        return keys;
    }

    public static Object getVal(String key){
        ConfigEnum[] enums = ConfigEnum.values();
        for (ConfigEnum en : enums){
            if (en.key.equals(key)){
                return en.defVal;
            }
        }
        return "";
    }

    public static ConfigEnum getEnum(String key){
        for (ConfigEnum enums : ConfigEnum.values()){
            if (enums.key.equals(key)){
                return enums;
            }
        }
        return null;
    }
}
