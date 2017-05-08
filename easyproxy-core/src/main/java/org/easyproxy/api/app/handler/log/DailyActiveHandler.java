package org.easyproxy.api.app.handler.log;

import org.easyarch.netpet.kits.file.FileKits;
import org.easyarch.netpet.web.http.request.HandlerRequest;
import org.easyarch.netpet.web.http.response.HandlerResponse;
import org.easyarch.netpet.web.mvc.action.handler.HttpHandler;
import org.easyarch.netpet.web.mvc.entity.Json;
import org.easyproxy.constants.Const;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
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
        List<File> logs =  FileKits.filter(Const.LOGS, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                Pattern pattern = Pattern.compile("access\\.(\\d){4}-(\\d){2}-(\\d){2}\\.log");
                Matcher matcher = pattern.matcher(pathname.getName());
                return matcher.matches();
            }
        });
        List<Map<String,Object>> records = new ArrayList<>();
        for (File log:logs){
            int count = FileKits.statistic(log,"\n");
            String date = log.getName().split("\\.")[1];
            records.add(new Json("day",date,"count",count).getJsonMap());
        }
        response.json(new Json("data",records));
    }


    public static void main(String[] args) throws Exception {
//        List<Json> jsons = new ArrayList<>();
//        jsons.add(new Json("code",100,"message","a"));
//        jsons.add(new Json("code",102,"message","b"));
//        jsons.add(new Json("code",103,"message","c"));
//        Map<String,Object> map = new HashMap<>();
//        map.put("Data",jsons);
//        System.out.println(Json.stringify(map));
        int count = FileKits.statistic("/home/code4j/osproject/jproxy/logs/access.2017-05-05.log","\n");
        System.out.println("count:"+count);
    }
}
