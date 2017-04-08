package org.easyproxy.api.app.pojo;

import org.easyproxy.cache.CacheType;
import org.easyproxy.constants.LBStrategy;
import org.easyproxy.pojo.ConfigEntity;
import org.easyproxy.pojo.WeightHost;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingtianyu on 17-4-2
 * 上午11:56
 * description:
 */

public class ConfigVO {
    /**
     * 负载均衡器端口
     */
    private Integer port;
    /**
     * 负载均衡策略
     */
    private String strategy;
    /**
     * 所负载的子节点以及权重信息
     */
    private List<HostVO> nodes;
    /**
     * 缓存是否开启
     */
    private Boolean cacheOpen;
    /**
     * 缓存超时时长
     */
    private Integer cacheTTL;
    /**
     * 缓存类型
     */
    private CacheType cacheType;
    /**
     * 静态资源路径
     */
    private String staticUrl;
    /**
     * 404页面文件名
     */
    private String notFoundPage;
    /**
     * 400页面文件名
     */
    private String badRequestPage;
    /**
     * 403页面文件名
     */
    private String forbidPage;
    /**
     * 50x页面文件名
     */
    private String errorPage;
    /**
     * 是否开启api接口
     */
    private Boolean apiOpen;
    /**
     * 是否开启日志
     */
    private Boolean logOpen;
    /**
     * 防盗链开关
     */
    private Boolean antiLeechOpen;
    /**
     * 防火墙开关
     */
    private Boolean fireWallOpen;
    /**
     * 防火墙黑名单
     */
    private List<String> blackList ;
    /**
     * tcp back log
     */
    private Integer backLog;
    /**
     * tcp nodely(是否启用Nagle算法)
     */
    private Boolean noDely;
    /**
     * tcp reuse addr
     */
    private Boolean reuseAddress;
    /**
     * tcp keepalive
     */
    private Boolean keepAlive;
    /**
     * tcp solinger
     */
    private Integer soLinger;
    /**
     * tcp send buffer
     */
    private Integer sendBuffer;
    /**
     * tcp recieve buffer
     */
    private Integer recieveBuffer;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public List<HostVO> getNodes() {
        return nodes;
    }

    public void setNodes(List<HostVO> nodes) {
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

    public Integer getBackLog() {
        return backLog;
    }

    public void setBackLog(Integer backLog) {
        this.backLog = backLog;
    }

    public Boolean getNoDely() {
        return noDely;
    }

    public void setNoDely(Boolean noDely) {
        this.noDely = noDely;
    }

    public Boolean getReuseAddress() {
        return reuseAddress;
    }

    public void setReuseAddress(Boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Integer getSoLinger() {
        return soLinger;
    }

    public void setSoLinger(Integer soLinger) {
        this.soLinger = soLinger;
    }

    public Integer getSendBuffer() {
        return sendBuffer;
    }

    public void setSendBuffer(Integer sendBuffer) {
        this.sendBuffer = sendBuffer;
    }

    public Integer getRecieveBuffer() {
        return recieveBuffer;
    }

    public void setRecieveBuffer(Integer recieveBuffer) {
        this.recieveBuffer = recieveBuffer;
    }

    public ConfigEntity convert(){
        ConfigEntity entity = new ConfigEntity();
        entity.setPort(port);
        entity.setStrategy(LBStrategy.getStrategy(strategy));
        entity.setCacheOpen(cacheOpen);
        entity.setCacheTTL(cacheTTL);
        entity.setCacheType(cacheType);
        entity.setStaticUrl(staticUrl);
        entity.setNotFoundPage(notFoundPage);
        entity.setBadRequestPage(badRequestPage);
        entity.setErrorPage(errorPage);
        entity.setLogOpen(logOpen);
        entity.setAntiLeechOpen(antiLeechOpen);
        entity.setFireWallOpen(fireWallOpen);
        entity.setBlackList(blackList);
        entity.setBackLog(backLog);
        entity.setNoDely(noDely);
        entity.setKeepAlive(keepAlive);
        entity.setSoLinger(soLinger);
        entity.setSendBuffer(sendBuffer);
        entity.setReuseAddress(reuseAddress);
        entity.setRecieveBuffer(recieveBuffer);
        List<WeightHost> hosts = new ArrayList<>();
        if (nodes!= null){
            for (HostVO vo:nodes){
                InetSocketAddress address = new InetSocketAddress(vo.getIp(),vo.getPort());
                WeightHost host = new WeightHost(address,vo.getWeight());
                hosts.add(host);
            }
        }
        entity.setNodes(hosts);
        return entity;
    }

    @Override
    public String toString() {
        return "ConfigVO{" +
                "port=" + port +
                ", strategy='" + strategy + '\'' +
                ", nodes=" + nodes +
                ", cacheOpen=" + cacheOpen +
                ", cacheTTL=" + cacheTTL +
                ", cacheType=" + cacheType +
                ", staticUrl='" + staticUrl + '\'' +
                ", notFoundPage='" + notFoundPage + '\'' +
                ", badRequestPage='" + badRequestPage + '\'' +
                ", forbidPage='" + forbidPage + '\'' +
                ", errorPage='" + errorPage + '\'' +
                ", apiOpen=" + apiOpen +
                ", logOpen=" + logOpen +
                ", antiLeechOpen=" + antiLeechOpen +
                ", fireWallOpen=" + fireWallOpen +
                ", blackList=" + blackList +
                ", backLog=" + backLog +
                ", noDely=" + noDely +
                ", reuseAddress=" + reuseAddress +
                ", keepAlive=" + keepAlive +
                ", soLinger=" + soLinger +
                ", sendBuffer=" + sendBuffer +
                ", recieveBuffer=" + recieveBuffer +
                '}';
    }
}
