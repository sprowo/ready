package com.prowo.ydnamic.encryption;

public class RC4 {
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String encode(String key, String data) {
        return bytesToHexString(rc4(key, data.getBytes()));
    }

    public static String decode(String key, String data) {
        return new String(rc4(key, hexStringToBytes(data)));
    }

    public static byte[] rc4(String key, byte[] data) {
        int[] box = new int[256];
        byte[] k = key.getBytes();
        int i = 0, x = 0, t = 0, l = k.length;

        for (i = 0; i < 256; i++) {
            box[i] = i;
        }

        for (i = 0; i < 256; i++) {
            x = (x + box[i] + k[i % l]) % 256;

            t = box[x];
            box[x] = box[i];
            box[i] = t;
        }

        //
        t = 0;
        i = 0;
        l = data.length;
        int o = 0, j = 0;
        byte[] out = new byte[l];
        int[] ibox = new int[256];
        System.arraycopy(box, 0, ibox, 0, 256);

        for (int c = 0; c < l; c++) {
            i = (i + 1) % 256;
            j = (j + ibox[i]) % 256;

            t = ibox[j];
            ibox[j] = ibox[i];
            ibox[i] = t;

            o = ibox[(ibox[i] + ibox[j]) % 256];
            out[c] = (byte) (data[c] ^ o);
        }
        return out;
    }
}