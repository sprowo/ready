package com.prowo.ydnamic.handler;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;
import com.prowo.ydnamic.mapper.JSONMapper;
import com.prowo.ydnamic.persist.Pagination;
import com.prowo.ydnamic.record.Recorder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapModel {

    private static final String MODEL_RESULT = "result";
    private static final String MODEL_SUCCESS = "success";
    private static final String MODEL_CODE = "code";
    private static final String MODEL_ERROR_CODE = "errorcode";
    private static final String MODEL_MESSAGE = "message";
    private static final String MODEL_MSG = "msg";
    private static final String MODEL_DATA = "data";
    private static final String MODEL_DATAS = "datas";
    private static final String MODEL_ROWS = "rows";
    private static final String MODEL_FOOTER = "footer";
    private static final String MODEL_TOTAL = "total";
    private static final String MODEL_EXT = "ext";
    private Map<String, Object> map;

    public static MapModel getModelInstance() {
        return new MapModel();
    }

    private MapModel() {
        map = new HashMap<String, Object>();
    }

    public void setResult(boolean result) {
        Recorder.setServiceResult(result);
        map.put(MODEL_RESULT, result);
    }

    public boolean getResult() {
        return (Boolean) map.get(MODEL_RESULT);
    }

    public void setSucess(boolean sucess) {
        map.put(MODEL_SUCCESS, sucess);
    }

    public boolean getSucess() {
        return (Boolean) map.get(MODEL_SUCCESS);
    }

    public void setMessage(String message) {
        map.put(MODEL_MESSAGE, message);
    }

    public String getMessage() {
        return (String) map.get(MODEL_MESSAGE);
    }

    public void setData(Map<String, Object> map2) {
        if (map2 == null)
            return;
        map.put(MODEL_DATA, map2);

    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getData() {
        return (Map<String, Object>) map.get(MODEL_DATA);

    }

    public void setMsg(String msg) {
        map.put(MODEL_MSG, msg);
    }

    public String getMsg() {
        return (String) map.get(MODEL_MSG);
    }

    @SuppressWarnings("unchecked")
    public void setBody(String body) {
        Map<String, Object> obj;
        try {
            obj = JSONMapper.fromJson(body, Map.class);
            map.putAll(obj);
        } catch (Exception e) {
            try {
                map.put(MODEL_DATAS, JSONMapper.fromJson(body));
            } catch (Exception e1) {
                map.put(MODEL_DATA, body);
            }

        }

    }

    public void setExt(Map<String, Object> ext) {
        if (ext == null) {
            return;
        }
        map.put(MODEL_EXT, ext);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getExt() {
        return (Map<String, Object>) map.get(MODEL_EXT);
    }

    public void setDatas(List<Map<String, Object>> datas) {
        if (datas == null) {
            return;
        }
        map.put(MODEL_DATAS, datas);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDatas() {
        return (List<Map<String, Object>>) map.get(MODEL_DATAS);
    }

    public Map<String, Object> getModel() {
        try {
            LoggerUtil.log(Level.DEBUG, "{0}", JSONMapper.toJson(this.map));
        } catch (Exception e) {
            LoggerUtil.log(Level.ERROR, e, "create model error");
        }
        return this.map;
    }

    public String toJson() throws Exception {
        return JSONMapper.toJson(this.map);
    }

    public void setCode(String code) {
        Recorder.setServiceErrorcode(code);
        if (code == null)
            return;
        map.put(MODEL_CODE, code);
    }

    public String getCode() {
        return (String) map.get(MODEL_CODE);
    }

    public void setErrorCode(String errorCode) {
        Recorder.setServiceErrorcode(errorCode);
        if (errorCode == null)
            return;
        map.put(MODEL_ERROR_CODE, errorCode);
    }

    public String getErrorCode() {
        return (String) map.get(MODEL_ERROR_CODE);
    }

    public void setRows(List<Map<String, Object>> rows) {
        if (rows == null) {
            return;
        }
        map.put(MODEL_ROWS, rows);
    }

    public void setFooter(List<Map<String, Object>> footer) {
        if (footer == null) {
            return;
        }
        map.put(MODEL_FOOTER, footer);
    }

    public void setTotal(int total) {
        map.put(MODEL_TOTAL, total);
    }

    public void setPagination(Pagination page) {
        setRows(page.getDatas());
        setTotal(page.getTotal());
        setFooter(page.getStats());
    }
}
