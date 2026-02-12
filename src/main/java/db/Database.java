package db;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Für die Verbindung mit der DB
public class Database {
    // .env File laden
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    // Datenbank Details aus dem .env laden
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");
    // Connection aufbauen mit der db
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

