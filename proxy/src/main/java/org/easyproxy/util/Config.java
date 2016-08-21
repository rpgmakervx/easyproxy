package org.easyproxy.util;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午11:08
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.easyproxy.cache.Cache;
import org.easyproxy.pojo.AccessRecord;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
//    private static List<String> roundrobin_ports = new CopyOnWriteArrayList<String>();
    private static List<InetSocketAddress> weight_hosts = new CopyOnWriteArrayList<InetSocketAddress>();
//    private static List<Integer> ports = new CopyOnWriteArrayList<Integer>();
    //ip_filter
    private static List<String> forbidden_hosts = new CopyOnWriteArrayList<String>();
    private static List<Integer> weights = new CopyOnWriteArrayList<Integer>();
    private static List<Map<String, Object>> proxys = new CopyOnWriteArrayList<Map<String, Object>>();
    private static int weight_sum = 0;
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

    private void configLoadbalanceStrategy(){
        JSONArray array = JSONUtil.getArrayFromJSON(PROXY_PASS, params);
        //先把权重和IP 端口相关信息记录到内存（各个List）中，记录总权重
        for (int index = 0; index < array.size(); index++) {
            JSONObject object = array.getJSONObject(index);
            Map<String, Object> proxy = new HashMap<String, Object>();
            int weight = object.getIntValue(WEIGHT);
            int port = object.getIntValue(PORT);
            String host = object.getString(HOST);
            InetSocketAddress address = new InetSocketAddress(host,port);
            proxy.put(WEIGHT, weight);
            proxy.put(HOST, address);
            roundrobin_hosts.add(address);
            weight_sum += weight;
            weights.add((int) Math.rint(weight));
            proxys.add(proxy);
            node_num++;
        }
        //权重可能会被用户设置的过高，这时手动降低权重值的量级，维持在10以内，并把权重记录更新到权重list中
        if (weight_sum > 10) {
            int new_weight = 0;
            for (int index = 0; index < proxys.size(); index++) {
                float percent = weights.get(index) / (float) weight_sum;
                int weight = 1;
                if ((10 * percent) > 0) {
                    weight = (int) Math.rint(10 * percent);
                }
                new_weight += weight;
                weights.set(index, weight);
            }
            //重新计算总权重
            weight_sum = new_weight;
        }

        //根据权重值，值是多少就给对应的IP PORT 相应的add多少次，权重越高add次数越多，被获取到的概率越高
        for (int index = 0; index < proxys.size(); index++) {
            Integer weight = (Integer) proxys.get(index).get(WEIGHT);
//            weight_sum += weight;
            for (int i = 0; i < weight; i++) {
                InetSocketAddress address = (InetSocketAddress) proxys.get(index).get(HOST);
                weight_hosts.add(i, address);
            }
        }
        System.out.println("weight_sum: " + weight_sum+", node_num: "+node_num);
    }

    private void configForbiddenHosts(){
        JSONArray array = JSONUtil.getArrayFromJSON(IP_FILTER, params);
        for (int index=0;index<array.size();index++){
            JSONObject object = array.getJSONObject(index);
            forbidden_hosts.add(object.getString(IP));
        }
    }

    private void initCache(){
        for (InetSocketAddress address:roundrobin_hosts){
            cache.addAccessRecord(address.getHostString()+":"+address.getPort()+ACCESSRECORD);
        }
    }

    public static InetSocketAddress roundRobin() {
//        System.out.println("weight_sum:"+weight_sum);
        InetSocketAddress address = roundrobin_hosts
                .get(rrindex.get() % roundrobin_hosts.size());
        rrindex.incrementAndGet();
        System.out.println("roundRobin新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
        return address;
    }

    public static InetSocketAddress weight() {
        InetSocketAddress address = weight_hosts
                .get(weight_rrindex.get() % weight_hosts.size());
        weight_rrindex.incrementAndGet();
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

    public static void listAll(){
        System.out.println(JSONObject.toJSONString(params));
    }

    public static void listAllWeightHosts(){
        System.out.println("listAllWeightHosts--->"+weight_hosts.size());
        for (InetSocketAddress address:weight_hosts){
            System.out.println(address);
        }
    }

    public static JSONObject getParams(){
        return params;
    }
}
