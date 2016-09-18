package com.prowo.persist.impl;

import com.prowo.persist.Blob;
import com.prowo.persist.Clob;
import org.springframework.beans.BeanWrapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.lob.LobHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


public class LobPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {

    /**
     * 大数据操作
     */
    private LobHandler lobHandler;

    /**
     * Clob字段
     */
    private Set<String> clobs;

    /**
     * Blob字段
     */
    private Set<String> blobs;

    public LobPropertyRowMapper() {
        super();
    }

    public LobPropertyRowMapper(Class<T> mappedClass, LobHandler lobHandler) {
        super(mappedClass);
        this.lobHandler = lobHandler;
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        if (lobHandler == null) {
            return super.getColumnValue(rs, index, pd);
        } else if (clobs.contains(pd.getName())) {
            return lobHandler.getClobAsString(rs, index);
        } else if (blobs.contains(pd.getName())) {
            return lobHandler.getBlobAsBytes(rs, index);
        } else {
            return super.getColumnValue(rs, index, pd);
        }
    }

    @Override
    protected void initBeanWrapper(BeanWrapper bw) {
        clobs = new HashSet<String>();
        blobs = new HashSet<String>();
        Field[] fields = bw.getWrappedClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.getAnnotation(Clob.class) != null) {
                clobs.add(f.getName());
            }
            if (f.getAnnotation(Blob.class) != null) {
                blobs.add(f.getName());
            }
        }
    }

    public LobHandler getLobHandler() {
        return lobHandler;
    }

    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    /**
     * 获得RowMapper实例
     *
     * @param mappedClass
     * @param lobHandler
     * @return
     */
    public static <T> LobPropertyRowMapper<T> newInstance(Class<T> mappedClass, LobHandler lobHandler) {
        LobPropertyRowMapper<T> newInstance = new LobPropertyRowMapper<T>();
        newInstance.setMappedClass(mappedClass);
        newInstance.setLobHandler(lobHandler);
        return newInstance;
    }
}
