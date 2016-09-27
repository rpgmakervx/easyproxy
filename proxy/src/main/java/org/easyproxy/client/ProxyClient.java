package org.easyproxy.client;/**
 * Description : 
 * Created by YangZH on 16-5-25
 *  下午10:47
 */

import io.netty.handler.codec.http.HttpHeaders;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description :
 * Created by YangZH on 16-5-25
 * 下午10:47
 */

public class ProxyClient {


    CloseableHttpClient httpclient = HttpClients.createDefault();
    CloseableHttpResponse response;
    HttpPost httpPost;
    HttpGet httpGet;
    HttpPut httpPut;
    HttpDelete httpDelete;
    HttpHost httpHost;
    String host;
    String HOST;

    public ProxyClient(InetSocketAddress address, String uri) {
        this.host = HOST = address.getHostName();
        boolean isLocal = host.equals("localhost");
        httpHost = new HttpHost(host, address.getPort());
        String url = "http://";
        if (isLocal) {
            url += host + ":" + address.getPort() + uri;
        } else if (!host.contains("http://")) {
            url += host + ":" + address.getPort() + uri;
        } else {
            url = "http://" + host + ":" + address.getPort() + uri;
        }
        httpPost = new HttpPost(url);
        httpGet = new HttpGet(url);
        httpPut = new HttpPut(url);
        httpDelete = new HttpDelete(url);
    }

    /**
     * 组装http的请求头(适配netty的请求头和httpclient的请求头)
     *
     * @param headers
     * @return
     */
    public CloseableHttpResponse makeResponse(HttpHeaders headers) {
        setHeader(httpGet, headers);
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private List<NameValuePair> setRequestData(Map<String, Object> params) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));
        }
        return nameValuePairs;
    }

    private HttpEntity setMultipartEntity(Map<String, Object> param) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                StringBody sb = null;
                try {
                    sb = new StringBody((String) entry.getValue(), Charset.forName("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                builder.addPart(entry.getKey(),sb);
                System.out.println("key:" + entry.getKey() + ", value:" + value);
            } else if (value instanceof File) {
                System.out.println("key:"+entry.getKey()+", value:"+value);
                builder.addBinaryBody(entry.getKey(), (File) value);
            }
        }
        return builder.build();
    }

    private void setHeader(HttpRequestBase httpRequest, HttpHeaders headers) {
//        System.out.println("REQUEST header ---------------");
        for (CharSequence name : headers.names()) {
            String n = name.toString();
            boolean exclusive = n.equalsIgnoreCase("Content-Length")
                    || n.equalsIgnoreCase("Referer")
                    || n.equalsIgnoreCase("If-Modified-Since")
                    || n.equalsIgnoreCase("If-None-Match");
            if (!exclusive) {
                httpRequest.setHeader(name.toString(), headers.get(name).toString());
//                System.out.println(name.toString() + ":" + headers.get(name).toString());
            }
        }
//        System.out.println("END header ---------------");
    }

    /**
     * post请求 json格式
     *
     * @param json
     * @param headers
     * @return
     */
    public CloseableHttpResponse postJsonRequest(String json, HttpHeaders headers) {
        setHeader(httpPost, headers);
        StringEntity s = null;
        CloseableHttpResponse response = null;
        try {
            s = new StringEntity(json,Charset.forName("UTF-8"));
            httpPost.setEntity(s);
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 表单提交
     *
     * @param param
     * @param headers
     * @return
     */
    public CloseableHttpResponse postMultipartEntityRequest(Map<String, Object> param, HttpHeaders headers) {
//        setHeader(httpPost, headers);
        CloseableHttpResponse response = null;
        try {
            HttpEntity entity = setMultipartEntity(param);
            httpPost.setEntity(entity);
            response = httpclient.execute(httpPost);
            System.out.println("file upload response code:"+response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public HttpEntity setFormDataEntity(Map<String,Object> param){
        MultipartEntity entity = new MultipartEntity();
        for (Map.Entry<String,Object> entry:param.entrySet()){
            Object value = entry.getValue();
            String key = entry.getKey();
            if (value instanceof String){
                StringBody sb = new StringBody((String) value, ContentType.MULTIPART_FORM_DATA);
                entity.addPart(key,sb);
            }else if (value instanceof byte[]){
            }
        }
        return entity;
    }

    /**
     * post请求
     *
     * @param param
     * @param headers
     * @return
     */
    public CloseableHttpResponse postEntityRequest(Map<String, Object> param, HttpHeaders headers) {
        setHeader(httpPost, headers);
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(setRequestData(param), "UTF-8"));
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getResponse(CloseableHttpResponse response) {
        String body = "";
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            try {
                body = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }

    public byte[] getByteResponse(CloseableHttpResponse response) {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            try {
                byte[] body = EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity);
                //释放链接
                return body;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public CloseableHttpResponse deleteRequest(HttpHeaders headers){
        setHeader(httpDelete, headers);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public CloseableHttpResponse putJsonRequest(String json, HttpHeaders headers) {
        setHeader(httpPut, headers);
        StringEntity s = null;
        CloseableHttpResponse response = null;
        try {
            s = new StringEntity(json);
            httpPut.setEntity(s);
            response = httpclient.execute(httpPut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public CloseableHttpResponse putEntityRequest(Map<String, Object> param, HttpHeaders headers) {
        setHeader(httpPut, headers);
        CloseableHttpResponse response = null;
        try {
            httpPut.setEntity(new UrlEncodedFormEntity(setRequestData(param), "UTF-8"));
            response = httpclient.execute(httpPut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
//        ProxyClient client = new ProxyClient(new InetSocketAddress("localhost",8080),"/tomcat.png");
        HttpPost post = new HttpPost("localhost:8080");
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet("localhost:8080");
    }

}
