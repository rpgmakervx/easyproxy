package org.easyproxy.config;

import com.alibaba.fastjson.JSONObject;
import org.easyproxy.cache.DefaultCache;
import org.easyproxy.pojo.AccessRecord;
import org.easyproxy.pojo.WeightHost;
import org.easyproxy.util.codec.EncryptUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.easyproxy.config.ConfigEnum.LB_STRATEGY;


/**
 * Description :
 * Created by xingtianyu on 16-12-16
 * 下午8:29
 */

public class PropetyConfig implements Config {

    private List<InetSocketAddress> roundrobin_hosts = new CopyOnWriteArrayList<InetSocketAddress>();
    //weight host list
//    private  List<WeightHost> weight_hosts_list = new CopyOnWriteArrayList<WeightHost>();
    private List<WeightHost> weight_hosts = new ArrayList<WeightHost>();
    //ip_filter
    private List<String> forbidden_hosts = new CopyOnWriteArrayList<String>();
    private AtomicInteger index = new AtomicInteger(-1);
    private int cw = 0;
    private int gcd = 0;
    private int maxWeight = 0;

    private AtomicInteger rrindex = new AtomicInteger(0);
    private DefaultCache cache;

    private JSONObject params;

    public InetSocketAddress roundRobin() {
        InetSocketAddress address = roundrobin_hosts
                .get(rrindex.get() % roundrobin_hosts.size());
        rrindex.incrementAndGet();
//        System.out.println("roundRobin新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
        return address;
    }

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
//        System.out.println("weight新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
        return address;
    }

    public InetSocketAddress ip_hash(String ip) {
        long hash = EncryptUtil.ip_hash(ip);
        InetSocketAddress address = roundrobin_hosts
                .get((int) hash % roundrobin_hosts.size());
//        System.out.println("ip_hash新获取的地址-->  " + address.getHostName() + ":" + address.getPort());
        return address;
    }

    public InetSocketAddress less_connect() {
        AccessRecord minRecord = Collections.min(cache.getAllAccessRecord());
        System.out.println(Thread.currentThread().getName() + " : AccessRecord--------: " + minRecord);
        String key = minRecord.getKey();
        String[] hostport = key.split("-")[0].split(":");
        return new InetSocketAddress(hostport[0], Integer.parseInt(hostport[1]));
    }

    public String getString(String param) {
        if (params == null)
            return "";
        return params.getString(param);
    }

    public Integer getInt(String param) {
        if (params == null)
            return 0;
        return params.getIntValue(param);
    }

    public List<String> getForbiddenHosts() {
        return forbidden_hosts;
    }

    public void setLB_Strategy(String strategy) {
        params.put(LB_STRATEGY.key, strategy);
    }


    public void listAll() {
        System.out.println(JSONObject.toJSONString(params));
    }

    private int getMaxDivisor(List<WeightHost> hosts) {
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

    /**
     * 获取服务器IP地址
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String  getServerIp(){
        String SERVER_IP = null;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                ip = (InetAddress) ni.getInetAddresses().nextElement();
                SERVER_IP = ip.getHostAddress();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {
                    SERVER_IP = ip.getHostAddress();
                    break;
                } else {
                    ip = null;
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return SERVER_IP;
    }

    public static void main(String[] args) {
        System.out.println(getServerIp());
    }
}
