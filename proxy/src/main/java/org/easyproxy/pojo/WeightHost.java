package org.easyproxy.pojo;/**
 * Description : 
 * Created by YangZH on 16-8-21
 *  下午3:12
 */

import java.net.InetSocketAddress;

/**
 * Description :
 * Created by YangZH on 16-8-21
 * 下午3:12
 */

public class WeightHost {

    public InetSocketAddress address;

    private Integer weight;

    public WeightHost(InetSocketAddress address, Integer weight) {
        this.address = address;
        this.weight = weight;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "WeightHost{" +
                "address=" + address +
                ", weight=" + weight +
                '}';
    }
}
