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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
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
            httpPost = new HttpPost(url);
            httpGet = new HttpGet(url);
            HOST = host + ":" + address.getPort();
        } else if (!host.contains("http://")) {
            url += host + ":" + address.getPort() + uri;
            httpPost = new HttpPost(url);
            httpGet = new HttpGet(url);
        } else {
            url = "http://" + host + ":" + address.getPort() + uri;
            httpPost = new HttpPost(host + address.getPort() + uri);
            httpGet = new HttpGet(host + address.getPort() + uri);
        }
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
            System.out.println(" response code : " + response.getStatusLine().getStatusCode());
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
                StringBody sb = new StringBody((String) entry.getValue(), ContentType.MULTIPART_FORM_DATA);
                builder.addPart(entry.getKey(), sb);
            } else if (value instanceof Byte[]) {
                builder.addBinaryBody(entry.getKey(), (byte[]) value);
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
            s = new StringEntity(json);
            httpPost.setEntity(s);
            response = httpclient.execute(httpPost);
            System.out.println(" response code : " + response.getStatusLine().getStatusCode());
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
        setHeader(httpPost, headers);
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(setMultipartEntity(param));
            response = httpclient.execute(httpPost);
            System.out.println(" response code : " + response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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
            System.out.println(" response code : " + response.getStatusLine().getStatusCode());
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
                //释放链接
                response.close();
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
                response.close();
                return body;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
//        ProxyClient client = new ProxyClient(new InetSocketAddress("localhost",8080),"/tomcat.png");
        HttpPost post = new HttpPost("localhost:8080");
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet("localhost:8080");
    }

}
