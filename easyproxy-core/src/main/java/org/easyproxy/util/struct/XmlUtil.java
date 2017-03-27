package org.easyproxy.util.struct;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午11:08
 */

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.easyproxy.config.ConfigEnum;
import org.easyproxy.constants.Const;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午11:08
 */

public class XmlUtil {

    private static SAXReader reader = new SAXReader();
    private Document document = null;
    private Element root = null;
    private JSONObject object = new JSONObject();

    public XmlUtil(InputStream is) {
        try {
            document = reader.read(is);
            root = document.getRootElement();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public XmlUtil(String xml_path) {
        try {
            InputStream is = new FileInputStream(xml_path);
            document = reader.read(is);
            root = document.getRootElement();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(){
        Iterator<Element> elementIterator = root.elementIterator();
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            Element nameElem = element.element("name");
            Element valueElem = element.element("value");
            ConfigEnum em = ConfigEnum.getEnum(nameElem.getTextTrim());
            switch (em){
                case NODES:
                    List<String> ips = new ArrayList<>();
                    List<Integer> ports = new ArrayList<>();
                    List<Integer> weights = new ArrayList<>();
                    String text = valueElem.getTextTrim();
                    if (StringUtils.isEmpty(text)){
                        break;
                    }
                    String[] nodes = text.split(",");
                    for (String node:nodes){
                        String[] vals = node.split(":");
                        ips.add(vals[0]);
                        ports.add(Integer.parseInt(vals[1]));
                        weights.add(Integer.parseInt(vals[2]));
                    }
                    object.put(Const.IP,ips);
                    object.put(Const.PORT,ports);
                    object.put(Const.WEIGHT,weights);
                    break;
                case FIREWALL_FILTER:
                    List<String> filterIP = new ArrayList<>();
                    String textTrim = valueElem.getTextTrim();
                    String[] filtered = textTrim.split(",");
                    for (String ip:filtered){
                        filterIP.add(ip);
                    }
                    object.put(Const.FILTERIP,filterIP);
                    break;
                default:
                    object.put(nameElem.getTextTrim(),valueElem.getTextTrim());
                    break;
            }
        }
    }

    public String xml2Json() {
        return object.toJSONString();
    }

    public void listAll() {
        System.out.println(object.toJSONString());
    }
}
