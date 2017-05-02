package org.easyproxy.api.app.pojo;

import org.easyarch.netpet.kits.StringKits;
import org.easyproxy.pojo.WeightHost;

/**
 * Created by xingtianyu on 17-4-2
 * 上午11:56
 * description:
 */

public class HostVO {

    private String id;

    private String ip;

    private Integer port;

    private Integer weight;

    public HostVO() {
        this.id = StringKits.uuid();
    }

    public HostVO(WeightHost host){
        this();
        wrap(host);
    }

    public HostVO(String ip, Integer port, Integer weight) {
        this.id = StringKits.uuid();
        this.ip = ip;
        this.port = port;
        this.weight = weight == null?1:weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void wrap(WeightHost host){
        this.ip = host.getAddress().getHostString();
        this.port = host.getAddress().getPort();
        this.weight = host.getWeight();
    }
}
