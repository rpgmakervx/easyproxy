package org.easyproxy.api.app.pojo;

/**
 * Created by xingtianyu on 17-4-8
 * 下午5:09
 * description:
 */

public class FireWallHost {

    private String id;

    private String ip;

    public FireWallHost(String id, String ip) {
        this.id = id;
        this.ip = ip;
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
}
