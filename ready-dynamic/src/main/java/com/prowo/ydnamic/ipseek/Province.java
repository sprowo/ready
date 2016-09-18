package com.prowo.ydnamic.ipseek;

public enum Province {

    HL("黑龙江"), JL("吉林"), LN("辽宁"), BJ("北京"), NM("内蒙古"), XJ("新疆"), GS("甘肃"), NX("宁夏"), SA("陕西"), SX("山西"), HB("河北"), TJ("天津"), QH("青海"), SD("山东"), HE("河南"), AH("安徽"), JS("江苏"), SH(
            "上海"), XZ("西藏"), SC("四川"), CQ("重庆"), HU("湖北"), HN("湖南"), JX("江西"), ZJ("浙江"), YN("云南"), GZ("贵州"), GX("广西"), GD("广东"), FJ("福建"), HA("海南"), MA("澳门"), HK("香港"), TA("台湾");

    private final String name;

    private Province(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
