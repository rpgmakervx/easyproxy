package org.easyproxy.pojo;

import org.easyproxy.cache.CacheType;
import org.easyproxy.constants.LBStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingtianyu on 17-3-30
 * 下午6:18
 * description:
 */

public class ConfigEntity {

    private Integer port;

    private LBStrategy strategy;

    private List<WeightHost> nodes;

    private Boolean cacheOpen;

    private Integer cacheTTL;

    private CacheType cacheType;

    private String staticUrl;

    private String notFoundPage;

    private String badRequestPage;

    private String forbidPage;

    private String errorPage;

    private Boolean apiOpen;

    private Boolean logOpen;

    private Boolean antiLeechOpen;

    private Boolean fireWallOpen;

    private List<String> blackList ;

    public ConfigEntity() {
        nodes = new ArrayList<>();
        blackList = new ArrayList<>();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public LBStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(LBStrategy strategy) {
        this.strategy = strategy;
    }

    public List<WeightHost> getNodes() {
        return nodes;
    }

    public void setNodes(List<WeightHost> nodes) {
        this.nodes = nodes;
    }

    public Boolean getCacheOpen() {
        return cacheOpen;
    }

    public void setCacheOpen(Boolean cacheOpen) {
        this.cacheOpen = cacheOpen;
    }

    public Integer getCacheTTL() {
        return cacheTTL;
    }

    public void setCacheTTL(Integer cacheTTL) {
        this.cacheTTL = cacheTTL;
    }

    public CacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
    }

    public String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }

    public String getNotFoundPage() {
        return notFoundPage;
    }

    public void setNotFoundPage(String notFoundPage) {
        this.notFoundPage = notFoundPage;
    }

    public String getBadRequestPage() {
        return badRequestPage;
    }

    public void setBadRequestPage(String badRequestPage) {
        this.badRequestPage = badRequestPage;
    }

    public String getForbidPage() {
        return forbidPage;
    }

    public void setForbidPage(String forbidPage) {
        this.forbidPage = forbidPage;
    }

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public Boolean getApiOpen() {
        return apiOpen;
    }

    public void setApiOpen(Boolean apiOpen) {
        this.apiOpen = apiOpen;
    }

    public Boolean getLogOpen() {
        return logOpen;
    }

    public void setLogOpen(Boolean logOpen) {
        this.logOpen = logOpen;
    }

    public Boolean getAntiLeechOpen() {
        return antiLeechOpen;
    }

    public void setAntiLeechOpen(Boolean antiLeechOpen) {
        this.antiLeechOpen = antiLeechOpen;
    }

    public Boolean getFireWallOpen() {
        return fireWallOpen;
    }

    public void setFireWallOpen(Boolean fireWallOpen) {
        this.fireWallOpen = fireWallOpen;
    }

    public List<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<String> blackList) {
        this.blackList = blackList;
    }

    public void addBlackList(String ip){
        this.blackList.add(ip);
    }

    public void addNode(WeightHost host){
        this.nodes.add(host);
    }
}
