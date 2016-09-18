package com.prowo.ydnamic.context;

import java.util.HashMap;
import java.util.Map;

public class OrderContext {

    private static Map<String, String> errors = new HashMap<String, String>();
    private static Map<String, String> status = new HashMap<String, String>();
    private static Map<String, String> descriptions = new HashMap<String, String>();

    static {
        status.put("init", "未处理");
        status.put("refuse", "拒绝订单");
        status.put("accept", "接单");
        status.put("accept_sys", "接单(系统)");
        status.put("getfail", "揽收失败");
        status.put("got", "已揽收");
        status.put("transit", "运输中");
        status.put("deliver", "派件中");
        status.put("signfail", "签收异常");
        status.put("signed", "已送达");
        status.put("shift", "重发/转单");
        status.put("withdraw", "订单取消");

        errors.put("s00", "未知错误");
        errors.put("s01", "非法的合作商账户");
        errors.put("s02", "非法的主机来源");
        errors.put("s03", "非法的数据签名");
        errors.put("s04", "非法的请求类型");
        errors.put("s05", "非法的XML格式");
        errors.put("s06", "非法的订单号");
        errors.put("s07", "账户不具有访问本功能的权限");
        errors.put("s11", "无效的指令操作");
        errors.put("s12", "没有指定有效的查询条件");
        errors.put("s21", "可更新的字段中数据内容一致，忽略更新");
        errors.put("s22", "订单当前状态下不再允许取消");
        errors.put("s23", "订单当前状态下不再允许修改");
        errors.put("s51", "发件人信息不完整");
        errors.put("s52", "收件人信息不完整");
        errors.put("s71", "发件人所在地区服务已关闭");
        errors.put("s72", "收件人所在地区服务已关闭");
        errors.put("s91", "信息不完整");
        errors.put("s97", "数据更新失败");
        errors.put("s98", "数据保存失败");
        errors.put("s99", "服务器错误");
        errors.put("e01", "用户取消投递");
        errors.put("e02", "用户恶意下单");
        errors.put("e03", "黑名单客户");
        errors.put("e11", "揽收地超服务范围");
        errors.put("e12", "派送地超服务范围");
        errors.put("e13", "揽收预约时间超范围，无法协商");
        errors.put("e14", "揽收地址错误或不详");
        errors.put("e15", "派送地址错误或不详");
        errors.put("e16", "多次联系，无法联系上发货方");
        errors.put("e17", "上门后用户不接受价格");
        errors.put("e18", "虚假揽货电话（客户电话与联系人不符）");
        errors.put("e19", "托寄物品为禁限寄品");
        errors.put("e20", "托寄物品超规格");
        errors.put("e21", "无法联系上收件人");
        errors.put("e22", "用户包装问题，取消投递");
        errors.put("e52", "错误收件人联系方式及地址");
        errors.put("e53", "收件人拒收（未验货）");
        errors.put("e54", "收件人拒收（验货，货不对款）");
        errors.put("e55", "收件人拒收（因拖寄物品破损）");
        errors.put("e56", "收件人拒收（代收货款价格不对）");
        errors.put("e57", "收件人拒付或仅愿意部分支付");
        errors.put("e81", "托寄物品丢失");
        errors.put("e99", "其他原因");

        descriptions.put("0", "文件类");
        descriptions.put("1", "电子产品类(包括家用电器)");
        descriptions.put("2", "办公用品类, 服装鞋帽，箱包类");
        descriptions.put("3", "化妆品，美容产品类");
        descriptions.put("4", "珠宝，手表，眼镜，贵重饰品类");
        descriptions.put("5", "食品，保健药品类");
        descriptions.put("6", "工艺品类(包括瓷器，茶具，烹饪用品)");
        descriptions.put("7", "玩具乐器类");
        descriptions.put("8", "其他类");
    }

    public static Map<String, String> getErrors() {
        return errors;
    }

    public static Map<String, String> getStatus() {
        return status;
    }

    public static Map<String, String> getDescriptions() {
        return descriptions;
    }

}
