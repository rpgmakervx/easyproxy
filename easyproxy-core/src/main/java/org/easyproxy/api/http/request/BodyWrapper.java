package org.easyproxy.api.http.request;


import org.easyproxy.api.kits.StringKits;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by xingtianyu on 17-3-21
 * 上午1:06
 * description:
 */

public class BodyWrapper<T> {

    public T getBean(Class<T> cls,Map<String,Object> params) throws Exception {
        T bean = cls.newInstance();
        Field[] fields = cls.getDeclaredFields();
        for (Field field:fields){
            Class<?> type = field.getType();
            String filedName = field.getName();
            if (!params.containsKey(filedName)){
                continue;
            }
            field.setAccessible(true);
            if (type.equals(Integer.class)||type.equals(int.class)){
                String value = String.valueOf(params.get(filedName));
                value = StringKits.isEmpty(value)||value.equals("null")?"0":value;
                field.set(bean,Integer.parseInt(value));
            }else if (type.equals(Float.class)||type.equals(float.class)){
                String value = String.valueOf(filedName);
                value = StringKits.isEmpty(value)||value.equals("null")?"0.0f":value;
                field.set(bean,Float.parseFloat(value));
            }else if (type.equals(Double.class)||type.equals(double.class)){
                String value = String.valueOf(filedName);
                value = StringKits.isEmpty(value)||value.equals("null")?"0.0":value;
                field.set(bean,Double.parseDouble(value));
            }else if (type.equals(Long.class)||type.equals(long.class)){
                String value = String.valueOf(filedName);
                value = StringKits.isEmpty(value)||value.equals("null")?"0l":value;
                field.set(bean,Long.parseLong(value));
            }else if (type.equals(Short.class)||type.equals(short.class)){
                String value = String.valueOf(filedName);
                value = StringKits.isEmpty(value)||value.equals("null")?"0":value;
                field.set(bean,Short.parseShort(value));
            }else if (type.equals(Boolean.class)||type.equals(boolean.class)){
                String value = String.valueOf(filedName);
                value = StringKits.isEmpty(value)||value.equals("null")?"false":value;
                field.set(bean,Boolean.parseBoolean(value));
            }else if (type.equals(Character.class)||type.equals(char.class)){
                char empty = '\0';
                field.set(bean,filedName==null?empty:filedName);
            }else if (type.equals(String.class)){
                String value = String.valueOf(filedName);
                value = StringKits.isEmpty(value)||value.equals("null")?"":value;
                field.set(bean,value);
            }
        }
        return bean;
    }
}
