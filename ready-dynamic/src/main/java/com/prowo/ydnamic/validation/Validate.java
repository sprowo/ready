package com.prowo.ydnamic.validation;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {

	/*
	 * "^\\d+$"//非负整数（正整数 + 0） 　　02、"^[0-9]*[1-9][0-9]*$"//正整数
	 * 　　03、"^((-\\d+)(0+))$"//非正整数（负整数 + 0） 　　04、"^-[0-9]*[1-9][0-9]*$"//负整数
	 * 　　05、"^-?\\d+$"//整数 　　06、"^\\d+(\\.\\d+)?$"//非负浮点数（正浮点数 + 0） 　　07、
	 * "^(([0-9]+\\.[0-9]*[1-9][0-9]*)([0-9]*[1-9][0-9]*\\.[0-9]+)([0-9]*[1-9][0-9]*))$"
	 * //正浮点数 　　08、"^((-\\d+(\\.\\d+)?)(0+(\\.0+)?))$"//非正浮点数（负浮点数 + 0） 　　09、
	 * "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)([0-9]*[1-9][0-9]*\\.[0-9]+)([0-9]*[1-9][0-9]*)))$"
	 * //负浮点数 　　10、"^(-?\\d+)(\\.\\d+)?$"//浮点数
	 * 　　11、"^[A-Za-z]+$"//由26个英文字母组成的字符串 　　12、"^[A-Z]+$"//由26个英文字母的大写组成的字符串
	 * 　　13、"^[a-z]+$"//由26个英文字母的小写组成的字符串
	 * 　　14、"^[A-Za-z0-9]+$"//由数字和26个英文字母组成的字符串
	 * 　　15、"^\\w+$"//由数字、26个英文字母或者下划线组成的字符串
	 * 　　16、"^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$"//email地址
	 */
	public static String NUMBER_REGEX = "[0-9]+(.[0-9]+)?"; // 正数 float 类型

	public static String POSITIVE_NUMBER = "(^-?|^\\+?)\\d+$";// 正整数 包括0

	public static String PHONE_MOBILE = "^[0-9]{0,3}(13[0-9]|145|147|15[0-9]|18[0-9])[0-9]{8}$"; // 手机号
	// ^[1][3|4|5|8]+\\d{5,9}$
	// "^(13[0-9]|145|147|15[0-9]|18[0-9])[0-9]{8}$"
	
	public static String MAILNO = "^\\d{13}";	// 运单号

	public static String PHONE = "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$"; // 座机
	///^[0-9]{0,4}-?[0-9]{0,4}-?[0-9]{7,8}-?[0,9]{0,4}$/

	// 日期
	public static String DATE_REGEX = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";

	public static String TIME_REGEX = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";

	/**
	 * 判断字符串是否为空，如是空则返回True, 否则返回False
	 **/
	public static boolean isNull(String value) {
		if (value == null || "".equals(value) || "null".equals(value) || "undefined".equals(value)) {
			return true;
		}
		if (value.trim().length() == 0) {
			return true;
		}
		if (value.length() == 0) {
			return true;
		}
		return false;
	}

	/***
	 * 
	 * @param value
	 *            需要验证的值
	 * @param regEx
	 *            正则表达式
	 * @param errorMsg
	 *            验证失败时的信息
	 */
	public static void RegExValidate(String value, String regEx, String errorMsg) throws Exception {
		try {
			if (Validate.isNull(value)) {
				return;
			}
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(value);
			if (!m.matches()) {
				throw new Exception(errorMsg);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param value
	 *            需要验证的值
	 * @param regEx
	 *            正则表达式
	 * @return 真表示验证通过，否则表示验证不通过
	 */
	public static boolean RegExValidate(String value, String regEx) {
		boolean con = false;
		if (!Validate.isNull(value)) {
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(value);
			if (m.matches()) {
				con = true;
			}
		}
		return con;
	}
	
    @SuppressWarnings("unchecked")
    public static <T> T nonNullVal(Map<String, Object> map, String key, T defVal) {
        if (map == null || map.get(key) == null) {
            return defVal;
        }
        if (defVal instanceof String) {
            return (T) map.get(key).toString();
        }
        return (T) map.get(key);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T nonNullVal(Object obj, T defVal) {
        if (obj == null || obj.equals("null")) {
            return defVal;
        }
        return (T)obj;
    }

	public static StringBuffer replace(String from, String to, String source) {
		if (source == null || from == null || to == null)
			return null;
		StringBuffer bf = new StringBuffer();
		int index = -1;
		while ((index = source.indexOf(from)) != -1) {
			bf.append(source.substring(0, index) + to);
			source = source.substring(index + from.length());
			index = source.indexOf(from);
		}
		bf.append(source);
		return bf;
	}
	

}
