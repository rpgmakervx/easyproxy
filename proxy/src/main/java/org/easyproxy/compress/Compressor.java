package org.easyproxy.compress;/**
 * Description : 
 * Created by YangZH on 16-8-16
 *  上午12:35
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Description :
 * Created by YangZH on 16-8-16
 * 上午12:35
 */

public class Compressor {

    private int BUFFER_SIZE = 1024;
    public byte[] gzip(byte[] data){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(baos);
            int len = 0;
            byte[] bytes = new byte[BUFFER_SIZE];
            while ((len=bais.read(bytes,0,BUFFER_SIZE)) != -1){
                gzip.write(bytes,0,len);
            }
            gzip.finish();
            gzip.flush();
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public byte[] unGzip(byte[] data){
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            GZIPInputStream gzip = new GZIPInputStream(bais);
            byte[] bytes = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len=gzip.read(bytes)) != -1){
                baos.write(bytes,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
