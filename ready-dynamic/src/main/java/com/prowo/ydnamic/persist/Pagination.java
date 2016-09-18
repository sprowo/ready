package com.prowo.ydnamic.persist;

import com.prowo.persist.KoC;
import com.prowo.persist.Objectx;

import java.util.List;
import java.util.Map;

public class Pagination extends Objectx {

    /**
     * 查询页号
     */
    private int page;

    /**
     * 每页记录数
     */
    private int rows;

    /**
     * 总记录数
     */
    private int total;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 排序方式
     */
    private String order;

    /**
     * 查询SQL
     */
    private String msql;

    /**
     * 统计SQL
     */
    private String csql;

    /**
     * 查询条件
     */
    private KoC[] kocs;

    /**
     * 查询数据结果
     */
    private List<Map<String, Object>> datas;

    /**
     * 查询数据统计项
     */
    private List<Map<String, Object>> stats;

    public Pagination(String msql, String csql, KoC[] kocs) {
        this.msql = msql;
        this.csql = csql;
        this.kocs = kocs;
    }

    public void set(int page, int rows) {
        this.page = page;
        this.rows = rows;
    }

    public void set(String sort, String order) {
        this.sort = sort;
        this.order = order;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMsql() {
        return msql;
    }

    public void setMsql(String msql) {
        this.msql = msql;
    }

    public String getCsql() {
        return csql;
    }

    public void setCsql(String csql) {
        this.csql = csql;
    }

    public List<Map<String, Object>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, Object>> datas) {
        this.datas = datas;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setKocs(KoC[] kocs) {
        this.kocs = kocs;
    }

    public KoC[] getKocs() {
        return kocs;
    }

    public void setStats(List<Map<String, Object>> stats) {
        this.stats = stats;
    }

    public List<Map<String, Object>> getStats() {
        return stats;
    }
}
