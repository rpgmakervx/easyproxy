package org.easyproxy.api.http.session.impl;


import org.easyproxy.api.http.session.HttpSession;
import org.easyproxy.api.kits.TimeKits;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xingtianyu on 17-3-14
 * 下午8:35
 * description:session,在HandlerContext处实现了主动淘汰，内部getAttr实现被动淘汰
 */

public class DefaultHttpSession implements HttpSession {

    private Map<String,Object> sessionMap = new HashMap<>();

    private int maxAge;

    public String sessionId;

    private Date createAt;

    public DefaultHttpSession(){
        createAt = new Date();
    }

    public Object getAttr(String name){
        if (isExpire()){
            remove(name);
            return null;
        }
        return sessionMap.get(name);
    }
    public void setAttr(String name,String value){
        sessionMap.put(name,value);
    }

    public Object remove(String name){
        return sessionMap.remove(name);
    }

    @Override
    public void clear() {
        sessionMap.clear();
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean isExpire() {
        Date current = new Date();
        return current.before(TimeKits.plusSeconds(maxAge,createAt));
    }
}
