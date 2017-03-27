package org.easyproxy.api.http.session;

import java.io.Serializable;

/**
 * Description :
 * Created by xingtianyu on 17-2-27
 * 上午12:01
 * description:
 */

public interface HttpSession extends Serializable{

    public Object getAttr(String name);
    public void setAttr(String name, String value);
    public String getSessionId();
    public void setSessionId(String sessionId);
    public Object remove(String name);
    public void clear();
    public int getMaxAge();
    public void setMaxAge(int maxAge);
    public boolean isExpire();
}
