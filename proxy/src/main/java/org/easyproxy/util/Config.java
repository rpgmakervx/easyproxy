package org.easyproxy.util;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午11:08
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.easyproxy.cache.Cache;
import org.easyproxy.pojo.AccessRecord;
import org.easyproxy.pojo.WeightHost;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.easyproxy.constants.Const.*;
/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午11:08
 */

public class Config {
    private static List<InetSocketAddress> roundrobin_hosts = new CopyOnWriteArrayList<InetSocketAddress>();
    //weight host list
//    private static List<WeightHost> weight_hosts_list = new CopyOnWriteArrayList<WeightHost>();
    private static List<WeightHost> weight_hosts = new ArrayList<WeightHost>();
    //ip_filter
    private static List<String> forbidden_hosts = new CopyOnWriteArrayList<String>();
    private static AtomicInteger index = new AtomicInteger(-1);
    private static int cw = 0;
    private static int gcd = 0;
    private static int maxWeight = 0;
    private XmlUtil xmlUtil;
    private static JSONObject params;
    private static int node_num = 0;
    private static AtomicInteger weight_rrindex = new AtomicInteger(0);
    private static AtomicInteger rrindex = new AtomicInteger(0);
    private static Cache cache ;
    public Config(String path) {
        xmlUtil = new XmlUtil(path);
        params = JSONUtil.str2Json(xmlUtil.xml2Json());
        cache = new Cache();
        init();
    }

    public Config(InputStream is){
        xmlUtil = new XmlUtil(is);
        System.out.println("param: "+xmlUtil.xml2Json());
        params = JSONUtil.str2Json(xmlUtil.xml2Json());
        cache = new Cache();
        init();
    }

    private void init() {
        configLoadbalanceStrategy();
        configForbiddenHosts();
        initCache();
    }

    private static void configLoadbalanceStrategy(){
        JSONArray array = JSONUtil.getArrayFromJSON(PROXY_PASS, params);
        //先把权重和IP 端口相关信息记录到内存（各个List）中，记录总权重
        for (int index = 0; index < array.size(); index++) {
            JSONObject object = array.getJSONObject(index);
            int weight = object.getIntValue(WEIGHT);
            int port = object.getIntValue(PORT);
            String host = object.getString(HOST);
            InetSocketAddress address = new InetSocketAddress(host,port);
            WeightHost whost = new WeightHost(address,weight);
            weight_hosts.add(whost);
            roundrobin_hosts.add(address);
        }
        Collections.sort(weight_hosts);
        Collections.reverse(weight_hosts);
        maxWeight = weight_hosts.get(0).getWeight();
        gcd = getMaxDivisor(weight_hosts);

        //权重可能会被用户设置的过高，这时手动降低权重值的量级，维持在10以内，并把权重记录更新到权重list中
//        if (weight_sum > 20) {
//            int new_weight = 0;
//            for (WeightHost host:weight_hosts){
//                float percent = host.getWeight() / (float) weight_sum;
//                int weight = 1;
//                if ((20 * percent) > 0) {
//                    weight = (int) Math.rint(20 * percent);
//                }
//                host.setWeight(weight);
//                new_weight += weight;
//                for (int index=0;index<weight;index++){
//                    weight_hosts_list.add(host);
//                }
//            }
//            //重新计算总权重
//            weight_sum = new_weight;
//        }else{
//            for (WeightHost host:weight_hosts){
//                for (int index=0;index<host.getWeight();index++){
//                    weight_hosts_list.add(host);
//                }
//            }
//
//        }
    }

    private static void configForbiddenHosts(){
        JSONArray array = JSONUtil.getArrayFromJSON(IP_FILTER, params);
        for (int index=0;index<array.size();index++){
            JSONObject object = array.getJSONObject(index);
            forbidden_hosts.add(object.getString(IP));
        }
    }

    private static void initCache(){
        for (InetSocketAddress address:roundrobin_hosts){
            cache.addAccessRecord(address.getHostString()+":"+address.getPort()+ACCESSRECORD);
        }
    }

    public static InetSocketAddress roundRobin() {
        InetSocketAddress address = roundrobin_hosts
                .get(rrindex.get() % roundrobin_hosts.size());
        rrindex.incrementAndGet();
        System.out.println("roundRobin新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
        return address;
    }

    public static InetSocketAddress weight() {
        InetSocketAddress address = null;
        while (true){
            index.set((index.get()+1)%weight_hosts.size());
            if (index.get() == 0){
                cw =  cw - gcd;
                if (cw <= 0){
                    cw = maxWeight;
                    if (cw == 0)
                        break;
                }
            }

            if (weight_hosts.get(index.get()).getWeight() >= cw){
                address = weight_hosts.get(index.get()).getAddress();
                break;
            }
        }
        if (address == null){
            address = weight_hosts.get(0).getAddress();
            System.out.println("weight负载均衡失败");
        }
        System.out.println("weight新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
        return address;
    }

    public static InetSocketAddress ip_hash(String ip) {
        long hash = EncryptUtil.ip_hash(ip);
        InetSocketAddress address = roundrobin_hosts
                .get((int) hash % roundrobin_hosts.size());
        System.out.println("ip_hash新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
        return address;
    }

    public static InetSocketAddress less_connect(){
        AccessRecord minRecord = Collections.min(cache.getAllAccessRecord());
        System.out.println(Thread.currentThread().getName()+" : AccessRecord--------: "+minRecord);
        String key = minRecord.getKey();
        String[] hostport = key.split("-")[0].split(":");
        return new InetSocketAddress(hostport[0],Integer.parseInt(hostport[1]));
    }

    public static String getString(String param) {
        if (params == null)
            return "";
        return params.getString(param);
    }

    public static Integer getInt(String param) {
        if (params == null)
            return 0;
        return params.getIntValue(param);
    }

    public static List<String> getForbiddenHosts(){
        return forbidden_hosts;
    }

    public static void setLB_Strategy(String strategy){
        params.put(LB_STRATEGY,strategy);
    }

    /**
     * 动态添加节点
     * @param hosts
     */
    public static void setNodes(List<WeightHost> hosts){
        List<Map<String,Object>> hostsmap = new CopyOnWriteArrayList<Map<String,Object>>();
        for (WeightHost host:hosts){
            InetSocketAddress address = host.getAddress();
            int weight = host.getWeight();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put(HOST,address.getHostName());
            map.put(PORT,address.getPort());
            map.put(WEIGHT,weight);
            hostsmap.add(map);
        }
        params.put(PROXY_PASS,JSONUtil.listToJson(hostsmap));
        configLoadbalanceStrategy();
    }

    public static void listAll(){
        System.out.println(JSONObject.toJSONString(params));
    }

    private static int getMaxDivisor(List<WeightHost> hosts) {
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
