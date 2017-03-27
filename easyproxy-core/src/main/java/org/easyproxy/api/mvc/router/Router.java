package org.easyproxy.api.mvc.router;

import org.easyproxy.api.http.protocol.HttpMethod;
import org.easyproxy.api.kits.StringKits;
import org.easyproxy.api.mvc.action.ActionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingtianyu on 17-3-12
 * 下午2:45
 * description:
 */

public class Router {

    private static final String SEPARATOR = "/";
    private static final String QUESTION = "?";
    public static final String POINT = ".";
    public static final String LEFT = "{";
    public static final String RIGHT = "}";
    public static final String WILDCARD = "*";

    private String path;

    private List<String> segements;

    private HttpMethod method;

    private Map<Integer,String> parameterizeUrl;

    private Map<String,String> pathParams;

    private boolean parameterize;

    private ActionType type;

    private int level;

    /**
     * 地址最后一个不带分隔符
     * @param router
     */
    public Router(String router, ActionType type){
        this.path = checkURL(router);
        this.type = type;
        this.method = HttpMethod.GET;
        this.parameterizeUrl = new HashMap<>();
        this.pathParams = new HashMap<>();
        this.segements = new ArrayList<>();
        parse(router);
    }
    public Router(String router, ActionType type,HttpMethod method){
        this.path = checkURL(router);
        this.type = type;
        this.method = method;
        this.parameterizeUrl = new HashMap<>();
        this.pathParams = new HashMap<>();
        this.segements = new ArrayList<>();
        parse(router);
    }

    private String checkURL(String url){
        if (StringKits.isEmpty(url)){
            return null;
        }
        String path = null;
        if (!url.equals(SEPARATOR)&&url.endsWith(SEPARATOR)){
            path = url.substring(0,url.length() - 1);
        }else if (!url.startsWith(SEPARATOR)&&!url.contains(WILDCARD)){
            path = "/"+url;
        }else{
            path = url;
        }
        int index = url.lastIndexOf(QUESTION);
        if (index == -1){
            return path;
        }
        return path.substring(0,index);
    }

    private void parse(String path){
        if (StringKits.isEmpty(path)){
            return;
        }
        if (!path.contains(SEPARATOR)&&!path.isEmpty()){
            if (path.startsWith(LEFT)&&path.endsWith(RIGHT)){
                this.parameterize = true;
                parameterizeUrl.put(0,path);
            }
            this.level++;
            this.segements.add(path);
        }
        String[] segements = path.split(SEPARATOR);
        for (String block:segements){
            if (block.startsWith(LEFT)&&block.endsWith(RIGHT)){
                this.parameterize = true;
                parameterizeUrl.put(level,block);
            }
            if (!StringKits.isEmpty(block)){
                this.level++;
                this.segements.add(block);
            }
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public int getParameterizeSize(){
        return parameterizeUrl.size();
    }

    public boolean isParameterize() {
        return parameterize;
    }

    public void setParameterize(boolean parameterize) {
        this.parameterize = parameterize;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Map<String,String> getPathParams(){
        return this.pathParams;
    }

    public List<String> getSegements() {
        return segements;
    }

    public void setSegements(List<String> segements) {
        this.segements = segements;
    }

    public Map<Integer, String> getParameterizeUrl() {
        return parameterizeUrl;
    }

    public void setParameterizeUrl(Map<Integer, String> parameterizeUrl) {
        this.parameterizeUrl = parameterizeUrl;
    }

    public void setPathParams(Map<String, String> pathParams) {
        this.pathParams = pathParams;
    }

    public static int getLevel(String path){
        if (StringKits.isEmpty(path)){
            return 0;
        }
        String[] segements = path.split(SEPARATOR);
        int level = 0;
        for (String block:segements){
            if (!StringKits.isEmpty(block)){
                level++;
            }
        }
        return level;
    }

    @Override
    public String toString() {
        return "Router{" +
                "path='" + path + '\'' +
                ", method=" + method +
                ", level=" + level +
                ", segements=" + segements +
                '}';
    }

}