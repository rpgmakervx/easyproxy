package org.easyproxy.util.struct;

import org.easyproxy.config.ConfigEnum;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.easyproxy.config.ConfigEnum.*;
import static org.easyproxy.constants.Const.IP;
import static org.easyproxy.constants.Const.PORT;
import static org.easyproxy.constants.Const.WEIGHT;

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
            if (NODES.key.equals(name)) {
                List<String> iplist = new ArrayList<>();
                List<Integer> weightlist = new ArrayList<>();
                List<Integer> portlist = new ArrayList<>();
                String[] nodes = getValue(String.valueOf(name), NODES.defVal).split(",");
                for (String node : nodes){
                    String[] vals = node.split(":");
                    String ip = vals[0];
                    Integer port = Integer.valueOf(vals[1]);
                    Integer weight = Integer.valueOf(vals[2]);
                    iplist.add(ip);
                    weightlist.add(weight);
                    portlist.add(port);
                }
                configMap.put(IP,iplist);
                configMap.put(PORT,portlist);
                configMap.put(WEIGHT,weightlist);
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

    private String getValue(String key, Object defVal) {
        return properties.getProperty(key, String.valueOf(defVal));
    }

    public Map<String,Object> getConfigMap(){
        return configMap;
    }

    public static void main(String[] args) {
        PropertiesUtil util = new PropertiesUtil("/home/code4j/IDEAWorkspace/easyproxy/proxy/src/main/resources/proxy.properties");
        System.out.println(JSONUtil.map2Json(util.configMap));

    }

}
