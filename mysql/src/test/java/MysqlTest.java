import org.omni.mysql.client.Mysql;

import java.sql.SQLException;

/**
 * @author Xieningjun
 * @date 2024/11/8 17:38
 * @description
 */
public class MysqlTest {

    public static void main(String[] args) throws SQLException {
        var mysql = new Mysql(
                "10.0.2.4",
                63306,
                "gac_travel_dev_new",
                "NJElna9OCLisAi#5RfY8oi#M1Qp71ZX"
        );
        mysql.register();
        System.out.println(mysql.explain("gac_sensitive", "select * from my_code;"));
        System.out.println(mysql.explain("gac_sensitive", "select max(id) from my_code;"));
        System.out.println(mysql.explain("gac_sensitive", "insert into my_code (code, status) values (\"2\", \"2000\");"));
        System.out.println(mysql.explain("gac_sensitive", "SELECT * FROM (SELECT 1) AS subquery;"));
        System.out.println(mysql.count("gac_sensitive", "my_code"));
    }

}
