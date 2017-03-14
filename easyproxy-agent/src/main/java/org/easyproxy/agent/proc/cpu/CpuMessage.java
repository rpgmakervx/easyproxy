package org.easyproxy.agent.proc.cpu;

/**
 * Description :
 * Created by xingtianyu on 16-11-23
 * 下午11:42
 */

public class CpuMessage {

    private double userPer;
    private double sysPer;
    private double combPer;

    private int mHz;

    public CpuMessage(double userPer, double sysPer, double combPer, int mHz) {
        this.userPer = userPer;
        this.sysPer = sysPer;
        this.combPer = combPer;
        this.mHz = mHz;
    }

    public double getUserPer() {
        return userPer;
    }

    public void setUserPer(double userPer) {
        this.userPer = userPer;
    }

    public double getSysPer() {
        return sysPer;
    }

    public void setSysPer(double sysPer) {
        this.sysPer = sysPer;
    }

    public double getCombPer() {
        return combPer;
    }

    public void setCombPer(double combPer) {
        this.combPer = combPer;
    }

    public int getmHz() {
        return mHz;
    }

    public void setmHz(int mHz) {
        this.mHz = mHz;
    }
}
