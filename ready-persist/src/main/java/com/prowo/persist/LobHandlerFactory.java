package com.prowo.persist;

import javax.sql.DataSource;

import org.springframework.jdbc.support.lob.LobHandler;

public interface LobHandlerFactory {
    /**
     * 获得LobHandler实现类
     *
     * @param source
     * @return LobHandler实现类
     */
    public LobHandler getObject(DataSource source);
}
