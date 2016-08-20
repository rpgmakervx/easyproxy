package org.easyproxy.log;/**
 * Description : 
 * Created by YangZH on 16-8-17
 *  上午3:01
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
                    BufferedWriter writer = new BufferedWriter(new FileWriter(ACCESSLOG,true));
                    writer.write(log);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
