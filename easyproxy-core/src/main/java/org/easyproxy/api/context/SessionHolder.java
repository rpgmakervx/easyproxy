package org.easyproxy.api.context;

import org.easyproxy.api.http.session.HttpSession;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xingtianyu on 17-3-19
 * 下午2:05
 * description:
 */

public class SessionHolder {

    private static Map<String,HttpSession> sessionMap = new ConcurrentHashMap<>();

    private ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

    public SessionHolder(){
        threadPool.scheduleAtFixedRate(new Monitor(),0, 1, TimeUnit.SECONDS);
    }

    public HttpSession getSession(String sessionId){
        return sessionMap.get(sessionId);
    }

    public void addSession(String sessionId,HttpSession session){
        sessionMap.put(sessionId,session);
    }

    public HttpSession delSession(String sessionId){
        return sessionMap.remove(sessionId);
    }

    class Monitor implements Runnable{

        @Override
        public void run() {
            Collection<HttpSession> entrySet = sessionMap.values();
            HttpSession []sessions = entrySet.toArray(new HttpSession [entrySet.size()]);
            for (HttpSession session:sessions){
                if (session.isExpire()){
                    session.clear();
                    delSession(session.getSessionId());
                    session = null;
                }
            }
        }
    }
}
