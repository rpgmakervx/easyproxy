package org.easyproxy.manager;/**
 * Description : 
 * Created by YangZH on 16-8-16
 *  下午4:20
 */

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description :
 * Created by YangZH on 16-8-16
 * 下午4:20
 */

public class HostManager {

    private static ConcurrentHashMap<String,InetSocketAddress> hosts = new ConcurrentHashMap<String,InetSocketAddress>();

    public static void setHosts(String src,InetSocketAddress dst){
        hosts.put(src,dst);
    }

    public static InetSocketAddress getHosts(String src){
        return hosts.get(src);
    }
}
