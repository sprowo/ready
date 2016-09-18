package com.prowo.ydnamic.ipseek;

/**
 * 查找IP范围：获取中国--省一级
 */
public class MyIPSeeker {

    private static IPSeeker seeker = IPSeeker.getInstance();

    public static Province getIPAddress(String ip) {
        String address = seeker.getCountry(ip);
        for (Province province : Province.values()) {
            if (address.contains(province.getName())) {
                return province;
            }
        }
        return Province.SH;
    }

    public static void reload() {
        seeker = IPSeeker.getInstance();
    }

    public static void main(String[] args) {
        Province province = getIPAddress("58.40.18.94");
        System.out.println(province.getName());
    }

}
