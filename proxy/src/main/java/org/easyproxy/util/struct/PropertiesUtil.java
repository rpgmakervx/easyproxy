package org.easyproxy.util.struct;

import org.easyproxy.config.ConfigEnum;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.easyproxy.config.ConfigEnum.*;

/**
 * Description :
 * Created by xingtianyu on 16-12-12
 * 下午7:54
 */

public class PropertiesUtil {

    private Properties properties;

    private Map<String, Object> configMap;

    public PropertiesUtil(String path) {
        properties = new Properties();
        configMap = new HashMap<>();
        try {
            properties.load(new FileInputStream(path));
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PropertiesUtil(InputStream is) {
        properties = new Properties();
        configMap = new HashMap<>();
        try {
            properties.load(is);
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        Enumeration<?> enums = properties.propertyNames();
        while (enums.hasMoreElements()) {
            String name = String.valueOf(enums.nextElement());
            if (NODE_IP.key.equals(name)) {
                List<String> iplist = new ArrayList<>();
                String[] ips = getValue(String.valueOf(name), NODE_IP.defVal).split(",");
                for (String ip : ips){
                    iplist.add(ip);
                }
                configMap.put(NODE_IP.key,iplist);
            }else if (NODE_PORT.key.equals(name)){
                List<String> portlsit = new ArrayList<>();
                String[] ports = getValue(String.valueOf(name), NODE_PORT.defVal).split(",");
                for (String port : ports){
                    portlsit.add(port);
                }
                configMap.put(NODE_IP.key,portlsit);
            }else if (NODE_WEIGHT.key.equals(name)){
                List<String> weightlist = new ArrayList<>();
                String[] weights = getValue(String.valueOf(name), NODE_WEIGHT.defVal).split(",");
                for (String weight : weights){
                    weightlist.add(weight);
                }
                configMap.put(NODE_WEIGHT.key,weightlist);
            }else if (FIREWALL_FILTER.key.equals(name)){
                List<String> filterlsit = new ArrayList<>();
                String[] weights = getValue(String.valueOf(name), FIREWALL_FILTER.defVal).split(",");
                for (String weight : weights){
                    filterlsit.add(weight);
                }
                configMap.put(FIREWALL_FILTER.key,filterlsit);
            }else{
                configMap.put(name,getValue(String.valueOf(name),ConfigEnum.getVal(name)));
            }
        }
    }


    public String getValue(String key, String defVal) {
        return properties.getProperty(key, defVal);
    }

    public static void main(String[] args) {
        PropertiesUtil util = new PropertiesUtil("/home/code4j/IDEAWorkspace/easyproxy/proxy/src/main/resources/proxy.properties");
        System.out.println(JSONUtil.map2Json(util.configMap));

    }

}
