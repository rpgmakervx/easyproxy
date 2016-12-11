package org.easyproxy.util.time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description :
 * Created by xingtianyu on 16-12-11
 * 下午5:15
 */

public class TimeUtil {
    private static SimpleDateFormat completeFormat = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD");

    public static String getNowTime(){
        return completeFormat.format(new Date());
    }

    public static String getFormattedTime(Date date){
        return completeFormat.format(date);
    }

    public static String getFormattedDate(Date date){
        return dateFormat.format(date);
    }
}
