package com.prowo.ydnamic.persist;

import java.util.List;
import java.util.Map;

import com.prowo.persist.KoC;
import com.prowo.persist.exception.PersistException;
import com.prowo.persist.impl.ArgPreparedStatementSetter;
import com.prowo.persist.impl.TacitComxAccess;
import com.prowo.persist.util.ToteBox;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

public class TacitMySQLPagingAccess extends TacitComxAccess implements ComxPagingDAO {

    @Override
    public int count(String table, KoC... kocs) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM ").append(table).append(" WHERE 1=1");
        Object[] params = new Object[kocs.length];
        int index = 0;
        for (KoC kvc : kocs) {
            sql.append(getProtasisSplicer().splice(kvc));
            params[index++] = kvc.getValue();
        }
        loggerSQL(sql.toString(), params);
        try {
//            return template.queryForInt(sql.toString(), params);
            return template.queryForObject(sql.toString(), params, Integer.class);
        } catch (Exception e) {
            logger.error("数据库访问异常", e);
            throw new PersistException();
        }
    }

    @Override
    public Map<String, Object> select(String sql, KoC... kocs) throws Exception {
        StringBuilder sql2 = new StringBuilder(sql);
        Object[] params = new Object[kocs.length];
        int index = 0;
        for (KoC kvc : kocs) {
            sql2.append(getProtasisSplicer().splice(kvc));
            params[index++] = kvc.getValue();
        }
        loggerSQL(sql2.toString(), params);
        try {
            return query(sql2.toString(), params);
        } catch (Exception e) {
            logger.error("数据库访问异常", e);
            throw new PersistException();
        }
    }

    @Override
    public List<Map<String, Object>> selects(String sql, KoC... kocs) throws Exception {
        StringBuilder sql2 = new StringBuilder(sql);
        Object[] params = new Object[kocs.length];
        int index = 0;
        for (KoC kvc : kocs) {
            sql2.append(getProtasisSplicer().splice(kvc));
            params[index++] = kvc.getValue();
        }
        loggerSQL(sql2.toString(), params);
        try {
            return querys(sql2.toString(), params);
        } catch (Exception e) {
            logger.error("数据库访问异常", e);
            throw new PersistException();
        }
    }

    @Override
    public List<Map<String, Object>> loads(String table, int page, int rows, KoC... kocs) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(table).append(" WHERE 1=1");
        Object[] params = new Object[kocs.length + 2];
        int index = 0;
        for (KoC kvc : kocs) {
            sql.append(getProtasisSplicer().splice(kvc));
            params[index++] = kvc.getValue();
        }
        sql.append(" LIMIT ?, ?");
        params[index++] = (page - 1) * rows;
        params[index] = rows;
        return this.querys(sql.toString(), params);
    }

    @Override
    public Map<String, Object> query(String sql, Object... cs) throws Exception {
        loggerSQL(sql, cs);
        try {
            return template.queryForMap(sql, cs);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            logger.error("数据库访问异常", e);
            throw new PersistException();
        }
    }

    @Override
    public List<Map<String, Object>> querys(String sql, Object... cs) throws Exception {
        ColumnMapRowMapper mapper = new ColumnMapRowMapper();
        RowMapperResultSetExtractor<Map<String, Object>> rmre = new RowMapperResultSetExtractor<Map<String, Object>>(
                mapper);
        loggerSQL(sql, cs);
        try {
            return template.query(new SPSC(sql, 0), new ArgPreparedStatementSetter(cs), rmre);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            logger.error("数据库访问异常", e);
            throw new PersistException();
        }
    }

    @Override
    public void loads(Pagination page) throws Exception {
        StringBuilder msqlSb = new StringBuilder(page.getMsql());
        StringBuilder csqlSb = new StringBuilder(page.getCsql());
        Object[] factors = new Object[page.getKocs().length];
        int index = 0;
        String psv = null;
        for (KoC kvc : page.getKocs()) {
            psv = getProtasisSplicer().splice(kvc);
            msqlSb.append(psv);
            csqlSb.append(psv);
            factors[index++] = kvc.getValue();
        }
//        int total = template.queryForInt(csqlSb.toString(), factors);
        int total = template.queryForObject(csqlSb.toString(), factors, Integer.class);
        String msql = msqlSb.toString();
        if (!ToteBox.empty(page.getSort()) && !ToteBox.empty(page.getOrder())) {
            msql += " ORDER BY " + page.getSort() + " " + page.getOrder();
        }
        page.setTotal(total);
        msql += " LIMIT ?,?";
        Object[] params = new Object[factors.length + 2];
        System.arraycopy(factors, 0, params, 0, factors.length);
        params[factors.length] = (page.getPage() - 1) * page.getRows();
        params[factors.length + 1] = page.getRows();
        List<Map<String, Object>> datas = this.querys(msql, params);
        page.setDatas(datas);
    }

    @Override
    public int queryCount(String sql, Object... objects) throws Exception {
        loggerSQL(sql, objects);
        try {
//            return template.queryForInt(sql.toString(), objects);
            return template.queryForObject(sql.toString(), objects, Integer.class);
        } catch (Exception e) {
            logger.error("数据库访问异常", e);
            throw new PersistException();
        }
    }

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return null;
    }

    @Override
    public Object excute(CallableStatementCreator callableStatementCreator,
            CallableStatementCallback<?> callableStatementCallback) {
        return null;
    }

}
