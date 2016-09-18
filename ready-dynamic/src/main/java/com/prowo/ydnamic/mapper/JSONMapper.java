package com.prowo.ydnamic.mapper;

import com.prowo.ydnamic.context.SystemConstants;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

public class JSONMapper {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(SystemConstants.DATE_FORMAT_DEFAULT_STRING));    // 特色社会主义的日期格式化
        mapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);    // 忽略特殊的字符
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);    // 忽略多余的字段
    }

    /**
     * 如果JSON字符串为Null或"null"字符串,返回Null. 如果JSON字符串为"[]",返回空集合.
     * <p>
     * 如需读取集合如List/Map,且不是List<String>这种简单类型时使用如下语句: List<MyBean> beanList =
     * binder.getMapper().readValue(listString, new
     * TypeReference<List<MyBean>>() {});
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(jsonString, clazz);
    }

    public static <T> T fromJson(InputStream stream, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(stream, clazz);
    }

    public static <T> List<T> fromJson(String jsonString) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(jsonString, new TypeReference<List<T>>() {
        });
    }

    /**
     * 如果对象为Null,返回"null". 如果集合为空集合,返回"[]".
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public static String toJson(Object object) throws JsonGenerationException, JsonMappingException, IOException {
        return mapper.writeValueAsString(object);
    }

    public static <T> void toJson(OutputStream stream, Object object) throws JsonGenerationException, JsonMappingException, IOException {
        mapper.writeValue(stream, object);
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public static ObjectMapper getMapper() {
        return mapper;
    }
}
