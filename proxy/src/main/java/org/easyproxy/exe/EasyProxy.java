package org.easyproxy.exe;/**
 * Description : 
 * Created by YangZH on 16-5-25
 *  下午2:28
 */

import org.easyproxy.constants.Const;
import org.easyproxy.server.ProxyServer;
import org.easyproxy.util.Config;
import org.easyproxy.util.XmlUtil;

import java.net.MalformedURLException;

/**
 * Description :
 * Created by YangZH on 16-5-25
 * 下午2:28
 */

public class EasyProxy {


    public static void main(final String[] args) throws MalformedURLException {
        //开源中国要有WWW
        String config = Const.DEFAULT_CONFIGPATH;
        System.out.println("args--> "+args.length);
        XmlUtil xmlUtil = new XmlUtil(Config.class.getResourceAsStream(config));
        System.out.println("param: "+xmlUtil.xml2Json());
        if (args.length>0){
            config = args[0];
        }
        System.out.println("config path-->"+config);
        ProxyServer server = new ProxyServer(config);
        server.startup();
    }

//    public static String getLocalIPForJava() {
//        StringBuilder sb = new StringBuilder();
//        try {
//            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
//            while (en.hasMoreElements()) {
//                NetworkInterface intf = (NetworkInterface) en.nextElement();
//                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
//                while (enumIpAddr.hasMoreElements()) {
//                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
//                            && inetAddress.isSiteLocalAddress()) {
//                        sb.append(inetAddress.getHostAddress().toString() + "\n");
//                    }
//                }
//            }
//        } catch (SocketException e) {
//        }
//        return sb.toString();
//    }
}
