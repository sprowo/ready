package com.prowo.ydnamic.persist;

import com.prowo.persist.ComxAccessDAO;
import com.prowo.persist.KoC;
import com.prowo.persist.ProtasisSplicer;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;

import java.util.List;
import java.util.Map;

public interface ComxPagingDAO extends ComxAccessDAO {

    /**
     * 获得总记录数
     *
     * @param table
     * @param cs
     * @return
     */
    public int count(String table, KoC... cs) throws Exception;

    /**
     * @param sql
     * @param objects
     * @return
     */
    public int queryCount(String sql, Object... objects) throws Exception;

    /**
     * 查询记录数
     *
     * @param table 目标表名
     * @param page  页码，第一页为1
     * @param rows  每页记录数
     * @param cs    查询条件
     * @return
     */
    public List<Map<String, Object>> loads(String table, int page, int rows, KoC... cs) throws Exception;


    /**
     * 根据SQL语句进行查询
     *
     * @param sql 查询SQL
     * @param cs  查询条件
     * @return
     */
    public Map<String, Object> query(String sql, Object... cs) throws Exception;

    public List<Map<String, Object>> querys(String sql, Object... cs) throws Exception;

    /**
     * 分页查询
     *
     * @param page
     */
    public void loads(Pagination page) throws Exception;

    /**
     * 分页
     *
     * @param sql    查询SQL
     * @param offset 起始
     * @param limit  结束
     * @return
     */
    String getLimitString(String sql, int offset, int limit) throws Exception;

    /**
     * 执行存储过程
     *
     * @param callableStatementCreator
     * @param callableStatementCallback
     * @return
     */
    public Object excute(CallableStatementCreator callableStatementCreator, CallableStatementCallback<?> callableStatementCallback) throws Exception;

    /**
     * @return 条件从句拼接对象
     */
    public ProtasisSplicer getProtasisSplicer();

    Map<String, Object> select(String sql, KoC... kocs) throws Exception;

    List<Map<String, Object>> selects(String sql, KoC... kocs) throws Exception;

}
