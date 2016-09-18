package com.prowo.persist;

import java.util.List;
import java.util.Map;

public interface BaseDao<T> {

    public abstract int save(String sql);

    public abstract int save(String sql, Object... args);

    public abstract int execute(String sql);

    public abstract int execute(String sql, Object... args);

    public abstract int queryForInt(String sql);

    public abstract int queryForInt(String sql, Object... args);

    public abstract List<Map<String, Object>> queryForList(String sql, Object... args);

    public abstract List<Map<String, Object>> queryForList(String sql);

    public abstract Map<String, Object> queryForMap(String sql);

    public abstract Map<String, Object> queryForMap(String sql, Object... args);

    public abstract int update(String sql);

    public abstract int update(String sql, Object... args);

    public abstract int execute(String sql, Map<String, ?> map);

    public abstract int queryForInt(String sql, Map<String, ?> map);

    public abstract List<Map<String, Object>> queryForList(String sql, Map<String, ?> map);

    public abstract long queryForLong(String sql, Map<String, ?> map);

    public long queryForLong(String sql);

    public long queryForLong(String sql, Object... args);

    public abstract Map<String, Object> queryForMap(String sql, Map<String, ?> map);

}