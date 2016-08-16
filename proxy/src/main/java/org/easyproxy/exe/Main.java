package org.easyproxy.exe;/**
 * Description : 
 * Created by YangZH on 16-5-25
 *  下午2:28
 */

import org.easyproxy.server.ProxyServer;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Description :
 * Created by YangZH on 16-5-25
 * 下午2:28
 */

public class Main {


    public static void main(final String[] args) throws MalformedURLException {
//        System.out.println(Arrays.toString(MD5Util.getMD5("x")));
//        System.out.println(MD5Util.getMD5("x").length*8);
//        System.out.println(Arrays.toString(new String(MD5Util.getMD5("x")).getBytes()));
        //开源中国要有WWW
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ProxyServer().startup();
            }
        }).start();
//        Pattern pattern = Pattern.compile(".*172.16.235.1.*");
//        System.out.println(pattern.matcher(" 172.16.235.1:8080").matches());
//        System.out.println(System.getProperty("java.library.path"));
//        URL url = new URL("192.168.1.1");
//        URL url2 = new URL("http://192.168.1.1/");
//        System.out.println(url.getPath());
//        System.out.println(url2.getPath());
//        System.out.println(getLocalIPForJava());
//        System.out.println(Const.PROXY_HOST);
    }

    public static String getLocalIPForJava() {
        StringBuilder sb = new StringBuilder();
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
                            && inetAddress.isSiteLocalAddress()) {
                        sb.append(inetAddress.getHostAddress().toString() + "\n");
                    }
                }
            }
        } catch (SocketException e) {
        }
        return sb.toString();
    }
}
