package org.omni.mysql.command;

import lombok.Data;

import java.sql.SQLException;

/**
 * @author Xieningjun
 * @date 2024/11/11 12:04
 * @description
 */
public interface SafeCommand {

    ExplainHolder explain(String db, String sql) throws SQLException;

    long count(String db, String table) throws SQLException;

    @Data
    class ExplainHolder {
        private String table;
        private String type;
        private String key;
        private int row;
        private String extra;
    }

}
