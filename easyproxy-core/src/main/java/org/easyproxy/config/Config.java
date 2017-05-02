package org.easyproxy.config;/**
 * Description :
 * Created by YangZH on 16-12-16
 * 下午8:27
 */

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.easyproxy.api.app.pojo.HostVO;
import org.easyproxy.cache.CacheManager;
import org.easyproxy.cache.CacheType;
import org.easyproxy.cache.DefaultCache;
import org.easyproxy.constants.Const;
import org.easyproxy.constants.LBStrategy;
import org.easyproxy.pojo.AccessRecord;
import org.easyproxy.pojo.ConfigEntity;
import org.easyproxy.pojo.WeightHost;
import org.easyproxy.util.codec.EncryptUtil;
import org.easyproxy.util.struct.JSONUtil;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description :
 * Created by code4j on 16-12-16
 * 下午8:27
 */

abstract public class Config {

    protected Map<Class<? extends Config>, JSONObject> typeMapper = new HashMap<>();

    protected List<InetSocketAddress> roundrobinHosts = new CopyOnWriteArrayList<InetSocketAddress>();
    protected List<WeightHost> weightHosts = new ArrayList<WeightHost>();
    //ip_filter
    protected Set<String> forbiddenHosts = new CopyOnWriteArraySet<>();
    protected AtomicInteger index = new AtomicInteger(-1);
    protected int cw = 0;
    protected int gcd = 0;
    protected int maxWeight = 0;

    protected AtomicInteger rrindex = new AtomicInteger(0);
    protected DefaultCache cache;
    protected JSONObject params;

    protected String configPath;

    protected void init() {
        System.out.println("params --> \n" + params);
        typeMapper.put(this.getClass(), params);
        configLoadbalanceStrategy();
        configForbiddenHosts();
    }

    public void configLoadbalanceStrategy() {
        List ips = JSONUtil.getListFromJson(Const.IP, params);
        List ports = JSONUtil.getListFromJson(Const.PORT, params);
        List weights = JSONUtil.getListFromJson(Const.WEIGHT, params);
        if (ips.size() == 0) {
            params.put(Const.ISPROXY, false);
            return;
        }
        params.put(Const.ISPROXY, true);
        //先把权重和IP 端口相关信息记录到内存（各个List）中，记录总权重
        for (int index = 0; index < ips.size(); index++) {
            String ip = (String) ips.get(index);
            int port = (int) ports.get(index);
            int weight = (int) weights.get(index);
            InetSocketAddress address = new InetSocketAddress(ip, port);
            WeightHost whost = new WeightHost(address, weight <= 0 ? 1 : weight);
            weightHosts.add(whost);
            roundrobinHosts.add(address);
        }
        Collections.sort(weightHosts);
        Collections.reverse(weightHosts);
        maxWeight = weightHosts.get(0).getWeight();
        gcd = getMaxDivisor(weightHosts);
    }

    public void configForbiddenHosts() {
        List array = JSONUtil.getListFromJson(ConfigEnum.FIREWALL_FILTER.key, params);
        for (int index = 0; index < array.size(); index++) {
            forbiddenHosts.add((String) array.get(index));
        }
    }

    public InetSocketAddress roundRobin() {
        InetSocketAddress address = roundrobinHosts
                .get(rrindex.get() % roundrobinHosts.size());
        rrindex.incrementAndGet();
        return address;
    }

    public InetSocketAddress weight() {
        InetSocketAddress address = null;
        while (true) {
            index.set((index.get() + 1) % weightHosts.size());
            if (index.get() == 0) {
                cw = cw - gcd;
                if (cw <= 0) {
                    cw = maxWeight;
                    if (cw == 0)
                        break;
                }
            }
            if (weightHosts.get(index.get()).getWeight() >= cw) {
                address = weightHosts.get(index.get()).getAddress();
                break;
            }
        }
        if (address == null) {
            address = weightHosts.get(0).getAddress();
            System.out.println("weight负载均衡失败");
        }
        return address;
    }

    public InetSocketAddress ipHash(String ip) {
        long hash = EncryptUtil.ipHash(ip);
        InetSocketAddress address = roundrobinHosts
                .get((int) hash % roundrobinHosts.size());
        return address;
    }

    public InetSocketAddress leastConnect() {
        AccessRecord minRecord = Collections.min(CacheManager.getCache().getAllAccessRecord());
        System.out.println(Thread.currentThread().getName() + " : AccessRecord--------: " + minRecord);
        String key = minRecord.getKey();
        String[] hostport = key.split("-")[0].split(":");
        return new InetSocketAddress(hostport[0], Integer.parseInt(hostport[1]));
    }

    abstract public String getString(String param);

    abstract public Boolean getBoolean(String param);

    abstract public Integer getInt(String param);

    public Set<String> getForbiddenHosts() {
        return forbiddenHosts;
    }

    public List<HostVO> getLBHosts() {
        List<HostVO> hosts = new ArrayList<>();
        for (WeightHost host : weightHosts) {
            hosts.add(new HostVO(host));
        }
        return hosts;
    }

    abstract public void setLBStrategy(String strategy);

    abstract public LBStrategy getLBStrategy();

    abstract public String getConfigPath();

    public List<InetSocketAddress> getServers() {
        return roundrobinHosts;
    }

    public void buildConfig(ConfigEntity entity) {
        setPort(entity.getPort());
        setStrategy(entity.getStrategy());
        setNodes(entity.getNodes());
        setCacheOpen(entity.getCacheOpen());
        setCacheTTL(entity.getCacheTTL());
        setCacheType(entity.getCacheType());
        setStaticUrl(entity.getStaticUrl());
        setNotFoundPage(entity.getNotFoundPage());
        setBadRequestPage(entity.getBadRequestPage());
        setForbidPage(entity.getForbidPage());
        setErrorPage(entity.getErrorPage());
        setApiOpen(entity.getApiOpen());
        setLogOpen(entity.getLogOpen());
        setAntiLeechOpen(entity.getAntiLeechOpen());
        setFireWallOpen(entity.getFireWallOpen());
        setBlackList(entity.getBlackList());
        setBackLog(entity.getBackLog());
        setNoDely(entity.getNoDely());
        setReuseAddress(entity.getReuseAddress());
        setKeepAlive(entity.getKeepAlive());
        setSoLinger(entity.getSoLinger());
        setSendBuffer(entity.getSendBuffer());
        setRecieveBuffer(entity.getRecieveBuffer());
    }

    public void setPort(Integer port) {
        if (port != null) {
            params.put(ConfigEnum.LISTEN.key, port);
        }
    }

    public void setStrategy(LBStrategy strategy) {
        if (strategy != null) {
            params.put(ConfigEnum.LB_STRATEGY.key, strategy.key);
        }
    }

    public void setCacheOpen(Boolean cacheOpen) {
        if (cacheOpen != null) {
            params.put(ConfigEnum.CACHE_OPEN.key, cacheOpen);
        }
    }

    public void setCacheTTL(Integer cacheTTL) {
        if (cacheTTL != null) {
            params.put(ConfigEnum.CACHE_TTL.key, cacheTTL);
        }
    }

    public void setCacheType(CacheType cacheType) {
        if (cacheType != null) {
            params.put(ConfigEnum.CACHE_TYPE.key, cacheType);
        }
    }

    public void setStaticUrl(String staticUrl) {
        if (StringUtils.isNotEmpty(staticUrl)) {
            params.put(ConfigEnum.STATIC_URI.key, staticUrl);
            System.out.println("put static as:" + staticUrl);
            String val = typeMapper.get(PropertyConfig.class).getString(ConfigEnum.STATIC_URI.key);
            System.out.println("val static as:" + val);
        }
    }

    public void setNotFoundPage(String notFoundPage) {
        if (StringUtils.isNotEmpty(notFoundPage)) {
            params.put(ConfigEnum.NOTFOUND_PAGE.key, notFoundPage);
        }
    }

    public void setBadRequestPage(String badRequestPage) {
        if (StringUtils.isNotEmpty(badRequestPage)) {
            params.put(ConfigEnum.BADREQUEST_PAGE.key, badRequestPage);
        }
    }

    public void setForbidPage(String forbidPage) {
        if (StringUtils.isNotEmpty(forbidPage)) {
            params.put(ConfigEnum.FORBIDDEN_PAGE.key, forbidPage);
        }
    }

    public void setErrorPage(String errorPage) {
        if (StringUtils.isNotEmpty(errorPage)) {
            params.put(ConfigEnum.ERROR_PAGE.key, errorPage);
        }
    }

    public void setApiOpen(Boolean apiOpen) {
        if (apiOpen != null) {
            params.put(ConfigEnum.API_OPEN.key, apiOpen);
        }
    }

    public void setLogOpen(Boolean logOpen) {
        if (logOpen != null) {
            params.put(ConfigEnum.LOG_OPEN.key, logOpen);
        }
    }

    public void setAntiLeechOpen(Boolean antiLeechOpen) {
        if (antiLeechOpen != null) {
            params.put(ConfigEnum.ANTILEECH_OPEN.key, antiLeechOpen);
        }
    }

    public void setFireWallOpen(Boolean fireWallOpen) {
        if (fireWallOpen != null) {
            params.put(ConfigEnum.FIREWALL_OPEN.key, fireWallOpen);
        }
    }

    public void setBlackList(List<String> blackList) {
        if (CollectionUtils.isNotEmpty(blackList)) {
            params.put(ConfigEnum.FIREWALL_FILTER.key, blackList);
            forbiddenHosts.clear();
            forbiddenHosts.addAll(blackList);
        }
    }

    public void setNodes(List<WeightHost> nodes) {
        if (CollectionUtils.isNotEmpty(nodes)) {
            weightHosts.clear();
            roundrobinHosts.clear();
            System.out.println("set nodes");
            List<String> iplist = new ArrayList<>();
            List<Integer> weightlist = new ArrayList<>();
            List<Integer> portlist = new ArrayList<>();
            for (WeightHost host : nodes) {
                InetSocketAddress address = host.getAddress();
                iplist.add(address.getHostString());
                portlist.add(address.getPort());
                weightlist.add(host.getWeight());
                weightHosts.add(host);
                roundrobinHosts.add(new InetSocketAddress(
                        address.getHostString(), address.getPort()));
            }
            params.put(Const.IP, iplist);
            params.put(Const.PORT, portlist);
            params.put(Const.WEIGHT, weightlist);
        }
    }

    public void setBackLog(Integer backLog) {
        if (backLog != null) {
            params.put(ConfigEnum.BACKLOG.key, backLog);
        }
    }

    public void setNoDely(Boolean noDely) {
        if (noDely != null) {
            params.put(ConfigEnum.NODELAY.key, noDely);
        }
    }

    public void setReuseAddress(Boolean reuseAddress) {
        if (reuseAddress != null) {
            params.put(ConfigEnum.REUSEADDR.key, reuseAddress);
        }
    }

    public void setKeepAlive(Boolean keepAlive) {
        if (keepAlive != null) {
            params.put(ConfigEnum.KEEPALIVE.key, keepAlive);
        }
    }

    public void setSoLinger(Integer soLinger) {
        if (soLinger != null) {
            params.put(ConfigEnum.SOLINGER.key, soLinger);
        }
    }

    public void setSendBuffer(Integer sendBuffer) {
        if (sendBuffer != null) {
            params.put(ConfigEnum.SNDBUF.key, sendBuffer);
        }
    }

    public void setRecieveBuffer(Integer recieveBuffer) {
        if (recieveBuffer != null) {
            params.put(ConfigEnum.RCVBUF.key, recieveBuffer);
        }
    }

    public ConfigEntity getConfigEntity() {
        ConfigEntity entity = new ConfigEntity();
        if (params.get(ConfigEnum.LISTEN.key) == null) {
            entity.setPort(Integer.parseInt(
                    String.valueOf(ConfigEnum.LISTEN.defVal)));
        } else {
            entity.setPort(Integer.parseInt(
                    String.valueOf(params.get(ConfigEnum.LISTEN.key))));
        }
        if (params.get(ConfigEnum.LB_STRATEGY.key) == null) {
            entity.setPort(Integer.parseInt(
                    String.valueOf(ConfigEnum.LB_STRATEGY.defVal)));
        } else {
            entity.setStrategy(LBStrategy.getStrategy(
                    String.valueOf(params.get(ConfigEnum.LB_STRATEGY.key))));
        }
        if (params.get(ConfigEnum.CACHE_OPEN.key) == null) {
            entity.setCacheOpen(Boolean.valueOf(
                    String.valueOf(ConfigEnum.CACHE_OPEN.defVal)));
        } else {
            entity.setCacheOpen(Boolean.valueOf(
                    String.valueOf(params.get(ConfigEnum.CACHE_OPEN.key))));
        }
        if (params.get(ConfigEnum.CACHE_TTL.key) == null) {
            entity.setCacheTTL(Integer.parseInt(
                    String.valueOf(ConfigEnum.CACHE_TTL.defVal)));
        } else {
            entity.setCacheTTL(Integer.parseInt(
                    String.valueOf(params.get(ConfigEnum.CACHE_TTL.key))));
        }
        if (params.get(ConfigEnum.CACHE_TYPE.key) == null) {
            entity.setCacheType(CacheType.getCache(
                    String.valueOf(ConfigEnum.CACHE_TYPE.defVal)));
        } else {
            entity.setCacheType(CacheType.getCache(
                    String.valueOf(params.get(ConfigEnum.CACHE_TYPE.key))));
        }
        if (params.get(ConfigEnum.STATIC_URI.key) == null) {
            entity.setStaticUrl(String.valueOf(ConfigEnum.STATIC_URI.defVal));
        } else {
            entity.setStaticUrl(String.valueOf(params.get(ConfigEnum.STATIC_URI.key)));
        }
        if (params.get(ConfigEnum.NOTFOUND_PAGE.key) == null) {
            entity.setNotFoundPage(String.valueOf(ConfigEnum.NOTFOUND_PAGE.defVal));
        } else {
            entity.setNotFoundPage(String.valueOf(params.get(ConfigEnum.NOTFOUND_PAGE.key)));
        }
        if (params.get(ConfigEnum.BADREQUEST_PAGE.key) == null){
            entity.setBadRequestPage(String.valueOf(ConfigEnum.BADREQUEST_PAGE.defVal));
        }else{
            entity.setBadRequestPage(String.valueOf(params.get(ConfigEnum.BADREQUEST_PAGE.key)));
        }
        if (params.get(ConfigEnum.FORBIDDEN_PAGE.key) == null){
            entity.setForbidPage(String.valueOf(ConfigEnum.FORBIDDEN_PAGE.defVal));
        }else{
            entity.setBadRequestPage(String.valueOf(params.get(ConfigEnum.FORBIDDEN_PAGE.key)));
        }
        if (params.get(ConfigEnum.ERROR_PAGE.key) == null){
            entity.setErrorPage(String.valueOf(ConfigEnum.ERROR_PAGE.defVal));
        }else{
            entity.setErrorPage(String.valueOf(params.get(ConfigEnum.ERROR_PAGE.key)));
        }
        if (params.get(ConfigEnum.API_OPEN.key) == null) {
            entity.setApiOpen(Boolean.valueOf(
                    String.valueOf(ConfigEnum.API_OPEN.defVal)));
        }else{
            entity.setApiOpen(Boolean.valueOf(
                    String.valueOf(params.get(ConfigEnum.API_OPEN.key))));
        }
        if (params.get(ConfigEnum.LOG_OPEN.key) == null){
            entity.setLogOpen(Boolean.valueOf(
                    String.valueOf(ConfigEnum.LOG_OPEN.defVal)));
        }else{
            entity.setLogOpen(Boolean.valueOf(
                    String.valueOf(params.get(ConfigEnum.LOG_OPEN.key))));
        }
        if (params.get(ConfigEnum.ANTILEECH_OPEN.key) == null){
            entity.setAntiLeechOpen(Boolean.valueOf(
                    String.valueOf(ConfigEnum.ANTILEECH_OPEN.defVal)));
        }else{
            entity.setAntiLeechOpen(Boolean.valueOf(
                    String.valueOf(params.get(ConfigEnum.ANTILEECH_OPEN.key))));
        }
        if (params.get(ConfigEnum.FIREWALL_OPEN.key) == null){
            entity.setFireWallOpen(Boolean.valueOf(
                    String.valueOf(ConfigEnum.FIREWALL_OPEN.defVal)));
        }else{
            entity.setFireWallOpen(Boolean.valueOf(
                    String.valueOf(params.get(ConfigEnum.FIREWALL_OPEN.key))));
        }
        if (params.get(ConfigEnum.BACKLOG.key) == null){
            entity.setBackLog(Integer.parseInt(
                    String.valueOf(ConfigEnum.BACKLOG.defVal)));
        }else{
            entity.setBackLog(Integer.parseInt(
                    String.valueOf(params.get(ConfigEnum.BACKLOG.key))));
        }
        if (params.get(ConfigEnum.NODELAY.key) == null){
            entity.setNoDely(Boolean.valueOf(
                    String.valueOf(ConfigEnum.NODELAY.defVal)));
        }else{
            entity.setNoDely(Boolean.valueOf(
                    String.valueOf(params.get(ConfigEnum.NODELAY.key))));
        }
        if (params.get(ConfigEnum.REUSEADDR.key) == null){
            entity.setReuseAddress(Boolean.valueOf(
                    String.valueOf(ConfigEnum.REUSEADDR.defVal)));
        }else{
            entity.setReuseAddress(Boolean.valueOf(
                    String.valueOf(params.get(ConfigEnum.REUSEADDR.key))));
        }
        if (params.get(ConfigEnum.KEEPALIVE.key) == null){
            entity.setKeepAlive(Boolean.valueOf(
                    String.valueOf(ConfigEnum.KEEPALIVE.defVal)));
        }else{
            entity.setKeepAlive(Boolean.valueOf(
                    String.valueOf(params.get(ConfigEnum.KEEPALIVE.key))));
        }
        if (params.get(ConfigEnum.SOLINGER.key) == null){
            entity.setSoLinger(Integer.valueOf(
                    String.valueOf(ConfigEnum.SOLINGER.defVal)));
        }else{
            entity.setSoLinger(Integer.valueOf(
                    String.valueOf(params.get(ConfigEnum.SOLINGER.key))));
        }
        if (params.get(ConfigEnum.SNDBUF.key) == null){
            entity.setSendBuffer(Integer.valueOf(
                    String.valueOf(ConfigEnum.SNDBUF.defVal)));
        }else{
            entity.setSendBuffer(Integer.valueOf(
                    String.valueOf(params.get(ConfigEnum.SNDBUF.key))));
        }
        if (params.get(ConfigEnum.RCVBUF.key) == null){
            entity.setRecieveBuffer(Integer.valueOf(
                    String.valueOf(ConfigEnum.RCVBUF.defVal)));
        }else{
            entity.setRecieveBuffer(Integer.valueOf(
                    String.valueOf(params.get(ConfigEnum.RCVBUF.key))));
        }
        return entity;
    }

    protected int getMaxDivisor(List<WeightHost> hosts) {
        int minN = Collections.min(hosts).getWeight();
        for (int j = minN; j >= 2; j--) {
            int count = 0;
            for (int i = 0; i < hosts.size(); i++) {
                if (hosts.get(i).getWeight() % j == 0) {
                    count++;
                }
            }
            if (count == hosts.size()) {
                return j;
            }
        }
        return -1;// 无最大公约数
    }


}
