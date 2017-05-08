package org.easyproxy.log;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import org.easyproxy.constants.Const;
import org.easyproxy.util.time.TimeUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.easyproxy.constants.Const.ACCESSLOG;
/**
 * Description :
 * Created by YangZH on 16-8-17
 * 上午3:01
 */

public class Logger {

    private ExecutorService threadPool;

    private static Logger logger ;

    public static Logger getLogger(){
        synchronized (Logger.class){
            if (logger==null){
                logger = new Logger();
            }
        }
        return logger;
    }

    public Logger(){
        threadPool  = Executors.newCachedThreadPool();
    }

    public void accessLog(final String log){
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    File logFile = new File(ACCESSLOG+"."+ TimeUtil.getFormattedDate(new Date())+".log");
                    if (!logFile.exists()){
                        logFile.createNewFile();
                    }
                    BufferedWriter writer = new BufferedWriter(new FileWriter(logFile,true));
                    writer.write(log);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void accessLog(HttpRequest request, String client_ip,int status){
        String log = generateLog(request,client_ip,status);
        accessLog(log);
    }

    /**
     * 192.168.1.1|+|2017-05-04 14:09:15|+|GET|+|/index|+|
     * @param request
     * @param client_ip
     */
    private String generateLog(HttpRequest request,String client_ip,int status){
        HttpHeaders headers = request.headers();
        StringBuffer buffer = new StringBuffer();
        buffer.append(client_ip+ Const.LOGSEPARATOR);
        buffer.append(TimeUtil.getFormattedTime(new Date())+Const.LOGSEPARATOR);
        buffer.append(request.method().name()+Const.LOGSEPARATOR);
        buffer.append(request.uri()+Const.LOGSEPARATOR);
        buffer.append(status+Const.LOGSEPARATOR);
        buffer.append(headers.get(HttpHeaderNames.USER_AGENT)+"\n");
        return buffer.toString();
    }
}
