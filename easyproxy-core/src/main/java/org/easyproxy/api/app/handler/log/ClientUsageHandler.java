package org.easyproxy.api.app.handler.log;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyproxy.api.app.util.LogUtil;

import java.io.IOException;
import java.util.List;

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
        for (String asString:contents){
            UserAgentInfo userAgentInfo = uasParser.parse(asString);
        }
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
