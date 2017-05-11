package org.easyproxy.api.app.handler.log;

import org.easyarch.netpet.kits.file.FileKits;
import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;
import org.easyproxy.api.app.pojo.DailyActiveLog;
import org.easyproxy.api.app.util.LogUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xingtianyu on 17-5-5
 * 下午5:36
 * description:
 */

public class DailyActiveHandler implements HttpHandler {

    @Override
    public void handle(HandlerRequest request, HandlerResponse response) throws Exception {
        List<File> logs = LogUtil.getLogs();
        List<Map<String,Object>> records = new ArrayList<>();
        List<DailyActiveLog> recordLogs = new ArrayList<>();
        for (File log:logs){
            int count = FileKits.statistic(log,"\n");
            String date = log.getName().split("\\.")[1];
            recordLogs.add(new DailyActiveLog(date,count));
        }
        recordLogs.sort(new Comparator<DailyActiveLog>() {
            @Override
            public int compare(DailyActiveLog a, DailyActiveLog b) {
                return a.getDay().compareTo(b.getDay());
            }
        });
        for (DailyActiveLog log:recordLogs){
            records.add(Json.parse(log).getJsonMap());
        }
        response.json(new Json("data",records));
    }


    public static void main(String[] args) throws Exception {
        List<File> logs =  FileKits.filter("/home/code4j/IDEAWorkspace/easyproxy/logs", new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                Pattern pattern = Pattern.compile("access\\.(\\d){4}-(\\d){2}-(\\d){2}\\.log");
                Matcher matcher = pattern.matcher(pathname.getName());
                return matcher.matches();
            }
        });
        System.out.println(logs);
    }
}
