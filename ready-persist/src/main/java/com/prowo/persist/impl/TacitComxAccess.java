package com.prowo.persist.impl;

import com.prowo.persist.BaseBO;
import com.prowo.persist.ComxAccessDAO;
import com.prowo.persist.KoC;
import com.prowo.persist.exception.PersistException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.util.Assert;

import javax.persistence.Table;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 通用数据访问
 */
public class TacitComxAccess extends BaseBO implements ComxAccessDAO {

    @Override
    public void insert(String table, Map<String, Object> data) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(table).append("(");
        StringBuilder holder = new StringBuilder();
        Object[] params = new Object[data.size()];
        int index = 0;
        for (Entry<String, Object> entry : data.entrySet()) {
            sql.append(entry.getKey()).append(",");
            holder.append("?,");
            params[index++] = entry.getValue();
        }
        sql.deleteCharAt(sql.length() - 1);
        holder.deleteCharAt(holder.length() - 1);
        sql.append(")VALUES(").append(holder).append(")");
        update(sql.toString(), params);
    }

    @Override
    public void delete(String table, KoC... cs) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(table);
        sql.append(" WHERE 1=1");
        Object[] params = new Object[cs.length];
        int index = 0;
        for (KoC kvc : cs) {
            sql.append(" AND ").append(kvc.getKey()).append(" = ?");
            params[index++] = kvc.getValue();
        }
        update(sql.toString(), params);
    }

    @Override
    public Map<String, Object> load(String table, KoC... kocs) throws Exception {
        List<Map<String, Object>> list = this.loads(new ColumnMapRowMapper(), table, 1, kocs);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<Map<String, Object>> loads(String table, KoC... kocs) throws Exception {
        return loads(new ColumnMapRowMapper(), table, 0, kocs);
    }

    @Override
    public <T> T load(Class<T> clazz, String table, KoC... kocs) throws Exception {
        // ParameterizedBeanPropertyRowMapper<T> mapper = ParameterizedBeanPropertyRowMapper.newInstance(clazz);
        LobPropertyRowMapper<T> mapper = LobPropertyRowMapper.newInstance(clazz, lobHandler);
        List<T> list = loads(mapper, table, 1, kocs);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public <T> List<T> loads(Class<T> clazz, String table, KoC... kocs) throws Exception {
        // ParameterizedBeanPropertyRowMapper<T> mapper = ParameterizedBeanPropertyRowMapper.newInstance(clazz);
        LobPropertyRowMapper<T> mapper = LobPropertyRowMapper.newInstance(clazz, lobHandler);
        try {
            return loads(mapper, table, 0, kocs);
        } catch (Exception e) {
            logger.error("数据库访问异常", e);
            throw new PersistException();
        }
    }

    @Override
    public <T> List<T> loads(Class<T> clazz, KoC... kocs) throws Exception {
        LobPropertyRowMapper<T> mapper = LobPropertyRowMapper.newInstance(clazz, lobHandler);
        Table table = clazz.getAnnotation(Table.class);
        Assert.notNull(table, "Class not mapped table!");
        return loads(mapper, table.name(), 0, kocs);
    }

    /**
     * 查询
     *
     * @param mapper 行映射
     * @param table  查询表
     * @param rows   最大行记录数
     * @param kocs   查询条件
     * @return
     * @throws Exception
     */
    public <T> List<T> loads(RowMapper<T> mapper, String table, int rows, KoC... kocs) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(table).append(" WHERE 1=1");
        Object[] params = new Object[kocs.length];
        int index = 0;
        for (KoC kvc : kocs) {
            sql.append(" AND ").append(kvc.getKey()).append(" ").append(kvc.getOptor()).append(" ?");
            params[index++] = kvc.getValue();
        }
        loggerSQL(sql.toString(), params);
        RowMapperResultSetExtractor<T> rmre = new RowMapperResultSetExtractor<T>(mapper);
        try {
            return template.query(new SPSC(sql.toString(), rows), new ArgPreparedStatementSetter(params), rmre);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            logger.error("数据库访问异常", e);
            throw new PersistException();
        }
    }

    @Override
    public void update(String table, KoC[] keys, KoC... cs) throws Exception {
        Object[] params = new Object[keys.length + cs.length];
        StringBuilder sql = new StringBuilder();
        int index = 0;
        sql.append("UPDATE ").append(table).append(" SET");
        for (KoC koc : cs) {
            sql.append(" ").append(koc.getKey()).append(" = ?,");
            params[index++] = koc.getValue();
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE 1=1");
        for (KoC koc : keys) {
            sql.append(" AND ").append(koc.getKey()).append(" = ?");
            params[index++] = koc.getValue();
        }
        update(sql.toString(), params);
    }

    @Override
    public int[] batch(String sql, List<Object[]> datas) throws Exception {
        if (logger.isDebugEnabled()) {
            StringBuilder params = new StringBuilder();
            if (datas != null && datas.size() > 0) {
                for (Object[] objects : datas) {
                    params.append("\nparam:[");
                    for (Object object : objects) {
                        params.append(object).append(",");
                    }
                    params.deleteCharAt(params.length() - 1).append("]");
                }
            }
            logger.debug("sqlbatch:[" + sql + "], params:" + params);
        }
        try {
            return template.batchUpdate(sql, datas);
        } catch (Exception e) {
            Throwable t = e.getCause();
            if (t != null && t instanceof BatchUpdateException) {
                BatchUpdateException ex = (BatchUpdateException) t;
                return ex.getUpdateCounts();
            }
            logger.error("数据库访问异常", e);
            throw new PersistException();
        }
    }

    @Override
    public void update(String table, KoC[] keys, Map<String, Object> srvo) throws Exception {
        Object[] params = new Object[keys.length + srvo.size()];
        StringBuilder sql = new StringBuilder();
        int index = 0;
        sql.append("UPDATE ").append(table).append(" SET");
        for (Entry<String, Object> e : srvo.entrySet()) {
            sql.append(" ").append(e.getKey()).append(" = ?,");
            params[index++] = e.getValue();
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE 1=1");
        for (KoC koc : keys) {
            sql.append(" AND ").append(koc.getKey()).append(" = ?");
            params[index++] = koc.getValue();
        }
        update(sql.toString(), params);
    }

    @Override
    public int update(String sql, Object[] params) throws Exception {
        loggerSQL(sql, params);
        int result = 0;
        try {
            result = template.update(sql, params);
        } catch (Exception e) {
            logger.error("数据库访问异常", e);
            throw new PersistException();
        }
        return result;
    }


    @Override
    public <T> T execute(String sql, PreparedStatementCallback<T> action) throws Exception {
        loggerSQL(sql, null);
        try {
            return template.execute(sql, action);
        } catch (Exception e) {
            logger.error("数据库访问异常", e);
            throw new PersistException();
        }
    }

    /**
     * Simple Prepared Statement Creator
     */
    protected static class SPSC implements PreparedStatementCreator {

        private String sql;

        private int rows;

        public SPSC(String sql, int rows) {
            super();
            this.sql = sql;
            this.rows = rows;
        }

        @Override
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            PreparedStatement pstm = con.prepareStatement(sql);
            if (rows > 0) {
                pstm.setMaxRows(rows);
            }
            return pstm;
        }
    }

    @Override
    public Long next(String seqname) {
        String sql = "select " + seqname + ".nextval from dual";
        loggerSQL(sql, null);
//		return template.queryForLong(sql);
        return template.queryForObject(sql, Long.class);
    }

    protected void loggerSQL(String sql, Object[] params) {
        if (logger.isDebugEnabled()) {
            StringBuilder param = new StringBuilder();
            if (params != null && params.length > 0) {
                param.append("[");
                for (Object object : params) {
                    param.append(object).append(",");
                }
                param.deleteCharAt(param.length() - 1).append("]");
                logger.debug("sql:[" + sql + "], params:" + param);
            } else {
                logger.debug("sql:[" + sql + "]");
            }
        }
    }

}
