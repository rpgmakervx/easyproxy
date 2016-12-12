package test;/**
 * Description : 
 * Created by YangZH on 16-8-21
 *  上午11:35
 */

import org.easyproxy.pojo.WeightHost;
import org.easyproxy.config.Config;
import org.easyproxy.util.struct.JSONUtil;
import org.easyproxy.util.struct.XmlUtil;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.easyproxy.constants.Const.*;

/**
 * Description :
 * Created by YangZH on 16-8-21
 * 上午11:35
 */

public class JSONTest {

    @Test
    public void testJSON(){
        System.out.println("begin");
        XmlUtil xmlUtil = new XmlUtil(Config.class.getResourceAsStream("/proxy.xml"));
        new Config(Config.class.getResourceAsStream("/proxy.xml"));
        System.out.println("param: "+xmlUtil.xml2Json());
//        Config.listAllWeightHosts();
//        JSONObject object = Config.getParams();
//        object.put("listen", 9000);
//        System.out.println("--------");
//        System.out.println("new json-->"+object.toJSONString());
//        Config.listAllWeightHosts();
    }

    @Test
    public void testJSON2(){
        List<WeightHost> hosts = new ArrayList<>();
        WeightHost host1 = new WeightHost(new InetSocketAddress("192.168.1.1",80),2);
        WeightHost host2 = new WeightHost(new InetSocketAddress("192.168.1.2",80),3);
        WeightHost host3 = new WeightHost(new InetSocketAddress("192.168.1.3",80),1);
        WeightHost host4 = new WeightHost(new InetSocketAddress("192.168.1.4",80),4);
        hosts.add(host1);
        hosts.add(host2);
        hosts.add(host3);
        hosts.add(host4);
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
        System.out.println(JSONUtil.list2Json(hostsmap));
    }
}
