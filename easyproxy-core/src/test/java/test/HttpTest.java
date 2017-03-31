package test;/**
 * Description : 
 * Created by YangZH on 16-9-26
 *  上午11:12
 */

import org.easyarch.netpet.asynclient.AsyncHttpClient;
import org.junit.jupiter.api.Test;

/**
 * Description :
 * Created by YangZH on 16-9-26
 * 上午11:12
 */

public class HttpTest {

    @Test
    public void testJson(){
        AsyncHttpClient client = new AsyncHttpClient("http://localhost:8800/index/user");
    }

//    @Test
//    public void test() throws IOException {
//        CloseableHttpClient httpclient = HttpClients.createDefault();
////        HttpPost httpPost = new HttpPost("http://localhost:8080/dobehub/activity/5623365215/add");
//        HttpPost httpPost = new HttpPost("http://localhost:8080/dobehub/activity/5623365215/add");
//        Map<String,Object> param = new HashMap<>();
//        param.put("content","2016.9.26 23:38 测试httpclient");
//        File file = new File("/home/code4j/picture/50k.jpg");
//        FileInputStream fis = new FileInputStream(file);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] bytes = new byte[1024];
//        int len = 0;
//        while ((len = fis.read(bytes)) != -1){
//            baos.write(bytes,0,len);
//        }
//        param.put("picture",file);
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        for (Map.Entry<String, Object> entry : param.entrySet()) {
//            Object value = entry.getValue();
//            if (value instanceof String) {
////                StringBody sb = new StringBody(, ContentType.MULTIPART_FORM_DATA);
////                builder.addPart(entry.getKey(), sb);
//                String v = (String) value;
//                builder.addTextBody(entry.getKey(), new String(v.getBytes(Charset.forName("UTF-8"))));
//                System.out.println("key:" + entry.getKey() + ", value:" + value);
//            } else if (value instanceof byte[]) {
//                System.out.println("key:"+entry.getKey()+", value:"+value);
//                FileBody body = new FileBody(file);
//                builder.addPart(entry.getKey(), body);
//            }
//        }
//        HttpEntity entity = builder.build();
//        httpPost.setEntity(entity);
//        CloseableHttpResponse response = httpclient.execute(httpPost);
//        System.out.println("result:\n" + EntityUtils.toString(response.getEntity()));
//        //释放链接
//        response.close();
//    }
//
//    @Test
//    public void testTmpFile(){
//        System.out.println(Const.TMP);
//        File file = new File(Const.TMP+"file");
//        try {
//            file.createNewFile();
//            System.out.println(file.getName());
////            System.in.read();
////            file.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
