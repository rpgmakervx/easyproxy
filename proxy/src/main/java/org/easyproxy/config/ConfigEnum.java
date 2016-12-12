package org.easyproxy.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * Created by xingtianyu on 16-12-12
 * 下午8:08
 */

public enum ConfigEnum {

    LISTEN("proxy.server.listen", "9999", "服务器监听的端口"),
    LB_STRATEGY("proxy.server.lb_strategy", "roundrobin", "负载均衡策略"),
    NODE_IP("proxy.server.nodes.ip", "", "负载节点ip"),
    NODE_PORT("proxy.server.nodes.port", "", "负载节点端口号"),
    NODE_WEIGHT("proxy.server.nodes.weight", "", "负载节点权重"),
    CACHE_OPEN("proxy.cache.open", "false", "缓存是否打开"),
    CACHE_TTL("proxy.cache.cache_ttl", "30", "缓存失效时间"),
    CACHE_TYPE("proxy.cache.type", "redis", "缓存类型"),
    STATIC_URI("proxy.resource.static_uri", "/easyproxy/.*", "静态资源uri"),
    NOTFOUND_PAGE("proxy.resource.notfound_page", "404page.html", "404页面"),
    BADREQUEST_PAGE("proxy.resource.bad_request", "badrequest.html", "400页面"),
    FORBIDDEN_PAGE("proxy.resource.forbidden_page", "forbidden.html", "禁止访问页面"),
    LOG_OPEN("proxy.log.logopen", "false", "accesslog是否打开"),
    ANTILEECH_OPEN("proxy.antileech.open", "fasle", "防盗链是否打开"),
    FIREWALL_OPEN("proxy.firewall.open", "fasle", "防火墙是否打开"),
    FIREWALL_FILTER("proxy.firewall.filter", "", "过滤的ip组");

    public String key;
    public String defVal;
    public String note;

    ConfigEnum(String key, String defVal, String note) {
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
}
