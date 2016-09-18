package com.prowo.ydnamic.mapper;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * map和javaBean之间相互转化工具类
 */
public class BeanMapMapper {

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param type 要转化的类型
     * @param map  包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InstantiationException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Object convertMap2Bean(Class<?> type, Map<?, ?> map) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象
        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            // 如果属性中包含"_",则不转换，否则转换
            String propertyName = "";
            if (descriptor.getName().indexOf("_") != -1) {
                propertyName = descriptor.getName();
            } else {
                propertyName = fieldToProperty(descriptor.getName());
            }
            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);
                Object[] args = new Object[1];
                args[0] = value;
                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    public static Map<String, Object> bean2Map(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class<? extends Object> type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            // 如果属性中包含"_",则不转换，否则转换
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName.toUpperCase(), result);
                } else {
                    returnMap.put(propertyName.toUpperCase(), null);
                }
            }
        }
        return returnMap;
    }

    public static Map<String, Object> bean2MapIgnoreNull(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class<? extends Object> type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            // 如果属性中包含"_",则不转换，否则转换
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName.toUpperCase(), result);
                }
            }
        }
        return returnMap;
    }

    public static Map<String, Object> bean2MapIgnoreNullAndEmpty(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class<? extends Object> type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            // 如果属性中包含"_",则不转换，否则转换
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null && StringUtils.isNotBlank(result.toString())) {
                    returnMap.put(propertyName.toUpperCase(), result);
                }
            }
        }
        return returnMap;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map<String, Object> convertBean2Map(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class<? extends Object> type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            // 如果属性中包含"_",则不转换，否则转换
            String propertyName = "";
            if (descriptor.getName().indexOf("_") != -1) {
                propertyName = descriptor.getName();
            } else {
                propertyName = propertyToField(descriptor.getName());
            }
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, null);
                }
            }
        }
        return returnMap;
    }

    /**
     * 对象属性转换为字段 例如：userName to user_name
     *
     * @param property 字段名
     * @return
     */
    public static String propertyToField(String property) {
        if (null == property) {
            return "";
        }
        char[] chars = property.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char c : chars) {
            if (CharUtils.isAsciiAlphaUpper(c)) {
                sb.append("_" + StringUtils.lowerCase(CharUtils.toString(c)));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 字段转换成对象属性 例如：user_name to userName
     *
     * @param field
     * @return
     */
    public static String fieldToProperty(String field) {
        if (null == field) {
            return "";
        }
        char[] chars = field.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '_') {
                int j = i + 1;
                if (j < chars.length) {
                    sb.append(StringUtils.upperCase(CharUtils.toString(chars[j])));
                    i++;
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
