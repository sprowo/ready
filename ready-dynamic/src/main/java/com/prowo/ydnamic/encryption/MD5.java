package com.prowo.ydnamic.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    /**
     * md5加密方法
     *
     * @param source 源字符串
     * @return 加密后的字符串
     * @throws NoSuchAlgorithmException
     */
    public static String md5(String source) throws NoSuchAlgorithmException {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(source.getBytes());
        byte[] tmp = md.digest();
        char[] str = new char[16 * 2];

        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = tmp[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

}
