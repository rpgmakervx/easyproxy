package org.easyproxy.api.kits;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by xingtianyu on 17-3-10
 * 下午12:07
 * description:
 */

public class TimeKits {

    private final static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date parse(String date){
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String format(Date date){
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        return format.format(date);
    }

    public static Date plus(long time,Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MILLISECOND, (int) time);
        return c.getTime();
    }
    public static Date plus(long time) {
        return plus(time,new Date());
    }

    public static Date plusSeconds(int second) {
        return plus(second * 1000);
    }

    public static Date plusSeconds(int second,Date date){
        return plus(second * 1000,date);
    }

    public static String getGMTTime(Date date){
        Calendar cd = Calendar.getInstance();
        cd.setTimeInMillis(date.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(cd.getTime());
    }


}
