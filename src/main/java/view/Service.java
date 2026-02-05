package view;

import dao.MealDAO;
import model.Meal;

import java.sql.SQLException;
import java.util.List;

public class Service {
    public List<Meal> searchMeals(String query) throws SQLException {
        final MealDAO mealDAO = new MealDAO();
        // Wenn query leer dann alle Meals sonst query
        if (query == null || query.isBlank()) {
            return mealDAO.listMeals();
        }
        return mealDAO.listMealsByNameContains(query);
    }
}
