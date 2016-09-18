package com.prowo.persist.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.prowo.persist.BaseVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.NumberUtils;

/**
 * 工具类
 */
public class ToteBox {
    private static final Log logger = LogFactory.getLog(ToteBox.class);

    /**
     * 类解析缓存
     */
    private static Map<String, List<Field>> fcache = new HashMap<String, List<Field>>();

    /**
     * 短格式日期
     */
    private static final SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 时分秒格式日期
     */
    private static final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 时分秒格式日期
     */
    private static final SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static boolean empty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static byte[] o2bs(Serializable object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
        } catch (Throwable e) {
            logger.error("", e);
        }
        return baos.toByteArray();
    }

    public static <T> T bs2o(byte[] bs, Class<T> clazz) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bs);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object object = ois.readObject();
            ois.close();
            return clazz.cast(object);
        } catch (Throwable e) {
            logger.error("", e);
        }
        return null;
    }

    public static List<Field> getFieldList(Class<?> pclazz) {
        List<Field> list = null;
        synchronized (fcache) {
            list = fcache.get(pclazz.getName());
            if (list == null) {
                list = new ArrayList<Field>();
                getCanWriteField(pclazz, new HashSet<String>(), list);
                fcache.put(pclazz.getName(), list);
            }
        }
        return list;
    }

    @SuppressWarnings("rawtypes")
    public static void getCanWriteField(Class clazz, HashSet<String> set, List<Field> fields) {
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers()) || set.contains(field.getName())) {
                continue;
            }
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            fields.add(field);
        }
        Class superc = clazz.getSuperclass();
        if (!superc.equals(Object.class)) {
            getCanWriteField(superc, set, fields);
        }
    }

    /**
     * 获取类中字段定义
     *
     * @param clazz
     * @param name
     * @return
     */
    public static Field getDeclaredField(Class<?> clazz, String name) {
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getDeclaredField(name);
        } catch (SecurityException e) {
            logger.error(clazz.getName() + "/" + name, e);
        } catch (NoSuchFieldException e) {
            return getDeclaredField(clazz.getSuperclass(), name);
        }
        return null;
    }

    public static String cs(Object value) {
        return cv(value, String.class);
    }

    public static void clear(Collection<?> c) {
        if (c != null) {
            c.clear();
        }
    }

    public static void clear(Map<?, ?> map) {
        if (map != null) {
            map.clear();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T cv(Object value, Class<T> requiredType) {
        if (value == null) {
            return null;
        }
        if (requiredType.isInstance(value)) {
            return (T) value;
        }
        if (String.class.equals(requiredType)) {
            return (T) value.toString();
        } else if (isNumberType(requiredType)) {
            if (value instanceof Number) {
                return (T) NumberUtils.convertNumberToTargetClass(((Number) value), (Class<Number>) requiredType);
            } else if (org.apache.commons.lang3.math.NumberUtils.isNumber(value.toString())) {
                return (T) NumberUtils.parseNumber(value.toString(), (Class<Number>) requiredType);
            } else {
                return null;
            }
        } else if (Date.class.isAssignableFrom(requiredType)) {
            String dv = value.toString();
            if (dv.length() == 0) {
                return null;
            }
            try {
                if (dv.length() == 10) {
                    return (T) ((SimpleDateFormat) df1.clone()).parse(dv);
                } else if (dv.length() == 16) {
                    return (T) ((SimpleDateFormat) df2.clone()).parse(dv);
                } else if (dv.length() == 19) {
                    return (T) ((SimpleDateFormat) df3.clone()).parse(dv);
                }
            } catch (ParseException e) {
                logger.error("解析日期异常：" + dv, e);
            }
            return null;
        } else {
            throw new IllegalArgumentException("Value [" + value + "] is of type [" + value.getClass().getName() + "] and cannot be converted to required type [" + requiredType.getName() + "]");
        }
    }

    private static boolean isNumberType(Class<?> clazz) {
        if (Number.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (int.class.equals(clazz) || long.class.equals(clazz) || double.class.equals(clazz) || float.class.equals(clazz)) {
            return true;
        }
        return false;
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz, ApplicationContext context) {
        return context.getBeansOfType(clazz);
    }

    /**
     * 从Request中获取值，保存到目标对象中
     *
     * @param target  目标对象
     * @param request
     * @param prefix  前缀
     */
    public static void fillValue(Object target, HttpServletRequest request, String prefix) {
        Field[] fields = target.getClass().getDeclaredFields();
        String value = null;
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            if (BaseVO.class.isAssignableFrom(f.getType())) {
                try {
                    Object bean = ConstructorUtils.invokeConstructor(f.getType(), new Object[0]);
                    fillValue(bean, request, f.getName());
                    FieldUtils.writeField(f, target, bean, true);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                continue;
            } else if (f.getType().isAssignableFrom(List.class)) {
                list:
                {
                    Type type = f.getGenericType();
                    if (type instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) type;
                        Class<?> atclass = ((Class<?>) pt.getActualTypeArguments()[0]);
                        Field[] subfs = atclass.getDeclaredFields();
                        Map<String, String[]> values = new HashMap<String, String[]>();
                        int min = Integer.MAX_VALUE;
                        for (Field sf : subfs) {
                            if (Modifier.isStatic(sf.getModifiers())) {
                                continue;
                            }
                            String[] valuesx = request.getParameterValues("i_" + sf.getName());
                            if (valuesx == null || valuesx.length == 0) {
                                valuesx = request.getParameterValues(sf.getName());
                            }
                            if (valuesx == null || valuesx.length == 0) {
                                break list;
                            }
                            if (valuesx.length < min) {
                                min = valuesx.length;
                            }
                            values.put(sf.getName(), valuesx);
                        }
                        List<Object> sublist = new ArrayList<Object>();
                        Object bean = null;
                        for (int i = 0; i < min; i++) {
                            try {
                                bean = ConstructorUtils.invokeConstructor(atclass, new Object[0]);
                                for (Field sf : subfs) {
                                    if (Modifier.isStatic(sf.getModifiers())) {
                                        continue;
                                    }
                                    FieldUtils.writeField(sf, bean, ConvertUtils.convert(values.get(sf.getName())[i], sf.getType()), true);
                                }
                                sublist.add(bean);
                            } catch (Throwable e) {
                                e.printStackTrace();
                                break list;
                            }
                        }
                        try {
                            FieldUtils.writeField(f, target, sublist, true);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            break list;
                        }
                    }
                }
            } else {
                if (prefix == null || prefix.length() == 0) {
                    value = request.getParameter(f.getName());
                } else {
                    value = request.getParameter(prefix + "." + f.getName());
                }
                if (value == null || value.length() == 0) {
                    continue;
                }
                try {
                    FieldUtils.writeField(f, target, ConvertUtils.convert(value, f.getType()), true);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> T setValue(T bean, Map<String, Object> pvalue) {
        if (bean == null || pvalue == null) {
            return bean;
        }
        Object value = null;
        try {
            for (Field field : getFieldList(bean.getClass())) {
                value = pvalue.get(field.getName());
                if (value == null) {
                    value = pvalue.get(field.getName().toLowerCase());
                }
                if (value == null) {
                    value = pvalue.get(field.getName().toUpperCase());
                }
                if (value == null) {
                    continue;
                }
                value = cv(value, field.getType());
                if (value != null) {
                    BeanUtils.setProperty(bean, field.getName(), value);
                }
            }
            return bean;
        } catch (IllegalAccessException e) {
            logger.error(e);
        } catch (InvocationTargetException e) {
            logger.error(e);
        }

        return bean;
    }

    public static <T> T[] subarray(T[] array, int start) {
        return subarray(array, start, array.length);
    }

    public static Object[] ta(Object... ts) {
        return ts;
    }

    public static <T> T[] tax(T... ts) {
        return ts;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T[] subarray(T[] array, int start, int end) {
        if (array == null) {
            return null;
        }
        if (start < 0) {
            start = 0;
        }
        if (end > array.length) {
            end = array.length;
        }
        int newSize = end - start;
        Class type = array.getClass().getComponentType();
        if (newSize <= 0) {
            return (T[]) Array.newInstance(type, 0);
        }
        T[] subarray = (T[]) Array.newInstance(type, newSize);
        System.arraycopy(array, start, subarray, 0, newSize);
        return subarray;
    }

    public static byte[] tobytes(String str, String charset) {
        if (str != null) {
            try {
                return str.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                logger.error("", e);
            }
        }
        return new byte[0];
    }

    public static String join(Collection<? extends Object> set, String segs) {
        if (set == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object item : set) {
            if (item == null) {
                continue;
            }
            sb.append(segs).append(item);
        }
        if (sb.length() > 0) {
            return sb.substring(segs.length());
        } else {
            return sb.toString();
        }
    }

    public static String join(Object[] set, String segs) {
        if (set == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object item : set) {
            sb.append(segs).append(item);
        }
        if (sb.length() > 0) {
            return sb.substring(segs.length());
        } else {
            return sb.toString();
        }
    }

    /**
     * 根据两点经纬度计算距离
     *
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static double distance(double lng1, double lat1, double lng2, double lat2) {
        // return qc(x1 - x2) + qc(y1 - y2);
        double radLat1 = lat1 * Math.PI / 180;
        double radLat2 = lat2 * Math.PI / 180;
        double a = radLat1 - radLat2;
        double b = lng1 * Math.PI / 180 - lng2 * Math.PI / 180;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
