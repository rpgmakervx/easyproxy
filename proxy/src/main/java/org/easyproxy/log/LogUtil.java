package org.easyproxy.log;/**
 * Description : 
 * Created by YangZH on 16-8-17
 *  上午3:01
 */

import org.easyproxy.constants.Const;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description :
 * Created by YangZH on 16-8-17
 * 上午3:01
 */

public class LogUtil {

    private ExecutorService threadPool;

    public LogUtil(){
        threadPool  = Executors.newCachedThreadPool();
    }

    public void accessLog(final String log){
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(Const.ACCESSLOG,true));
                    writer.write(log);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
