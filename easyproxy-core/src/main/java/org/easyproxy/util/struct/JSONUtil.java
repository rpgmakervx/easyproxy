package org.easyproxy.util.struct;/**
 * Description : 
 * Created by YangZH on 16-5-29
 *  下午5:50
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description :
 * Created by YangZH on 16-5-29
 * 下午5:50
 */

public class JSONUtil {

    public static boolean isJson(String string) {
        try {
            JSON.parseObject(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String list2Json(List list) {
        if (CollectionUtils.isEmpty(list))
            return "";
        return JSON.parseArray(JSON.toJSONString(list, true)).toString();
    }

    public static<T> List<T> json2List(String json, Class<T> cls){
        if (StringUtils.isEmpty(json)){
            return new ArrayList<T>();
        }
        return JSON.parseArray(json,cls);
    }

    public static String pojo2Json(Object object) {
        if (object == null)
            return "";
        return JSON.toJSONString(object, true);
    }
    public static<T> T json2Pojo(String json,Class<T> clz) {
        if (StringUtils.isEmpty(json))
            return null;
        return JSONObject.parseObject(json,clz);
    }

    public static String map2Json(Map<String,Object> jsonMap){
        if (MapUtils.isEmpty(jsonMap))
            return "";
        JSONObject jsonObject = new JSONObject(jsonMap);
        return jsonObject.toJSONString();
    }
    public static Map<String,Object> json2Map(String json){
        if (StringUtils.isEmpty(json))
            return null;
        JSONObject object = str2Json(json);
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        for (Map.Entry<String,Object> entry:object.entrySet()){
            jsonMap.put(entry.getKey(),entry.getValue());
        }
        return jsonMap;
    }

    public static JSONObject str2Json(String str) {
        if (StringUtils.isEmpty(str))
            return null;
        return JSON.parseObject(str);
    }

    public static<T> T map2Pojo(Map<String,Object> jsonMap,Class<T> clz){
        if (MapUtils.isEmpty(jsonMap))
            return null;
        return json2Pojo(map2Json(jsonMap),clz);
    }

    public static Map<String,Object> pojo2Map(Object object){
        if (object == null)
            return null;
        return json2Map(pojo2Json(object));
    }

    public static String addKV(String json,String key,String value){
        Map<String,Object> jsonMap = json2Map(json);
        jsonMap.put(key,value);
        return map2Json(jsonMap);
    }

    public static String format(String str){
        return JSON.toJSONString(str.trim());
    }

    public static JSONArray getArrayFromStr(String key, String json) {
        JSONObject object = JSON.parseObject(json);
        return (JSONArray) object.get(key);
    }

    public static JSONArray getArrayFromJSON(String key, JSONObject json) {
        return (JSONArray) json.get(key);
    }
    public static List getListFromJson(String key, JSONObject json) {
        return (List) json.get(key);
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("Server","Apache-Coyote/1.1");
        map.put("Content-Type","Apache-Coyote/1.1");
        map.put("Content-Language","zh-CN");
        System.out.println(map2Json(map));
    }
}
