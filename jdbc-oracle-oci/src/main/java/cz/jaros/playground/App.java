package cz.jaros.playground;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {
        try {
            var app = new App();
            app.testThinConnection();
            app.testOciConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void testThinConnection() throws SQLException {
        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@webcluster-t.test:1521/IMWDBT");
        ods.setUser("CUPI_USR");
        ods.setPassword("jVkacYzMH3nm");
        try (Connection conn = ods.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("JDBC driver version is " + meta.getDriverVersion());
        }
    }

    void testOciConnection() throws SQLException {
        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:oci:@//webcluster-t.test:1521/IMWDBT");
        // CupiDB by muselo byt definovane v tnsnames.ora
//        ods.setURL("jdbc:oracle:oci:@CupiDB");
        ods.setUser("CUPI_USR");
        ods.setPassword("jVkacYzMH3nm");
        try (Connection conn = ods.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("JDBC driver version is " + meta.getDriverVersion());
        }
    }
}
