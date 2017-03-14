package test;/**
 * Description : 
 * Created by YangZH on 16-9-29
 *  上午9:50
 */

import org.easyproxy.pojo.WeightHost;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description :
 * Created by YangZH on 16-9-29
 * 上午9:50
 */

public class TestStrategy {

    static int index = -1;
    static int round = 0;
    @Test
    public void test(){
        List<WeightHost> hosts = new ArrayList<>();
        InetSocketAddress address1 = new InetSocketAddress("192.168.1.1",8080);
        InetSocketAddress address2 = new InetSocketAddress("192.168.1.2",8080);
        InetSocketAddress address3 = new InetSocketAddress("192.168.1.3",8080);
        InetSocketAddress address4 = new InetSocketAddress("192.168.1.4",8080);
//        WeightHost host1 = new WeightHost(address1,2);
        WeightHost host2 = new WeightHost(address2,4);
        WeightHost host3 = new WeightHost(address3,6);
//        WeightHost host4 = new WeightHost(address4,8);
//        hosts.add(host1);
        hosts.add(host2);
        hosts.add(host3);
//        hosts.add(host4);
        Collections.sort(hosts);
        Collections.reverse(hosts);
        WeightHost maxWeight = Collections.max(hosts);
        int gcd = getMaxDivisor(hosts);
        System.out.println(hosts);
        for (int i=0;i<10;i++){
            System.out.println(comput(hosts,gcd,maxWeight.getWeight()));
        }
    }

    public WeightHost comput(List<WeightHost> hosts,int gcd,int max){
        while (true){
            index = (index +1)%hosts.size();
//            System.out.println("index-->"+index);
//            System.out.println("round-->"+round);
            if (index == 0){
                round =  round - gcd;
                if (round <= 0){
                    round = max;
                    if (round == 0)
                        return null;
                }
            }

            if (hosts.get(index).getWeight() >= round){
                return hosts.get(index);
            }
        }
    }

    public int getMaxDivisor(List<WeightHost> hosts) {
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
