package org.easyproxy.config;/**
 * Description :
 * Created by YangZH on 16-12-16
 * 下午8:27
 */

import com.alibaba.fastjson.JSONObject;
import org.easyproxy.cache.DefaultCache;
import org.easyproxy.pojo.WeightHost;
import org.easyproxy.util.struct.JSONUtil;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.easyproxy.constants.Const.*;

/**
 * Description : 
 * Created by code4j on 16-12-16
 *  下午8:27
 */

abstract public class Config {

    protected Map<Class<? extends Config>,JSONObject> typeMapper = new HashMap<>();

    protected List<InetSocketAddress> roundrobin_hosts = new CopyOnWriteArrayList<InetSocketAddress>();
    //weight host list
//    protected  List<WeightHost> weight_hosts_list = new CopyOnWriteArrayList<WeightHost>();
    protected List<WeightHost> weight_hosts = new ArrayList<WeightHost>();
    //ip_filter
    protected List<String> forbidden_hosts = new CopyOnWriteArrayList<String>();
    protected AtomicInteger index = new AtomicInteger(-1);
    protected int cw = 0;
    protected int gcd = 0;
    protected int maxWeight = 0;

    protected AtomicInteger rrindex = new AtomicInteger(0);
    protected DefaultCache cache;
    protected JSONObject params;

    protected String configPath;

    protected void init() {
        typeMapper.put(this.getClass(),params);
        configLoadbalanceStrategy();
        configForbiddenHosts();
    }

    abstract public void configLoadbalanceStrategy();

    abstract public void configForbiddenHosts();

    abstract public InetSocketAddress roundRobin();

    abstract public InetSocketAddress weight();

    abstract public InetSocketAddress ip_hash(String ip);

    abstract public InetSocketAddress least_connect();

    abstract public String getString(String param);

    abstract public Integer getInt(String param);

    abstract public List<String> getForbiddenHosts();

    abstract public void setLB_Strategy(String strategy);

    abstract public String getConfigPath();

    public List<InetSocketAddress> getServers(){
        return roundrobin_hosts;
    }

    /**
     * 动态添加节点
     * @param hosts
     */
    public void setNodes(List<WeightHost> hosts) {
        List<Map<String, Object>> hostsmap = new CopyOnWriteArrayList<Map<String, Object>>();
        for (WeightHost host : hosts) {
            InetSocketAddress address = host.getAddress();
            int weight = host.getWeight();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(HOST, address.getHostName());
            map.put(PORT, address.getPort());
            map.put(WEIGHT, weight);
            hostsmap.add(map);
        }
        params.put(PROXY_PASS, JSONUtil.list2Json(hostsmap));
        configLoadbalanceStrategy();
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
