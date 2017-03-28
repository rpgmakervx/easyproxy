package org.easyproxy.config;/**
 * Description :
 * Created by YangZH on 16-12-16
 * 下午8:27
 */

import com.alibaba.fastjson.JSONObject;
import org.easyproxy.cache.DefaultCache;
import org.easyproxy.constants.LBStrategy;
import org.easyproxy.pojo.WeightHost;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description : 
 * Created by code4j on 16-12-16
 *  下午8:27
 */

abstract public class Config {

    protected Map<Class<? extends Config>,JSONObject> typeMapper = new HashMap<>();

    protected List<InetSocketAddress> roundrobinHosts = new CopyOnWriteArrayList<InetSocketAddress>();
    //weight host list
//    protected  List<WeightHost> weight_hosts_list = new CopyOnWriteArrayList<WeightHost>();
    protected List<WeightHost> weightHosts = new ArrayList<WeightHost>();
    //ip_filter
    protected List<String> forbiddenHosts = new CopyOnWriteArrayList<String>();
    protected AtomicInteger index = new AtomicInteger(-1);
    protected int cw = 0;
    protected int gcd = 0;
    protected int maxWeight = 0;

    protected AtomicInteger rrindex = new AtomicInteger(0);
    protected DefaultCache cache;
    protected JSONObject params;

    protected String configPath;

    protected void init() {
        System.out.println("params --> \n"+params);
        typeMapper.put(this.getClass(),params);
        configLoadbalanceStrategy();
        configForbiddenHosts();
    }

    abstract public void configLoadbalanceStrategy();

    abstract public void configForbiddenHosts();

    abstract public InetSocketAddress roundRobin();

    abstract public InetSocketAddress weight();

    abstract public InetSocketAddress ipHash(String ip);

    abstract public InetSocketAddress leastConnect();

    abstract public String getString(String param);

    abstract public Integer getInt(String param);

    abstract public List<String> getForbiddenHosts();

    abstract public void setLBStrategy(String strategy);

    abstract public LBStrategy getLBStrategy();

    abstract public String getConfigPath();

    public List<InetSocketAddress> getServers(){
        return roundrobinHosts;
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
