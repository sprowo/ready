package com.prowo.persist.impl;

import com.prowo.persist.LobHandlerFactory;
import com.prowo.persist.Objectx;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.lob.LobHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 默认的LobHandler工厂
 */
public class TacitLobHandlerFactory extends Objectx implements LobHandlerFactory {

    private Map<String, LobHandler> handlers;

    @Override
    public LobHandler getObject(DataSource source) {
        Connection conn = null;
        try {
            conn = source.getConnection();
            String name = conn.getMetaData().getDriverName();
            for (Entry<String, LobHandler> e : handlers.entrySet()) {
                if (name.matches(e.getKey())) {
                    return e.getValue();
                }
            }
        } catch (Exception e) {
            logger.error("source==" + source, e);
        } finally {
            JdbcUtils.closeConnection(conn);
        }
        return null;
    }

    public Map<String, LobHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(Map<String, LobHandler> handlers) {
        this.handlers = handlers;
    }
}
