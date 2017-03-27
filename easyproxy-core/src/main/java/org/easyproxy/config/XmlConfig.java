package org.easyproxy.config;/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午11:08
 */

import org.easyproxy.cache.CacheManager;
import org.easyproxy.constants.Const;
import org.easyproxy.pojo.AccessRecord;
import org.easyproxy.pojo.WeightHost;
import org.easyproxy.util.codec.EncryptUtil;
import org.easyproxy.util.struct.JSONUtil;
import org.easyproxy.util.struct.XmlUtil;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;


/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午11:08
 */

public class XmlConfig extends Config {
    private XmlUtil util;

    public XmlConfig(String path) {
        configPath = path;
        util = new XmlUtil(path);
        params = JSONUtil.str2Json(util.xml2Json());
        init();
    }

    public XmlConfig(InputStream is) {
        util = new XmlUtil(is);
        System.out.println("param: " + util.xml2Json());
        params = JSONUtil.str2Json(util.xml2Json());
        init();
    }
    @Override
    public void configLoadbalanceStrategy(){
        List ips = JSONUtil.getListFromJson(Const.IP, params);
        List ports = JSONUtil.getListFromJson(Const.PORT, params);
        List weights = JSONUtil.getListFromJson(Const.WEIGHT, params);
        if (ips.size()==0){
            params.put(Const.ISPROXY,false);
            return;
        }
        params.put(Const.ISPROXY,true);
        //先把权重和IP 端口相关信息记录到内存（各个List）中，记录总权重
        for (int index = 0; index < ips.size(); index++) {
            String ip = (String) ips.get(index);
            int port = (int) ports.get(index);
            int weight = (int) weights.get(index);
            InetSocketAddress address = new InetSocketAddress(ip,port);
            WeightHost whost = new WeightHost(address,weight<=0?1:weight);
            weightHosts.add(whost);
            roundrobinHosts.add(address);
        }
        Collections.sort(weightHosts);
        Collections.reverse(weightHosts);
        maxWeight = weightHosts.get(0).getWeight();
        gcd = getMaxDivisor(weightHosts);
    }

    @Override
    public void configForbiddenHosts(){
        List array = JSONUtil.getListFromJson(Const.FILTERIP, params);
        for (int index=0;index<array.size();index++){
            forbiddenHosts.add((String) array.get(index));
        }
    }
    @Override
    public InetSocketAddress roundRobin() {
        InetSocketAddress address = roundrobinHosts
                .get(rrindex.get() % roundrobinHosts.size());
        rrindex.incrementAndGet();
        return address;
    }

    @Override
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

    @Override
    public InetSocketAddress ipHash(String ip) {
        long hash = EncryptUtil.ipHash(ip);
        InetSocketAddress address = roundrobinHosts
                .get((int) hash % roundrobinHosts.size());
        return address;
    }

    @Override
    public InetSocketAddress leastConnect() {
        AccessRecord minRecord = Collections.min(CacheManager.getCache().getAllAccessRecord());
        String key = minRecord.getKey();
        String[] hostport = key.split("-")[0].split(":");
        return new InetSocketAddress(hostport[0], Integer.parseInt(hostport[1]));
    }

    @Override
    public String getString(String param) {
        if (params == null)
            return "";
        return typeMapper.get(this.getClass()).getString(param);
    }

    @Override
    public Integer getInt(String param) {
        if (params == null)
            return 0;
        return typeMapper.get(this.getClass()).getIntValue(param);
    }

    @Override
    public List<String> getForbiddenHosts() {
        return forbiddenHosts;
    }

    @Override
    public void setLBStrategy(String strategy) {
        params.put(Const.LB_STRATEGY, strategy);
    }

    @Override
    public String getConfigPath() {
        return configPath;
    }

}
