package org.easyproxy.api.app.pojo;

/**
 * Created by xingtianyu on 17-5-9
 * 下午10:10
 * description:
 */

public class DailyActiveLog {

    private String day;

    private Integer count;

    public DailyActiveLog(String day, Integer count) {
        this.day = day;
        this.count = count;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
