package com.prowo.persist.util;

import java.security.MessageDigest;

/**
 * MD5签名工具类
 */
public class MD5Utils {
    /**
     * 对数据进行MD5签名
     *
     * @param data
     * 要签名的数据
     * @return 对数据的MD5签名值的Base64编码字符串
     */

    public static char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5sign(String data) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.reset();
            messagedigest.update(data.getBytes("UTF8"));
            byte abyte0[] = messagedigest.digest();
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = abyte0[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获得密文时出错！");
        }
    }

    public static void main(String[] args) {
        System.out.println(md5sign("kvsoft2008xxxx"));
    }
}
