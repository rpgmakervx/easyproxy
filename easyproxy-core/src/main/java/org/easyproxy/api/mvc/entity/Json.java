package org.easyproxy.api.mvc.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.easyproxy.api.kits.StringKits;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Description :
 * Created by xingtianyu on 17-2-27
 * 下午5:12
 * description:
 */

public class Json<V> {

    private Map<String,V> jsonMap = new HashMap<String, V>();

    public Json(){

    }
    public Json(Object ... datas){
        init(datas);
    }

    public Json(Map<String,V> map){
        this.jsonMap = map;
    }


    private void init(Object ... datas){
        int index = 0;
        Object tmp = null;
        for (Object data:datas){
            if (index%2 == 1){
                jsonMap.put(String.valueOf(tmp), (V) data);
            }
            tmp = data;
            index++;
        }
    }

    private static Map<String,Object> toMap(String json){
        if (StringKits.isEmpty(json)){
            return null;
        }
        JSONObject object = JSON.parseObject(json);
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        for (Map.Entry<String,Object> entry:object.entrySet()){
            jsonMap.put(entry.getKey(),entry.getValue());
        }
        return jsonMap;
    }
    public static String stringify(Map<String,Object> jsonMap){
        if (jsonMap ==null||jsonMap.size()== 0){
            return "";
        }
        JSONObject jsonObject = new JSONObject(jsonMap);
        return jsonObject.toJSONString();
    }

    public static Json parse(String str){
        Json json = new Json(toMap(str));
        return json;
    }

    public static String stringify(Json json){
        Map<String,Object> map = json.getJsonMap();
        return stringify(map);
    }

    public static<T> byte[] serial(T bean){
        return JSONObject.toJSONBytes(bean, SerializerFeature.PrettyFormat);
    }

    public static<T> T deserial(byte[] json,Class<T> cls){
        return (T) JSONObject.parse(json, Feature.OrderedField);
    }

    public Map<String, V> getJsonMap() {
        return jsonMap;
    }

    public int size() {
        return jsonMap.size();
    }

    public boolean isEmpty() {
        return jsonMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return jsonMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return jsonMap.containsValue(value);
    }

    public V get(Object key) {
        return jsonMap.get(key);
    }

    public V put(String key, V value) {
        return jsonMap.put(key, value);
    }

    public V remove(Object key) {
        return jsonMap.remove(key);
    }

    public void putAll(Map<? extends String, ? extends V> m) {
        jsonMap.putAll(m);
    }

    public void clear() {
        jsonMap.clear();
    }

    public Set<String> keySet() {
        return jsonMap.keySet();
    }

    public Collection<V> values() {
        return jsonMap.values();
    }

    public Set<Map.Entry<String, V>> entrySet() {
        return jsonMap.entrySet();
    }

    public V getOrDefault(Object key, V defaultValue) {
        return jsonMap.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super String, ? super V> action) {
        jsonMap.forEach(action);
    }

    public void replaceAll(BiFunction<? super String, ? super V, ? extends V> function) {
        jsonMap.replaceAll(function);
    }

    public V putIfAbsent(String key, V value) {
        return jsonMap.putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return jsonMap.remove(key, value);
    }

    public boolean replace(String key, V oldValue, V newValue) {
        return jsonMap.replace(key, oldValue, newValue);
    }

    public V replace(String key, V value) {
        return jsonMap.replace(key, value);
    }

    public V computeIfAbsent(String key, Function<? super String, ? extends V> mappingFunction) {
        return jsonMap.computeIfAbsent(key, mappingFunction);
    }

    public V computeIfPresent(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
        return jsonMap.computeIfPresent(key, remappingFunction);
    }

    public V compute(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
        return jsonMap.compute(key, remappingFunction);
    }

    public V merge(String key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return jsonMap.merge(key, value, remappingFunction);
    }

}
