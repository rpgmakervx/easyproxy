package org.easyproxy.api.app.handler.log;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;
import org.easyproxy.api.app.pojo.BrowserLog;
import org.easyproxy.api.app.pojo.OsLog;
import org.easyproxy.api.app.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingtianyu on 17-5-10
 * 上午12:12
 * description:
 */

public class ClientUsageHandler implements HttpHandler {

    private UASparser uasParser;

    public ClientUsageHandler() throws IOException {
        uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
    }

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        List<String> contents = LogUtil.getAllContents();
        Json respData = new Json();
        List<BrowserLog> browserLogs = new ArrayList<>();
        List<OsLog> osLogs = new ArrayList<>();
        Map<String,Integer> browsers = new HashMap<>();
        Map<String,Integer> systems = new HashMap<>();
        for (String asString:contents){
            UserAgentInfo userAgentInfo = uasParser.parse(asString);
            String browserName = userAgentInfo.getUaFamily();
            String osName = userAgentInfo.getOsName();
            Integer bCount = browsers.get(browserName);
            Integer oCount = systems.get(osName);

            if (bCount != null){
                bCount++;
            }else{
                bCount = 1;
            }
            browsers.put(browserName,bCount);
            if (oCount != null){
                oCount++;
            }else{
                oCount = 1;
            }
            systems.put(osName,oCount);
        }
        for (Map.Entry<String,Integer> bEntity:browsers.entrySet()){
            browserLogs.add(new BrowserLog(bEntity.getKey(),bEntity.getValue()));
        }
        for (Map.Entry<String,Integer> oEntity:systems.entrySet()){
            osLogs.add(new OsLog(oEntity.getKey(),oEntity.getValue()));
        }
        respData.put("browsers",browserLogs);
        respData.put("systems",osLogs);
        response.json(respData);
    }

    public static void main(String[] args) throws Exception {
        UASparser uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
        List<String> contents = LogUtil.getAllContents();
        int i = 0;
        for (String asString:contents){
            UserAgentInfo userAgentInfo = uasParser.parse(asString);
            System.out.println("操作系统名称："+userAgentInfo.getOsFamily());//
            System.out.println("操作系统："+userAgentInfo.getOsName());//
            System.out.println("浏览器名称："+userAgentInfo.getUaFamily());//
            System.out.println("浏览器版本："+userAgentInfo.getBrowserVersionInfo());//
            System.out.println("设备类型："+userAgentInfo.getDeviceType());
            System.out.println("浏览器:"+userAgentInfo.getUaName());
            System.out.println("类型："+userAgentInfo.getType());
            System.out.println("------------------------------");
            i++;
        }
        System.out.println(i);
    }
}
