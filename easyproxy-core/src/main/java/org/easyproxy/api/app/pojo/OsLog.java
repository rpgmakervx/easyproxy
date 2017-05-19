package org.easyproxy.api.app.pojo;

/**
 * Created by xingtianyu on 17-5-11
 * 下午7:44
 * description:
 */

public class OsLog {

    private String systemName;

    private Integer count;

    public OsLog(String systemName, Integer count) {
        this.systemName = systemName;
        this.count = count;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
