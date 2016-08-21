package test;/**
 * Description : 
 * Created by YangZH on 16-8-21
 *  上午11:35
 */

import org.easyproxy.util.Config;
import org.easyproxy.util.XmlUtil;
import org.junit.Test;

/**
 * Description :
 * Created by YangZH on 16-8-21
 * 上午11:35
 */

public class JSONTest {

    @Test
    public void testJSON(){
        System.out.println("begin");
        XmlUtil xmlUtil = new XmlUtil(Config.class.getResourceAsStream("/proxy.xml"));
        new Config(Config.class.getResourceAsStream("/proxy.xml"));
        System.out.println("param: "+xmlUtil.xml2Json());
//        Config.listAllWeightHosts();
//        JSONObject object = Config.getParams();
//        object.put("listen", 9000);
//        System.out.println("--------");
//        System.out.println("new json-->"+object.toJSONString());
//        Config.listAllWeightHosts();
    }
}
