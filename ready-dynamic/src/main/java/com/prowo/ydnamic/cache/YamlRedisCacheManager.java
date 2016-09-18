package com.prowo.ydnamic.cache;

import com.prowo.ydnamic.validation.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class YamlRedisCacheManager extends RedisCacheManager implements ICacheQuery {

    @Override
    public String getString(String key) {
        String value = super.getString(key);
        if (defaultCacheManager != null && Validate.isNull(value)) {
            return defaultCacheManager.getString(key);
        }
        return value;
    }

    @Override
    public Integer getInt(String key) {
        Integer value = super.getInt(key);
        if (defaultCacheManager != null && value == null) {
            return defaultCacheManager.getInt(key);
        }
        return value;
    }

    @Override
    public Long getLong(String key) {
        Long value = super.getLong(key);
        if (defaultCacheManager != null && value == null) {
            return defaultCacheManager.getLong(key);
        }
        return value;
    }

    @Override
    public Map<String, String> getMap(String key) {
        Map<String, String> value = super.getMap(key);
        if (defaultCacheManager != null) {
            Map<String, String> defautValueMap = defaultCacheManager.getMap(key);
            if (defautValueMap != null) {
                Map<String, String> total = new HashMap<String, String>(defautValueMap);
                total.putAll(value);
                return total;
            }
        }

        return value;
    }

    @Override
    public Set<String> keys(String pattern) {
        Set<String> keysSet = super.keys(pattern);
        if (defaultCacheManager != null) {
            keysSet.addAll(defaultCacheManager.keys(pattern));
        }
        return keysSet;
    }
}
