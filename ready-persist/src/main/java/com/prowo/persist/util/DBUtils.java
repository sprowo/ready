package com.prowo.persist.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Wrapper;

public final class DBUtils {

    public static void closes(Wrapper... wrappers) {
        for (Wrapper w : wrappers) {
            if (w instanceof Connection) {
                close((Connection) w);
            } else if (w instanceof PreparedStatement) {
                close((PreparedStatement) w);
            } else if (w instanceof ResultSet) {
                close((ResultSet) w);
            }
        }
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    public static void close(PreparedStatement pstm) {
        if (pstm != null) {
            try {
                pstm.close();
            } catch (Exception e) {
            }
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
    }
}
