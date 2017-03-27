package org.easyproxy.api.mvc.action;


import org.easyproxy.api.http.protocol.HttpStatus;
import org.easyproxy.api.mvc.action.filter.HttpFilter;
import org.easyproxy.api.mvc.router.Router;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xingtianyu on 17-3-3
 * 下午6:12
 * description:ActionWrapper是一个链表结构，持有一个指向前面的指针
 */

public class ActionWrapper {

    private Action self;

    private Router router;

    private int index;

    private int status;

    private ActionType type;

    private ActionWrapper pre;

    public ActionWrapper(Action action,Router router){
        this.self = action;
        this.router = router;
        this.type = ActionType.getType(action);
        this.index = 0;
        this.status = HttpStatus.OK;
        this.pre = null;
    }
    public ActionWrapper(Action action,Router router, int index){
        this.self = action;
        this.router = router;
        this.type = ActionType.getType(action);
        this.status = HttpStatus.OK;
        this.index = index;
        this.pre = null;
    }
    public ActionWrapper(Action action,int status, int index){
        this.self = action;
        this.type = ActionType.getType(action);
        this.index = index;
        this.status = status;
        this.pre = null;
    }

    public ActionWrapper(Action self, int index, ActionType type) {
        this.self = self;
        this.index = index;
        this.type = type;
        this.pre = null;
    }

    public Action getAction() {
        return self;
    }

    public void setAction(Action self) {
        this.self = self;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public ActionWrapper getPreAction() {
        return pre;
    }

    public void setPreAction(ActionWrapper wrapper) {
        this.pre = wrapper;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    /**
     * 获取当前wrapper的真实action类型
     * 如果是Filter则存储到返回结果中，并继续遍历，下一个如果不是filter了，说明该wrapper所处的拦截器结束
     * 否则继续存储到返回结果，继续遍历，直到不是filter为止。
     * 返回时做一次结果翻转，保证先声明的filter先被获取并执行。
     * 该方法不会返回空指针
     * @return
     */
    public List<HttpFilter> getFilters(){
        List<HttpFilter> filter = new ArrayList<>();
        ActionWrapper wrapper = this;
        boolean continuly = true;
        while (wrapper != null){
            ActionType type = wrapper.getType();
            Action action = wrapper.getAction();
            if (type == ActionType.FILTER&&continuly){
                filter.add((HttpFilter) action);
                ActionWrapper preWrapper = wrapper.getPreAction();
                //下一个action如果不是过滤器的话就结束过滤器遍历
                if (preWrapper.getType() != ActionType.FILTER
                        ||preWrapper == null){
                    continuly = false;
                }
            }
            wrapper = wrapper.getPreAction();
        }
        Collections.reverse(filter);
        return filter;
    }
}
