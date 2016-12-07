package org.easyproxy.agent.proc.mem;

/**
 * Description :
 * Created by xingtianyu on 16-11-24
 * 上午12:00
 */

public class MemMessage {

    private long total;

    private long used;

    private long free;

    private long swpTotal;

    private long swpUsed;

    private long swpFree;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
    }

    public long getSwpTotal() {
        return swpTotal;
    }

    public void setSwpTotal(long swpTotal) {
        this.swpTotal = swpTotal;
    }

    public long getSwpUsed() {
        return swpUsed;
    }

    public void setSwpUsed(long swpUsed) {
        this.swpUsed = swpUsed;
    }

    public long getSwpFree() {
        return swpFree;
    }

    public void setSwpFree(long swpFree) {
        this.swpFree = swpFree;
    }
}
