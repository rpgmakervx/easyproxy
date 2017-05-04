package org.easyproxy.log;

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
        threadPool  = Executors.newFixedThreadPool(8*2);
    }

    public void accessLog(final String log){
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    File logFile = new File(ACCESSLOG+ TimeUtil.getFormattedDate(new Date())+".log");
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
}
