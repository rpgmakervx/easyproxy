package org.easyproxy;/**
 * Description : 
 * Created by YangZH on 16-8-15
 *  上午12:17
 */

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Description :
 * Created by YangZH on 16-8-15
 * 上午12:17
 */

public class Main {

    public static void main(String[] args) throws IOException {
//        XmlUtil xmlUtil = new XmlUtil("/proxy.xml");
//        xmlUtil.listAll();
//        NodeSelector.init("/proxy.xml");
//        InetSocketAddress address = NodeSelector.weight();
//        System.out.println("host: "+address.getHostString());
//        Config config = new Config("/proxy.xml");
//        for (int i = 0; i < 14; i++) {
//            InetSocketAddress address = Config.roundRobin();
//            System.out.println("host: " + address.getHostString());
//        }
//        System.out.println("memory:"+JedisUtil.getMemoryUsed());
        Pattern pattern = Pattern.compile("/easyproxy.*");
        System.out.println(pattern.matcher("/easyproxy/kk?lb_strategy=roundrobin").matches());
    }

}
