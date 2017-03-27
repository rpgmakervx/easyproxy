package org.easyproxy.config;

import com.alibaba.fastjson.JSONObject;
import org.easyproxy.cache.CacheManager;
import org.easyproxy.pojo.AccessRecord;
import org.easyproxy.pojo.WeightHost;
import org.easyproxy.util.codec.EncryptUtil;
import org.easyproxy.util.struct.JSONUtil;
import org.easyproxy.util.struct.PropertiesUtil;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

import static org.easyproxy.config.ConfigEnum.FIREWALL_FILTER;
import static org.easyproxy.config.ConfigEnum.LB_STRATEGY;
import static org.easyproxy.constants.Const.*;


/**
 * Description :
 * Created by xingtianyu on 16-12-16
 * 下午8:29
 */

public class PropertyConfig extends Config {

    private PropertiesUtil util;

    public PropertyConfig(String path){
        configPath = path;
        util = new PropertiesUtil(path);
        params = new JSONObject(util.getConfigMap());
        init();
    }

    public PropertyConfig(InputStream is){
        util = new PropertiesUtil(is);
        params = new JSONObject(util.getConfigMap());
        init();
    }
    @Override
    public void configLoadbalanceStrategy(){
        List ips = JSONUtil.getListFromJson(IP, params);
        List ports = JSONUtil.getListFromJson(PORT, params);
        List weights = JSONUtil.getListFromJson(WEIGHT, params);
        if (ips.size()==0){
            params.put(ISPROXY,false);
            return;
        }
        params.put(ISPROXY,true);
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
        List array = JSONUtil.getListFromJson(FIREWALL_FILTER.key, params);
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
//        System.out.println("weight新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
        return address;
    }

    @Override
    public InetSocketAddress ipHash(String ip) {
        long hash = EncryptUtil.ipHash(ip);

        System.out.println("roundrobinHosts size:"+roundrobinHosts.size());
        System.out.println("ip hash:"+hash);
        System.out.println("result:"+((int) hash % roundrobinHosts.size()));
        InetSocketAddress address = roundrobinHosts
                .get((int) hash % roundrobinHosts.size());
//        System.out.println("ip_hash新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
        return address;
    }

    @Override
    public InetSocketAddress leastConnect() {
        AccessRecord minRecord = Collections.min(CacheManager.getCache().getAllAccessRecord());
        System.out.println(Thread.currentThread().getName() + " : AccessRecord--------: " + minRecord);
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
        params.put(LB_STRATEGY.key, strategy);
    }

    @Override
    public String getConfigPath() {
        return configPath;
    }

    public void listAll() {
        System.out.println(JSONObject.toJSONString(params));
    }

}
