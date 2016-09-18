package com.prowo.persist.impl;

import com.prowo.persist.BaseDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class BaseDaoImpl<T> implements BaseDao<T> {

    private static final Log logger = LogFactory.getLog(BaseDaoImpl.class);

    public JdbcTemplate jdbcTemplate;

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#save(java.lang.String)
     */
    public int save(String sql) {
        return this.save(sql, (Object[]) null);
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#save(java.lang.String, java.lang.Object)
     */
    public int save(String sql, Object... args) {
        return this.update(sql, args);
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#execute(java.lang.String)
     */
    public int execute(String sql) {
        return this.execute(sql, (Object[]) null);
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#execute(java.lang.String, java.lang.Object)
     */
    public int execute(String sql, Object... args) {
        return this.update(sql, args);
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#queryForInt(java.lang.String)
     */
    public int queryForInt(String sql) {
        return this.queryForInt(sql, (Object[]) null);
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#queryForInt(java.lang.String, java.lang.Object)
     */
    public int queryForInt(String sql, Object... args) {
        long begin = new Date().getTime();
        // int i = jdbcTemplate.queryForInt(sql, args);
        // spring 3.2.2之后，jdbctemplate中的queryForInt已经被取消了
        int i = jdbcTemplate.queryForObject(sql, args, Integer.class);
        long end = new Date().getTime();
        printSQL(sql, args, end - begin);
        return i;
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#queryForList(java.lang.String, java.lang.Object)
     */
    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        long begin = new Date().getTime();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args);
        long end = new Date().getTime();
        printSQL(sql, args, end - begin);
        return list;
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#queryForList(java.lang.String)
     */
    public List<Map<String, Object>> queryForList(String sql) {
        return this.queryForList(sql, (Object[]) null);
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#queryForStringList(java.lang.String, java.lang.Object)
     */
    public List<String> queryForStringList(String sql, Object... args) {
        long begin = new Date().getTime();
        List<String> list = jdbcTemplate.query(sql,
                new SingleColumnRowMapper<String>(String.class), args);
        long end = new Date().getTime();
        printSQL(sql, args, end - begin);
        return list;
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#queryForMap(java.lang.String)
     */
    public Map<String, Object> queryForMap(String sql) {
        return this.queryForMap(sql, (Object[]) null);
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#queryForMap(java.lang.String, java.lang.Object)
     */
    public Map<String, Object> queryForMap(String sql, Object... args) {
        long begin = new Date().getTime();
        Map<String, Object> map;
        try {
            map = jdbcTemplate.queryForMap(sql, args);
        } catch (EmptyResultDataAccessException e) {
            map = null;
        }
        long end = new Date().getTime();
        printSQL(sql, args, end - begin);
        return map;
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#update(java.lang.String)
     */
    public int update(String sql) {
        return this.update(sql, (Object[]) null);
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#update(java.lang.String, java.lang.Object)
     */
    public int update(String sql, Object... args) {
        long begin = new Date().getTime();
        int i = jdbcTemplate.update(sql, args);
        long end = new Date().getTime();
        printSQL(sql, args, end - begin);
        return i;
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#execute(java.lang.String, java.util.Map)
     */
    public int execute(String sql, Map<String, ?> map) {
        long begin = new Date().getTime();
        int i = jdbcTemplate.update(sql, map);
        long end = new Date().getTime();
        printSQL(sql, map.entrySet().toArray(), end - begin);
        return i;
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#queryForInt(java.lang.String, java.util.Map)
     */
    public int queryForInt(String sql, Map<String, ?> map) {
        long begin = new Date().getTime();
//        int i = jdbcTemplate.queryForInt(sql, map);
        int i = jdbcTemplate.queryForObject(sql, Integer.class, map);
        long end = new Date().getTime();
        printSQL(sql, map.entrySet().toArray(), end - begin);
        return i;
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#queryForList(java.lang.String, java.util.Map)
     */
    public List<Map<String, Object>> queryForList(String sql, Map<String, ?> map) {
        long begin = new Date().getTime();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, map);
        long end = new Date().getTime();
        printSQL(sql, map.entrySet().toArray(), end - begin);
        return list;
    }

    public long queryForLong(String sql) {
        return this.queryForLong(sql, (Object[]) null);
    }

    public long queryForLong(String sql, Object... args) {
        long begin = new Date().getTime();
        long l = 0;
        try {
            l = jdbcTemplate.queryForObject(sql, args, Long.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        long end = new Date().getTime();
        printSQL(sql, args, end - begin);
        return l;
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#queryForLong(java.lang.String, java.util.Map)
     */
    public long queryForLong(String sql, Map<String, ?> map) {
        long begin = new Date().getTime();
        long l = jdbcTemplate.queryForObject(sql, Long.class, map);
        long end = new Date().getTime();
        printSQL(sql, map.entrySet().toArray(), end - begin);
        return l;
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#queryForMap(java.lang.String, java.util.Map)
     */
    public Map<String, Object> queryForMap(String sql, Map<String, ?> map) {
        long begin = new Date().getTime();
        Map<String, Object> m = jdbcTemplate.queryForMap(sql, map);
        long end = new Date().getTime();
        printSQL(sql, map.entrySet().toArray(), end - begin);
        return m;
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#save(java.lang.String, java.util.Map)
     */
    public int save(String sql, Map<String, ?> map) {
        return this.execute(sql, map);
    }

    /* (non-Javadoc)
     * @see com.yundaex.common.impl.BaseDao#update(java.lang.String, java.util.Map)
     */
    public int update(String sql, Map<String, ?> map) {
        return this.execute(sql, map);
    }

    private void printSQL(String sql, Object[] params, long time) {
        logger.info("  SQL: " + sql + (params != null ? "\nParams: " + Arrays.deepToString(params) : "") + ",耗时：" + time + " 毫秒\n");
    }

    public void setjdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
