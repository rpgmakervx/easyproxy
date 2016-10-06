package org.easyproxy.util.disk;/**
 * Description : 
 * Created by YangZH on 16-9-26
 *  下午11:51
 */

import org.easyproxy.constants.Const;
import org.easyproxy.util.codec.EncryptUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Description :
 * Created by YangZH on 16-9-26
 * 下午11:51
 */

public class FileUtil {

    /**
     * 将字节流保存成临时文件。
     * @param bytes
     */
    public static File tempFile(byte[] bytes,String filename){
        System.out.println("byte length : "+bytes.length);
        File file = new File(Const.TMP+ EncryptUtil.hash(filename)+System.nanoTime());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (RandomAccessFile raf = new RandomAccessFile(file,"rw");
             FileChannel channel = raf.getChannel()){
//            FileInputStream fis;
//            FileOutputStream fos''
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, bytes.length);
            buffer.put(bytes);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void delete(File file){
        file.delete();
    }
}
