import db.DbInit;
import javafx.application.Application;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Check DB Connection first
            System.out.println("Checking database connection...");
            DbInit.checkConnection();

            // 2. Launch JavaFX
            Application.launch(App.class, args);

        } catch (SQLException e) {
            System.err.println("Could not connect to the database. UI will not start.");
            e.printStackTrace();
        }
    }
}