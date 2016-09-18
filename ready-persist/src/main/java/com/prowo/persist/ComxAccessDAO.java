package com.prowo.persist;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementCallback;

public interface ComxAccessDAO {

    /**
     * 查询一行
     *
     * @param table
     * @param kocs
     * @return
     */
    public Map<String, Object> load(String table, KoC... kocs) throws Exception;

    /**
     * 查询列表
     *
     * @param table
     * @param kocs
     * @return
     */
    public List<Map<String, Object>> loads(String table, KoC... kocs) throws Exception;

    /**
     * 查询列表
     *
     * @param table
     * @param kocs
     * @return
     */
    public <T> T load(Class<T> clazz, String table, KoC... kocs) throws Exception;

    /**
     * 查询列表
     *
     * @param table
     * @param kocs
     * @return
     */
    public <T> List<T> loads(Class<T> clazz, String table, KoC... kocs) throws Exception;

    /**
     * 查询列表
     *
     * @param table
     * @param kocs
     * @return
     */
    public <T> List<T> loads(Class<T> clazz, KoC... kocs) throws Exception;

    /**
     * 保存数据
     *
     * @param table
     * @param data
     */
    public void insert(String table, Map<String, Object> data) throws Exception;

    /**
     * 批量保存数据
     *
     * @param sql
     * @param datas
     */
    public int[] batch(String sql, List<Object[]> datas) throws Exception;

    /**
     * 删除数据
     *
     * @param table
     * @param cs
     */
    public void delete(String table, KoC... cs) throws Exception;

    /**
     * 修改数据
     *
     * @param table 表名
     * @param keys  更新条件
     * @param cs    更新值
     */
    public void update(String table, KoC[] keys, KoC... cs) throws Exception;

    /**
     * 修改数据
     *
     * @param table
     * @param asArray
     * @param srvo
     */
    public void update(String table, KoC[] keys, Map<String, Object> srvo) throws Exception;

    /**
     * 更新数据
     *
     * @param sql
     * @param params
     */
    public int update(String sql, Object[] params) throws Exception;

    /**
     * 查询SEQ的下一个值
     *
     * @param urlSeq
     * @return
     */
    public Long next(String seqname) throws Exception;

    /**
     * 执行
     *
     * @param sql
     * @param action
     * @return
     */
    public <T> T execute(String sql, PreparedStatementCallback<T> action) throws Exception;
}
