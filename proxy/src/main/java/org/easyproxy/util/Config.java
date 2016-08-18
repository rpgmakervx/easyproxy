package org.easyproxy.util;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午11:08
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import static org.easyproxy.constants.Const.*;
/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午11:08
 */

public class Config {
    private static List<String> hostsname = new ArrayList<String>();
    private static List<Integer> ports = new ArrayList<Integer>();
    //经过修正的权值
    private static List<Integer> weights = new ArrayList<Integer>();
    private static List<Map<String, Object>> proxys = new ArrayList<Map<String, Object>>();
    private static int weight_sum = 0;
    private XmlUtil xmlUtil;
    private static JSONObject params;
    private static int node_num = 0;
    private static AtomicInteger integer = new AtomicInteger(1);

    public Config(String path) {
        xmlUtil = new XmlUtil(path);
        params = JSONUtil.str2Json(xmlUtil.xml2Json());
        init();
    }

    public Config(InputStream is){
        xmlUtil = new XmlUtil(is);
        System.out.println("param: "+xmlUtil.xml2Json());
        params = JSONUtil.str2Json(xmlUtil.xml2Json());
        init();
    }

    private void init() {
        JSONArray array = JSONUtil.getArrayFromJSON(PROXY_PASS, params);
        //先把权重和IP 端口相关信息记录到内存（各个List）中，记录总权重
        for (int index = 0; index < array.size(); index++) {
            JSONObject object = array.getJSONObject(index);
            Map<String, Object> proxy = new HashMap<String, Object>();
            float weight = object.getFloatValue(WEIGHT);
            int port = object.getIntValue(PORT);
            String host = object.getString(HOST);
            proxy.put(WEIGHT, weight);
            proxy.put(PORT, port);
            proxy.put(HOST, host);
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
            Integer weight = weights.get(index);
//            weight_sum += weight;
            for (int i = 0; i < weight; i++) {
                hostsname.add(i, (String) proxys.get(index).get(HOST));
                ports.add(i, (Integer) proxys.get(index).get(PORT));
            }
        }
        System.out.println("weight_sum: " + weight_sum+", node_num: "+node_num);
    }

    public static InetSocketAddress roundRobin() {
//        System.out.println("weight_sum:"+weight_sum);
        InetSocketAddress address = new InetSocketAddress(
                hostsname.get(integer.get() % hostsname.size()), ports.get(integer.get() % ports.size()));
        System.out.println("新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
        System.out.println("AtomicCounter--> "+integer.incrementAndGet());
        return address;
    }

    public static InetSocketAddress weight() {
        System.out.println("weight_sum:" + weight_sum);
        Random rand = new Random();
        int randcode = rand.nextInt(weight_sum);
        return new InetSocketAddress(hostsname.get(randcode), ports.get(randcode));
    }

    public static InetSocketAddress ip_hash(String ip) {
        long hash = EncryptUtil.ip_hash(ip);
        return new InetSocketAddress(hostsname.get((int) hash % hostsname.size()), ports.get((int) hash % ports.size()));
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

    public static void listAll(){
        System.out.println(JSONObject.toJSONString(params));
    }
}
