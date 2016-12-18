package org.easyproxy.config;/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午11:08
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.easyproxy.cache.CacheManager;
import org.easyproxy.pojo.AccessRecord;
import org.easyproxy.pojo.WeightHost;
import org.easyproxy.util.codec.EncryptUtil;
import org.easyproxy.util.struct.JSONUtil;
import org.easyproxy.util.struct.XmlUtil;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

import static org.easyproxy.constants.Const.*;

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
    public void configLoadbalanceStrategy(){
        JSONArray array = JSONUtil.getArrayFromJSON(PROXY_PASS, params);
        if (array.size()==0){
            params.put(PROXY_SERVER,false);
            return;
        }
        params.put(PROXY_SERVER,true);
        //先把权重和IP 端口相关信息记录到内存（各个List）中，记录总权重
        for (int index = 0; index < array.size(); index++) {
            JSONObject object = array.getJSONObject(index);
            int weight = object.getIntValue(WEIGHT);
            int port = object.getIntValue(PORT);
            String host = object.getString(HOST);
            InetSocketAddress address = new InetSocketAddress(host,port);
            WeightHost whost = new WeightHost(address,weight<=0?1:weight);
            weight_hosts.add(whost);
            roundrobin_hosts.add(address);
        }
        Collections.sort(weight_hosts);
        Collections.reverse(weight_hosts);
        maxWeight = weight_hosts.get(0).getWeight();
        gcd = getMaxDivisor(weight_hosts);
    }

    public void configForbiddenHosts(){
        JSONArray array = JSONUtil.getArrayFromJSON(IP_FILTER, params);
        for (int index=0;index<array.size();index++){
            JSONObject object = array.getJSONObject(index);
            forbidden_hosts.add(object.getString(FILTERED_IP));
        }
    }
    @Override
    public InetSocketAddress roundRobin() {
        InetSocketAddress address = roundrobin_hosts
                .get(rrindex.get() % roundrobin_hosts.size());
        rrindex.incrementAndGet();
        return address;
    }

    @Override
    public InetSocketAddress weight() {
        InetSocketAddress address = null;
        while (true) {
            index.set((index.get() + 1) % weight_hosts.size());
            if (index.get() == 0) {
                cw = cw - gcd;
                if (cw <= 0) {
                    cw = maxWeight;
                    if (cw == 0)
                        break;
                }
            }
            if (weight_hosts.get(index.get()).getWeight() >= cw) {
                address = weight_hosts.get(index.get()).getAddress();
                break;
            }
        }
        if (address == null) {
            address = weight_hosts.get(0).getAddress();
            System.out.println("weight负载均衡失败");
        }
        return address;
    }

    @Override
    public InetSocketAddress ip_hash(String ip) {
        long hash = EncryptUtil.ip_hash(ip);
        InetSocketAddress address = roundrobin_hosts
                .get((int) hash % roundrobin_hosts.size());
        return address;
    }

    @Override
    public InetSocketAddress least_connect() {
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
        return forbidden_hosts;
    }

    @Override
    public void setLB_Strategy(String strategy) {
        params.put(LB_STRATEGY, strategy);
    }

    @Override
    public String getConfigPath() {
        return configPath;
    }

}