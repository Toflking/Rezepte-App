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

public class MainController {

    @FXML private ListView<String> mealList;
    @FXML private TextField searchField;

    private final MealDAO mealDAO = new MealDAO();
    private final Service service = new Service();
    private final PauseTransition debounce = new PauseTransition(Duration.millis(300));

    // initialize wird ähnlich wie main automatisch ausgeführt, jedoch erst, nachdem das fxml file geladen wurde
    @FXML
    public void initialize() {
        refreshMealList();

        // debounce um nicht zu viele DB anfragen zu machen
        debounce.setOnFinished(e -> runSearch());
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            debounce.playFromStart();
        });
    }

    @FXML
    private void refreshMealList() {
        try {
            // Use your friend's 'listMeals' method
            List<Meal> meals = mealDAO.listMeals();

            // Clear the UI list before adding new items
            mealList.getItems().clear();

            for (Meal meal : meals) {
                // Use your model's 'getName' method
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
        Thread t = new Thread(task);
        // Damit die App sauber schliesst, auch wenn eine Task noch am Laufen ist
        t.setDaemon(true);
        t.start();

    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}