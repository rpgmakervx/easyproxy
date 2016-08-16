package compress;/**
 * Description : 
 * Created by YangZH on 16-8-16
 *  上午1:29
 */

import org.easyproxy.compress.Compressor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * Description :
 * Created by YangZH on 16-8-16
 * 上午1:29
 */

public class CompressTest {

//    @Test
    public static void main(String n[]) throws Exception {
        Compressor compressor = new Compressor();
        FileInputStream fis = new FileInputStream("/home/code4j/picture/邢天宇.jpg");
        FileInputStream fis1 = new FileInputStream("/home/code4j/java_error_in_IDEA_7188.log");
        FileInputStream fis2 = new FileInputStream("/home/code4j/util/apache-maven-3.3.9-bin.zip");
        FileInputStream fis3 = new FileInputStream("/home/code4j/桌面/独立日：卷土重来[DVD版]_bd.mp4");
        BufferedInputStream bis = new BufferedInputStream(fis3);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
//        fis.read()
        byte[] bytes = new byte[10240];
        int len=0;
        while ((len=bis.read(bytes))!=-1){
            bos.write(bytes,0,len);
        }
//        baos.write(bytes,0,fis.available());
        System.out.println("origin size:"+baos.toByteArray().length);
        byte[] result = compressor.gzip(baos.toByteArray());
        System.out.println("compressed size:"+result.length);
        System.out.println("sub: "+(baos.toByteArray().length-result.length));
        byte[] result2 = compressor.unGzip(result);
        System.out.println("uncompressed size:"+result2.length);
//        System.out.println(new String(result2));
    }
}
