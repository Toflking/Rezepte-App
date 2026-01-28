package db;

import java.sql.Connection;
import java.sql.SQLException;

public class DbInit {

    public static void checkConnection() throws SQLException {
        try (Connection conn = Database.getConnection()) {
            System.out.println("DB connection OK");
        } catch (SQLException ex) {
            System.out.println("DB connection ERROR");
        }

    }
}
