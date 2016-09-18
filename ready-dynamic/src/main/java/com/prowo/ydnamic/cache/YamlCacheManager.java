package com.prowo.ydnamic.cache;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.mapper.JSONMapper;
import com.prowo.ydnamic.validation.Validate;
import org.ho.yaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class YamlCacheManager extends CacheManager {
    private Map<String, Object> cacheMap = new HashMap<String, Object>();

    public YamlCacheManager() {
        refresh();
    }

    @SuppressWarnings("unchecked")
    public void refresh() {
        InputStream inputStream;
        LoggerUtil.log(LoggerUtil.Level.DEBUG, "start to load ready-config.yml with type[HashMap] ");
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream("ready-config.yml");
            if (inputStream == null) {
                LoggerUtil.log(LoggerUtil.Level.WARN, "can not find ready-config.yml");
                return;
            }
            cacheMap = Yaml.loadType(inputStream, HashMap.class);
            LoggerUtil.log(LoggerUtil.Level.DEBUG, "load ready-config.yml with type[HashMap] success:{0}",
                    JSONMapper.toJson(cacheMap));
        } catch (Exception e) {
            LoggerUtil.log(LoggerUtil.Level.WARN, e, "load ready-config.yml with type[HashMap] failed!");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCacheObject(String key) {
        return (T) cacheMap.get(key);
    }

    @Override
    public String getString(String key) {
        try {
            return (String) cacheMap.get(key);
        } catch (Exception e) {
            return cacheMap.get(key).toString();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> getMap(String key) {
        return (Map<String, String>) cacheMap.get(key);
    }

    @Override
    public Set<String> keys(String pattern) {
        Set<String> keySet = new HashSet<String>();
        if (!Validate.isNull(pattern)) {
            for (String key : cacheMap.keySet()) {
                if (key.matches(pattern)) {
                    keySet.add(key);
                }
            }
        }
        return keySet;
    }

    @Override
    public void setString(String key, String value) {
        cacheMap.put(key, value);
    }

    @Override
    public void setMap(String key, Map<String, String> value) {
        cacheMap.put(key, value);
    }

    @Override
    public void removeCache(String key) {
        cacheMap.remove(key);
    }

    @Override
    public void clearCache(String pattern) {
        if (!Validate.isNull(pattern)) {
            for (String key : cacheMap.keySet()) {
                if (key.matches(pattern)) {
                    cacheMap.remove(key);
                }
            }
        }
    }

    @Override
    protected void expire(String key, long timeout) {

    }

    @Override
    public Integer getInt(String key) {
        try {
            return (Integer) cacheMap.get(key);
        } catch (Exception e) {
        }
        return Integer.parseInt(getString(key));
    }

    @Override
    public Long getLong(String key) {
        try {
            return (Long) cacheMap.get(key);
        } catch (Exception e) {
        }
        return Long.parseLong(getString(key));
    }
}
