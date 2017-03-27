package org.easyproxy.util.codec;/**
 * Description : MD5Util
 * Created by YangZH on 16-5-25
 *  下午2:25
 */

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description : MD5Util
 * Created by YangZH on 16-5-25
 * 下午2:25
 */

public class EncryptUtil {

    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String MD5 = "MD5";

    public static String hash(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            if (encName == null || encName.equals("")) {
                encName = "MD5";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); //to HexString
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid algorithm.");
            return null;
        }
        return strDes;
    }

    private static String bytes2Hex(byte[] bts) {
        StringBuffer des = new StringBuffer();
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    public static int ipHash(String key) {
        int hash, i;
        for (hash = 0, i = 0; i < key.length(); ++i) {
            hash += key.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);
        //   return (hash & M_MASK);
        if (hash<=0){
            hash = -hash;
        }
        return hash;
    }

    public static long hash(String key) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance(SHA1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(key.getBytes());
        byte[] bKey = md5.digest();
        long res = ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16) | ((long) (bKey[1] & 0xFF) << 8)
                | (long) (bKey[0] & 0xFF);
        return res & 0xffffffffL;
    }

    public static String encodeBase64(byte[] key) throws Exception {
        if (ArrayUtils.isEmpty(key)){
            return "";
        }
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * BASE64 解密
     * @param key 需要解密的字符串
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] decodeBase64(String key) throws Exception {
        if (StringUtils.isEmpty(key)){
            return null;
        }
        return (new BASE64Decoder()).decodeBuffer(key);
    }
}
