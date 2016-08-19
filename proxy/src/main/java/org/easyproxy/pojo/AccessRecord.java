package org.easyproxy.pojo;/**
 * Description : 
 * Created by YangZH on 16-8-18
 *  下午11:01
 */

/**
 * Description :
 * Created by YangZH on 16-8-18
 * 下午11:01
 */

public class AccessRecord implements Comparable<AccessRecord>{

    private String key;

    private Integer times;

    public AccessRecord(String key, Integer times) {
        this.key = key;
        this.times = times;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    @Override
    public int compareTo(AccessRecord o) {
        if (this.times>o.getTimes()){
            return 1;
        }else if (this.times<o.getTimes()){
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "AccessRecord{" +
                "key='" + key + '\'' +
                ", times=" + times +
                '}';
    }
}
