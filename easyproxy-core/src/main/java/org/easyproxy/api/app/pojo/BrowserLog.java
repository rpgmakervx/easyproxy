package org.easyproxy.api.app.pojo;

/**
 * Created by xingtianyu on 17-5-11
 * 下午7:27
 * description:
 */

public class BrowserLog {

    private String browserName;

    private Integer count;

    public BrowserLog(String browserName, Integer count) {
        this.browserName = browserName;
        this.count = count;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
