package view;

import dao.MealDAO;
import model.Meal;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import java.sql.SQLException;
import java.util.List;

public class MainController {

    @FXML
    private ListView<String> mealList;

    private final MealDAO mealDAO = new MealDAO();

    /**
     * This method is called by JavaFX automatically
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        refreshMealList();
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

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}