package view;

import dao.MealDAO;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import model.Meal;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import java.sql.SQLException;
import java.util.List;

// Hauptmethode für das Frontend, kontrolliert alle Vorgänge in der App
public class MainController {

    // Felder in der App
    @FXML private ListView<String> mealList;
    @FXML private TextField searchField;

    // Erstellen der Objekte
    private final MealDAO mealDAO = new MealDAO();
    private final Service service = new Service();
    private final PauseTransition debounce = new PauseTransition(Duration.millis(300));

    // initialize wird ähnlich wie main automatisch ausgeführt, jedoch erst, nachdem das fxml file geladen wurde
    @FXML
    public void initialize() {
        // lädt die meal list
        refreshMealList();

        // debounce um nicht zu viele DB anfragen zu machen
        debounce.setOnFinished(e -> runSearch());
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            debounce.playFromStart();
        });
    }

    @FXML
    // um die meal list zu laden
    private void refreshMealList() {
        try {
            // ListMeals methode benutzen um alle Meals zu bekommen
            List<Meal> meals = mealDAO.listMeals();

            // Ui clearen vor dem einfügen
            mealList.getItems().clear();

            for (Meal meal : meals) {
                // Für alle Meals die Namen in die Liste eintragen
                mealList.getItems().add(meal.getName());
            }
        } catch (SQLException e) {
            showError("Database Error", "Could not load meals: " + e.getMessage());
        }
    }

    @FXML
    private void runSearch() {
        // Text extrahieren
        String query = searchField.getText();
        // Nach Meals suchen, die query drin haben
        Task<List<Meal>> task = new Task<>() {
            @Override
            protected List<Meal> call() throws Exception {
                return service.searchMeals(query);
            }
        };
        // mealList updaten mit gesuchten Meals
        task.setOnSucceeded(e -> {
            mealList.getItems().clear();
            for (Meal meal : task.getValue()) {
                mealList.getItems().add(meal.getName());
            }
        });
        // Error Log
        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            showError("Database Error", ex != null ? ex.getMessage() : "Unknown error");
        });
        // thread erstellen mit der task
        Thread t = new Thread(task);
        // Damit die App sauber schliesst, auch wenn ein Task noch am Laufen ist
        t.setDaemon(true);
        t.start();

    }

    // helper methode um errors in der app anzuzeigen
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}