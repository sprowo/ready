package com.prowo.ydnamic.cache;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis读取操作类
 *
 * @author hugong
 */
public class RedisCacheManager extends CacheManager {

    private RedisTemplate<String, String> template;
    private ValueOperations<String, String> valueOperations;
    private SetOperations<String, String> setOperations;
    private ListOperations<String, String> listOperations;
    private HashOperations<String, String, String> hashOperatins;

    public RedisTemplate<String, String> getTemplate() {
        return template;
    }

    public void setTemplate(RedisTemplate<String, String> template) {
        this.template = template;
        this.valueOperations = template.opsForValue();
        this.setOperations = template.opsForSet();
        this.listOperations = template.opsForList();
        this.hashOperatins = template.opsForHash();
    }

    protected void expire(String key, long timeout) {
        if (timeout > 0) {
            template.expire(key, timeout, TimeUnit.MILLISECONDS);
        }
    }

    public void setString(String key, String value) {
        valueOperations.set(key, (String) value);
    }

    public String getString(String key) {
        return valueOperations.get(key);
    }

    public void setMap(String key, Map<String, String> value) {
        hashOperatins.putAll(key, value);
    }

    public Map<String, String> getMap(String key) {
        return hashOperatins.entries(key);
    }

    public void removeCache(String key) {
        if (template.hasKey(key)) {
            template.delete(key);
        }
    }

    public void clearCache(String pattern) {
        Set<String> keys = template.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            template.delete(keys);
        }
    }

    public Set<String> keys(String pattern) {
        return template.keys(pattern);
    }

    public SetOperations<String, String> getSetOperations() {
        return setOperations;
    }

    public ListOperations<String, String> getListOperations() {
        return listOperations;
    }

    @SuppressWarnings("unchecked")
    public <T> void setCacheObject(String key, T value, DataType dataType, long timeout) {
        if (DataType.STRING.equals(dataType)) {
            valueOperations.set(key, (String) value);
        } else if (DataType.LIST.equals(dataType)) {
            listOperations.leftPush(key, (String) value);
        } else if (DataType.SET.equals(dataType)) {
            setOperations.add(key, (String) value);
        } else if (DataType.HASH.equals(dataType)) {
            hashOperatins.putAll(key, (Map<String, String>) value);
        }

        // 设置缓存过期时间
        expire(key, timeout);

    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public <T> T getCacheObject(String key) {
        if (template.hasKey(key)) {
            if (DataType.STRING.equals(template.type(key))) {
                return (T) valueOperations.get(key);
            } else if (DataType.LIST.equals(template.type(key))) {
                return (T) listOperations.range(key, 0, -1);
            } else if (DataType.SET.equals(template.type(key))) {
                return (T) setOperations.members(key);
            } else if (DataType.HASH.equals(template.type(key))) {
                return (T) hashOperatins.entries(key);
            }
        }
        return null;
    }

    @Override
    public Integer getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    @Override
    public Long getLong(String key) {
        return Long.parseLong(getString(key));
    }

}
