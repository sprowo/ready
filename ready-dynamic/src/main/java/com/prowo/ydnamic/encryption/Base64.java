package com.prowo.ydnamic.encryption;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;


public class Base64 {

    public static String encode(byte[] origin) {
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(origin));
    }

    public static String encode(String origin) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        return encode(origin.getBytes("UTF-8"));
    }

    public static String decode(String target) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(target)) {
            return "";
        }
        return new String(decodeBuffer(target), "UTF-8");
    }

    /**
     * <pre>
     * 解决Unexpected end-of-input: was expecting closing quote for a string value at [Source: java.io.StringReader@15075f9; line: 1, column: 107]
     * 问题 例如：修字
     * duzhiyong@yundaex.com
     * </pre>
     *
     * @param target
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decodeSafe(String target) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(target)) {
            return "";
        }
        return decode(target.replace(" ", "+"));
    }

    public static byte[] decodeBuffer(String target) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(target);
    }

}
