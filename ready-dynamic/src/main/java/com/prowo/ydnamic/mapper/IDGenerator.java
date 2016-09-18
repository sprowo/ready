package com.prowo.ydnamic.mapper;

import com.prowo.ydnamic.context.SystemConstants;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

public class IDGenerator {

    public IDGenerator() {
    }

    /**
     * 获得一个UUID,通用的ID生成方式
     *
     * @return String UUID
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();

        StringTokenizer tokenizer = new StringTokenizer(s, "-");
        StringBuilder builder = new StringBuilder();
        int count = tokenizer.countTokens();
        for (int i = 0; i < count; i++) {
            builder.append(tokenizer.nextToken());
        }
        return builder.toString();

        // 去掉“-”符号
        // return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) +
        // s.substring(19, 23) + s.substring(24);
    }

    /**
     * 获得指定数目的UUID
     *
     * @param number int 需要获得的UUID数量
     * @return String[] UUID数组
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }

    /**
     * 客户ID生成规则:yyMMddHHmmssS+手机尾号
     *
     * @param mobile
     * @return
     */
    public static String getId(String mobile) {
        return new SimpleDateFormat(SystemConstants.DATE_FORMAT_IDS_STRING).format(new Date()) + "X" + mobile.substring(7, 11);
    }

    /***
     * 获得订单号 生成规则：//年月日 + 当天时间的毫秒 + 4位随机数 + 手机后四位数字 //130530 + 30921687 + 1010 +
     * 0676 //年月日 + 当天时间的毫秒 + 手机号码 当前毫秒数 + 手机后四位
     *
     * @return
     */
    public static String getOrderId(String mobile) {
        return "T" + System.currentTimeMillis() + mobile.substring(7, 11);
    }

    /***
     * 版本ID生成规则
     *
     * @return
     */
    public static String getVersionId() {
        return new SimpleDateFormat(SystemConstants.DATE_FORMAT_IDS_STRING).format(new Date()) + "X" + RandomStringUtils.randomNumeric(4);
    }
}
