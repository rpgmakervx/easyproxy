package org.easyproxy.api.context;


import org.easyproxy.api.http.protocol.HttpStatus;
import org.easyproxy.api.kits.StringKits;
import org.easyproxy.api.mvc.action.Action;
import org.easyproxy.api.mvc.action.ActionWrapper;
import org.easyproxy.api.mvc.action.filter.HttpFilter;
import org.easyproxy.api.mvc.router.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.easyproxy.api.mvc.action.ActionType.FILTER;


/**
 * Created by xingtianyu on 17-3-3
 * 下午6:11
 * description:
 */

public class ActionHolder {

    /**
     * handler写path; filter写类全名
     * 顺序的连式结构
     */
    private static List<ActionWrapper> actions = new CopyOnWriteArrayList<>();

    /**
     * 1.遍历所有的router和action映射
     * 2.做eq对比，参数化url和真正url对比出结果
     * 3.不相等的继续遍历比较，相等的进行下一个逻辑
     * 4.如果是path完全相等，则直接返回action,返回前检查method是否匹配，否则返回405；
     * 5.否则是参数化匹配的结果，记录结果，继续遍历（因为path完全相等优先级最高）
     *
     * @param router
     * @return
     */
    public ActionWrapper getAction(Router router) {
        ActionWrapper wrapper = null;
        for (ActionWrapper wrp : actions) {
            Router r = wrp.getRouter();
            boolean equals = eq(r, router);
            if (equals) {
                if (r.getPath().equals(router.getPath())) {
                    wrapper = wrp;
                    if (!r.getMethod().equals(router.getMethod())){
                        wrapper.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                    }
                    return wrapper;
                }
                wrapper = wrp;
                if (!r.getMethod().equals(router.getMethod())){
                    wrapper.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                }
            }
        }
        return wrapper;
    }

    /**
     * 获取拦截这个请求的所有拦截器
     * 便利顺序的action集合，如果命中router则说明
     * @param router
     * @return
     */
    public List<HttpFilter> getFilters(Router router) {
        List<HttpFilter> filters = new ArrayList<>();
        for (ActionWrapper wrp : actions) {
            Router r = wrp.getRouter();
            boolean equals = eq(r, router);
            if (equals) {
                Action action = wrp.getAction();
                if (FILTER.equals(wrp.getType())){
                    filters.add((HttpFilter) action);
                }
            }
        }
        return filters;
    }

    public void addAction(Router router, Action action) {
        int currentIndex = actions.size();
        ActionWrapper wrapper = new ActionWrapper(action,router, currentIndex);
        actions.add(wrapper);
        System.out.println("add Router:" + router + ", actionsize:" + actions.size());
    }
    /**
     * 1.路由地址相同必定相等
     * 2.路由包括参数化片段，则进一步匹配;如果不包括，不相等
     * 3.匹配每个片段，如果除了参数化片段都相等，则相等.
     * 4.如果遇到参数化router且内部router不是filter类型，则记录match，表示成功匹配参数化router
     * 4.如遇到通配符，且router类型是过滤器，则终止匹配，直接返回true。
     * 5.对于 *.html这种过滤规则，则先看两个router是否都包含‘.’，并且内部router是否包含通配符，符合则取两个router的后缀，相等则返回true，否则返回false；
     * 注意：
     *      1.eq方法执行过程中，将参数保存，供request获取
     *      2.对于 / 路径，需要判断segement集合是否为空
     *
     * @param storeRouter
     * @param cmpRouter
     * @return
     */
    private boolean eq(Router storeRouter, Router cmpRouter) {
        String strPath = storeRouter.getPath();
        String cmpPath = cmpRouter.getPath();
        if (storeRouter == null || cmpRouter == null) {
            return false;
        }
        if (!storeRouter.getMethod().equals(cmpRouter.getMethod())){
            return false;
        }
        boolean isFilter = FILTER.equals(storeRouter.getType());
        if (storeRouter.getLevel() != cmpRouter.getLevel()
                &&!isFilter) {
            return false;
        }
        if (strPath.equals(cmpPath)) {
            return true;
        } else if (storeRouter.isParameterize()||isFilter) {
            boolean matched = false;
            List<String> storeSegements = storeRouter.getSegements();
            List<String> cmpSegements = cmpRouter.getSegements();
            if (strPath.startsWith(Router.WILDCARD)
                    &&strPath.contains(Router.POINT)){
                String seg = cmpSegements.get(cmpSegements.size()-1);
                if (seg.contains(Router.POINT)){
                    String suffix1 = seg.split("\\"+Router.POINT)[1];
                    String suffix2 = strPath.split("\\"+Router.POINT)[1];
                    return suffix1.equals(suffix2);
                }
            }
            for (int index = 0; index < storeRouter.getLevel(); index++) {
                String seg1 = "";
                String seg2 = "";
                if (!storeSegements.isEmpty()){
                    seg1 = storeSegements.get(index);
                }
                if (!cmpSegements.isEmpty()){
                    seg2 = cmpSegements.get(index);
                }
                String paramSeg1 = storeRouter.getParameterizeUrl().get(index);
                //两个片段不相等，或当前片段不是参数化的,
                //再检查router是否是filter类型且否包含通配符，是则认为相等，否则不相等
                if (!seg1.equals(seg2) && paramSeg1 == null) {
                    if (Router.WILDCARD.equals(seg1)){
                        return true;
                    }else if (seg1.contains(Router.POINT)
                            &&seg2.contains(Router.POINT)
                            &&seg2.startsWith(Router.WILDCARD)){
                        String suffix1 = seg1.split("\\"+Router.POINT)[1];
                        String suffix2 = seg2.split("\\"+Router.POINT)[1];
                        return suffix1.equals(suffix2);
                    }
                    return false;
                }
                if (paramSeg1 != null&&!isFilter) {
                    String name = StringKits.strip(paramSeg1, Router.LEFT, Router.RIGHT);
                    cmpRouter.getPathParams().put(name, seg2);
                    matched = true;
                }
            }
            return matched;
        }
        return false;
    }
}
