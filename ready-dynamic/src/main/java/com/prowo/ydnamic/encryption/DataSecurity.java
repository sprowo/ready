package com.prowo.ydnamic.encryption;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import org.apache.commons.codec.binary.Base64;

public class DataSecurity {

    /***
     * 订单数据加密
     *
     * @param name1
     *            合作商字段名称
     * @param name2
     *            数据字段名称
     * @param name3
     *            验证字段名称
     * @param partnerid
     *            合作商ID
     * @param data
     *            源数据
     * @param password
     *            密码
     * @return 加密后的数据
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String security(String name1, String name2, String name3, String partnerid, String password, String data) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        data = new String(Base64.encode(data));

        // 签名内容 = base64(data) + partnerid + password;
        String validation = data + partnerid + password;
        validation = md5(validation);

        partnerid = URLEncoder.encode(partnerid, "UTF-8");
        data = URLEncoder.encode(data, "UTF-8");
        validation = URLEncoder.encode(validation, "UTF-8");

        return new StringBuffer().append(name1).append("=").append(partnerid).append("&").append(name2).append("=").append(data).append("&").append(name3).append("=")
                .append(validation).toString();
    }

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
