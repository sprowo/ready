package com.prowo.ydnamic.persist;

import com.prowo.persist.KoC;
import com.prowo.persist.util.ToteBox;

import java.util.List;
import java.util.Map;

public class TacitOraclePagingAccess extends TacitMySQLPagingAccess {

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
        msql = "SELECT * FROM (SELECT A .*, ROWNUM RN FROM (" + msql + ") A  WHERE   ROWNUM <= ?) WHERE RN > ?";
        Object[] params = new Object[factors.length + 2];
        System.arraycopy(factors, 0, params, 0, factors.length);
        params[factors.length] = page.getPage() * page.getRows();
        params[factors.length + 1] = (page.getPage() - 1) * page.getRows();
        List<Map<String, Object>> datas = this.querys(msql, params);
        page.setDatas(datas);
    }

    @Override
    public String getLimitString(String sql, int offset, int limit) {

        sql = sql.trim();
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);

        pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");

        pagingSelect.append(sql);

        pagingSelect.append(" ) row_ ) where rownum_ > ").append(offset).append(" and rownum_ <= ")
                .append(offset + limit);

        return pagingSelect.toString();
    }

}
