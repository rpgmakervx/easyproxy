package org.easyproxy.resources;/**
 * Description : 
 * Created by YangZH on 16-8-22
 *  上午1:08
 */

import org.easyproxy.config.XmlConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static org.easyproxy.constants.Const.*;
/**
 * Description :
 * Created by YangZH on 16-8-22
 * 上午1:08
 */

public class Resource {

    public static byte[] getPage(String path){
        File page = new File(path);
        if (!page.exists()){
            return getResource(CODE_NOTFOUND);
        }
        byte[] bytes = new byte[(int) page.length()];
        try {
            FileChannel channel = new FileInputStream(page).getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, page.length());
            buffer.get(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static byte[] getResource(int code){
        String resourceName = chooseResource(code);
        try {
            File resource = new File(resourceName);
            if (!resource.isDirectory()&&!resource.exists()){
                System.out.println("Resource Not Found!!");
                resource = new File(RESOURCES+ XmlConfig.getString(NOTFOUND_PAGE));
            }
            FileInputStream fis = new FileInputStream(resource);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fis.read(bytes)) != -1){
                baos.write(bytes,0,len);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String chooseResource(int code){
        StringBuffer buffer = new StringBuffer(RESOURCES);
        switch (code){
            case CODE_NOTFOUND:
                buffer.append(XmlConfig.getString(NOTFOUND_PAGE));
                break;
            case CODE_FORBIDDEN:
                buffer.append(XmlConfig.getString(FORBIDDEN_PAGE));
                break;
            case CODE_SERVERERROR:
                buffer.append(XmlConfig.getString(ERROR_PAGE));
                break;
            case CODE_BADREQUEST:
                buffer.append(XmlConfig.getString(BADREQUEST_PAGE));
                break;
            default:
                buffer.append(XmlConfig.getString(NOTFOUND_PAGE));
                break;
        }
        return buffer.toString();
    }
}
