package com.prowo.ydnamic.web;

import com.prowo.ydnamic.validation.Validate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Servlet业务中实体工具
 */
public class ServletBeanTools {

    /**
     * 自动匹配参数赋值到实体bean中
     *
     * @param bean
     * @param request
     * @author LiuDing 2014-2-16 下午10:23:37
     */
    public static void populate(Object bean, HttpServletRequest request) {
        Class<? extends Object> clazz = bean.getClass();
        Method ms[] = clazz.getDeclaredMethods();
        String mname;
        String field;
        String fieldType;
        String value;
        for (Method m : ms) {
            mname = m.getName();
            if (!mname.startsWith("set") || ArrayUtils.isEmpty(m.getParameterTypes())) {
                continue;
            }
            try {
                field = mname.toLowerCase().charAt(3) + mname.substring(4, mname.length());
                value = request.getParameter(field);
                if (StringUtils.isEmpty(value)) {
                    continue;
                }
                fieldType = m.getParameterTypes()[0].getName();
                // 以下可以确认value为String类型
                if (String.class.getName().equals(fieldType) && !Validate.isNull((String) value)) {
                    m.invoke(bean, (String) value);
                } else if (Integer.class.getName().equals(fieldType) && NumberUtils.isDigits((String) value)) {
                    m.invoke(bean, Integer.valueOf((String) value));
                } else if (Short.class.getName().equals(fieldType) && NumberUtils.isDigits((String) value)) {
                    m.invoke(bean, Short.valueOf((String) value));
                } else if (Long.class.getName().equals(fieldType) && NumberUtils.isDigits((String) value)) {
                    m.invoke(bean, Long.valueOf((String) value));
                } else if (Float.class.getName().equals(fieldType) && NumberUtils.isNumber((String) value)) {
                    m.invoke(bean, Float.valueOf((String) value));
                } else if (Double.class.getName().equals(fieldType) && NumberUtils.isNumber((String) value)) {
                    m.invoke(bean, Double.valueOf((String) value));
                } else if (Date.class.getName().equals(fieldType)) {
                    m.invoke(bean, DateUtils.parseDate((String) value, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyyMMdd",
                            "yyyyMMddHHmmss", "yyyyMMdd HHmmss", "yyyy/MM/dd HH:mm:ss"));
                } else if (Boolean.class.getName().equals(fieldType)) {
                    m.invoke(bean, BooleanUtils.toBoolean(value));
                } else {
                    m.invoke(bean, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 获取默认的分页的条数
     *
     * @param request
     * @return
     */
    public static String getPageSize(HttpServletRequest request) {
        return Validate.isNull(request.getParameter("pageSize")) ? "10" : request.getParameter("pageSize");
    }
}
