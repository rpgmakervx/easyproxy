package org.easyproxy.api.app.pojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description :
 * Created by xingtianyu on 17-1-17
 * 下午2:30
 * description:
 */

public class ResponseEntity<T> {


    private Collection<T> rows;

    private int total;
    public ResponseEntity(int total, Collection<T> rows) {
        this.rows = rows;
        this.total = total;
    }
    public ResponseEntity(int total, T row) {
        this.rows = new ArrayList<>();
        this.total = total;
    }


    public Collection<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
