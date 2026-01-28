import db.Database;
import db.DbInit;

import java.sql.Connection;
import java.sql.SQLException;

class Main {
    public static void main(String[] args) throws SQLException {
        DbInit.checkConnection();
    }
}