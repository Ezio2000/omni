package org.omni.mysql.client;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.omni.mysql.command.SafeCommand;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Xieningjun
 * @date 2024/11/8 17:18
 * @description
 */
public class Mysql implements SafeCommand {

    protected final String host;

    protected final int port;

    protected final String username;

    protected final String password;

    // todo 要不要强行改成hikariPool
    protected DataSource dataSource;

    public Mysql(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public synchronized void register() {
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://%s:%s".formatted(host, port));
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        var dataSource = new HikariDataSource(config);
        register(dataSource);
    }

    public synchronized void register(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ExplainHolder explain(String db, String sql) throws SQLException {
        var holder = new ExplainHolder();
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("USE %s".formatted(db));
            try (var resSet = stmt.executeQuery("EXPLAIN %s".formatted(sql))) {
                while (resSet.next()) {
                    holder.setTable(resSet.getString("table"));
                    holder.setType(resSet.getString("select_type"));
                    holder.setKey(resSet.getString("key"));
                    holder.setRow(resSet.getInt("rows"));
                    holder.setExtra(resSet.getString("extra"));
                }
            }
        }
        return holder;
    }

    @Override
    public long count(String db, String table) throws SQLException {
        long holder = 0L;
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("USE %s".formatted(db));
            try (var resSet = stmt.executeQuery("SHOW TABLE STATUS LIKE \"%s\"".formatted(table))) {
                while (resSet.next()) {
                    holder = resSet.getLong("Rows");
                }
            }
        }
        return holder;
    }

}
