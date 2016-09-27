package org.easyproxy;/**
 * Description : 
 * Created by YangZH on 16-8-15
 *  上午12:17
 */

import java.io.IOException;

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
//        Pattern pattern = Pattern.compile("/easyproxy.*");
//        System.out.println(pattern.matcher("/easyproxy/kk?lb_strategy=roundrobin").matches());
//        CyclicBarrier barrier = new CyclicBarrier(2,);
        Integer a1 = 127;
        Integer a2 = 127;
        Integer b1 = -129;
        Integer b2 = -129;
        String str1 = "111";
        String str2 = new String("111").intern();
        System.out.println(a1==a2);
        System.out.println(a1.equals(a2));
        System.out.println(b1==b2);
        System.out.println(b1.equals(b2));
        System.out.println(str1==str2);
    }

}
