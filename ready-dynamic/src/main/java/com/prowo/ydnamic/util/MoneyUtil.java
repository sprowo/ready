package com.prowo.ydnamic.util;

import java.text.DecimalFormat;

public final class MoneyUtil {

    // private static Map<String, DecimalFormat> formatters = new
    // HashMap<String, DecimalFormat>();

    private enum MONEY_FORMAT {
        YYY_FJ("##0.00"), YYY_FJL("##0.000");

        private MONEY_FORMAT(String format) {
            this.format = format;
        }

        String format;

        String getFormat() {
            return this.format;
        }
    }

    public enum MONEY_UNIT {
        WAN, QIAN, BAI, SHI, YUAN, JIAO, FEN, LI, LI_1, LI_2, LI_3;
    }

    /*
     * static { DecimalFormat YYY_FJ = new DecimalFormat("###.00");// 这样为保持2位
     * formatters.put(MONEY_FORMAT.YYY_FJ.name(), YYY_FJ);
     * 
     * DecimalFormat YYY_FJL = new DecimalFormat("###.000");// 这样为保持3位
     * formatters.put(MONEY_FORMAT.YYY_FJL.name(), YYY_FJL);
     * 
     * }
     */

    /**
     * 转化为##0.00
     *
     * @param money
     * @return
     */
    public static String double2yjf(double money) {
        return new DecimalFormat(MONEY_FORMAT.YYY_FJ.getFormat()).format(money);
    }

    /**
     * 转化为##0.000
     *
     * @param money
     * @return
     */
    public static String double2yjfl(double money) {
        return new DecimalFormat(MONEY_FORMAT.YYY_FJL.getFormat()).format(money);
    }

    /**
     * 转化为##0.00
     *
     * @param money
     * @param unit
     * @return
     */
    public static String str2yjf(String money, MONEY_UNIT unit) {
        double factor = unit2factor(unit);
        return double2yjf(Double.parseDouble(money) * factor);
    }

    /**
     * 转化为##0.000
     *
     * @param money
     * @param unit
     * @return
     */
    public static String str2yjfl(String money, MONEY_UNIT unit) {
        double factor = unit2factor(unit);
        return double2yjfl(Double.parseDouble(money) * factor);
    }

    private static double unit2factor(MONEY_UNIT unit) {
        double factor = 1;
        switch (unit) {
            case WAN:
                factor = 10000;
                break;
            case QIAN:
                factor = 1000;
                break;
            case BAI:
                factor = 100;
                break;
            case SHI:
                factor = 10;
                break;
            case YUAN:
                factor = 1;
                break;
            case JIAO:
                factor = 0.1;
                break;
            case FEN:
                factor = 0.01;
                break;
            case LI:
                factor = 0.001;
                break;
            case LI_1:
                factor = 0.0001;
                break;
            case LI_2:
                factor = 0.00001;
                break;
            case LI_3:
                factor = 0.000001;
                break;
            default:
                throw new RuntimeException("not supported unit");
        }
        return factor;
    }

    /**
     * 一元操作符 最后转化为##0.00
     */
    public static String operate1yjf(MONEY_UNIT unit, String arg, MONEY_OPREATOR1 operator) {
        return double2yjf(operate1(unit, arg, operator));
    }

    /**
     * 一元操作符 最后转化为##0.000
     */
    public static String operate1yjfl(MONEY_UNIT unit, String arg, MONEY_OPREATOR1 operator) {
        return double2yjf(operate1(unit, arg, operator));
    }

    private static double operate1(MONEY_UNIT unit, String arg, MONEY_OPREATOR1 operator) {
        double factor = unit2factor(unit);
        double halfRslt = Double.parseDouble(arg) * factor;
        switch (operator) {
            case REVERSE:
                halfRslt = 0 - halfRslt;
                break;
            case ABS:
                halfRslt = Math.abs(halfRslt);
                break;
            case CEIL:
                halfRslt = Math.ceil(halfRslt);
                break;
            case FLLOR:
                halfRslt = Math.floor(halfRslt);
                break;
            case ROUND:
                halfRslt = Math.round(halfRslt);
                break;
            default:
                throw new RuntimeException("not supported operator");

        }
        return halfRslt;
    }

    /**
     * 二元操作符 最后转化为##0.00
     *
     * @param unit1
     * @param arg1
     * @param unit2
     * @param arg2
     * @param operator
     * @return
     */
    public static String operate2yjf(MONEY_UNIT unit1, String arg1, MONEY_UNIT unit2, String arg2,
                                     MONEY_OPREATOR2 operator) {
        return double2yjf(operate2(unit1, arg1, unit2, arg2, operator));
    }

    /**
     * 二元操作符 最后转化为##0.000
     *
     * @param unit1
     * @param arg1
     * @param unit2
     * @param arg2
     * @param operator
     * @return
     */
    public static String operate2yjfl(MONEY_UNIT unit1, String arg1, MONEY_UNIT unit2, String arg2,
                                      MONEY_OPREATOR2 operator) {
        return double2yjfl(operate2(unit1, arg1, unit2, arg2, operator));
    }

    private static double operate2(MONEY_UNIT unit1, String arg1, MONEY_UNIT unit2, String arg2,
                                   MONEY_OPREATOR2 operator) {
        double factor1 = unit2factor(unit1);
        double num1 = Double.parseDouble(arg1) * factor1;
        double factor2 = unit2factor(unit2);
        double num2 = Double.parseDouble(arg2) * factor2;
        double halfRslt = 0;
        switch (operator) {
            case ADD:
                halfRslt = num1 + num2;
                break;
            case SUB:
                halfRslt = num1 - num2;
                break;
            case MUL:
                halfRslt = num1 * num2;
                break;
            case DIV:
                halfRslt = num1 / num2;
                break;
            case MIN:
                halfRslt = Math.min(num1, num2);
                break;
            case MAX:
                halfRslt = Math.max(num1, num2);
                break;
            default:
                throw new RuntimeException("not supported operator");
        }
        return halfRslt;
    }

    public enum MONEY_OPREATOR2 {
        ADD, SUB, MUL, DIV, MIN, MAX;
    }

    public enum MONEY_OPREATOR1 {
        REVERSE, ABS, CEIL, FLLOR, ROUND;
    }

    public static String toggleMinus(String money) {
        if (money.startsWith("-")) {
            return money.replace("-", "");
        } else {
            return "-" + money;
        }

    }

}