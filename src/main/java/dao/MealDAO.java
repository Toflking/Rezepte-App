package dao;

import db.Database;
import model.Meal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Backend
// Alle Methoden für Meals Objekte
public class MealDAO {
    // SQL Strings
    private static final String CREATE_MEAL = "INSERT INTO meals (name, category_id, area_id, instructions, thumb, youtube, source, tags) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_MEAL_BY_ID = "SELECT * FROM meals WHERE id = ?";
    private static final String LIST_MEALS = "SELECT * FROM meals";
    private static final String LIST_MEALS_BY_NAME_CONTAINS = "SELECT * FROM meals WHERE name LIKE ? ORDER BY name";
    private static final String LIST_MEALS_BY_INGREDIENT = "SELECT DISTINCT m.* FROM meals m JOIN meal_ingredients mi ON mi.meal_id = m.id WHERE mi.ingredient_id = ?";
    private static final String LIST_MEALS_BY_CATEGORY = "SELECT * FROM meals WHERE category_id = ?";
    private static final String LIST_MEALS_BY_AREA = "SELECT * FROM meals WHERE area_id = ?";
    private static final String UPDATE_MEAL = "UPDATE meals SET name = ?, category_id = ?, area_id = ?, instructions = ?, thumb = ?, youtube = ?, source = ?, tags = ? WHERE id = ?";
    private static final String DELETE_MEAL = "DELETE FROM meals WHERE id = ?";


    // C
    public int createMeal(Meal meal) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_MEAL)) {
            bindMealParams(stmt, meal);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    // R
    public Meal getMealById(int id) throws SQLException {
        try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(GET_MEAL_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildMeal(rs);
                }
            }
        }
        return null;
    }

    public List<Meal> listMeals() throws SQLException {
        List<Meal> meals = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(LIST_MEALS)) {
            while (rs.next()) {
                Meal meal = buildMeal(rs);
                meals.add(meal);
            }
        }
        return meals;
    }

    public List<Meal> listMealsByNameContains(String letters) throws SQLException {
        List<Meal> meals = new ArrayList<>();
        try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(LIST_MEALS_BY_NAME_CONTAINS)) {
            stmt.setString(1, "%" + letters + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Meal meal = buildMeal(rs);
                    meals.add(meal);
                }
            }
        }
        return meals;
    }

    public List<Meal> listMealsByIngredient(int ingredientId) throws SQLException {
        return listMealsByIntParam(LIST_MEALS_BY_INGREDIENT, ingredientId);
    }

    public List<Meal> listMealsByCategory(int categoryId) throws SQLException {
        return listMealsByIntParam(LIST_MEALS_BY_CATEGORY, categoryId);
    }

    public List<Meal> listMealsByArea(int areaId) throws SQLException {
        return listMealsByIntParam(LIST_MEALS_BY_AREA, areaId);
    }

    // U
    public boolean updateMeal(Meal meal) throws SQLException {
        try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(UPDATE_MEAL)) {
            bindMealParams(stmt, meal);
            stmt.setInt(9, meal.getId());
            return stmt.executeUpdate() == 1;
        }
    }

    // D
    public boolean deleteMealById(int id) throws SQLException {
        try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(DELETE_MEAL)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() == 1;
        }
    }

    // Helper
    private void bindMealParams(PreparedStatement stmt, Meal meal) throws SQLException {
        stmt.setString(1, meal.getName());
        stmt.setObject(2, meal.getCategory_id(), Types.INTEGER);
        stmt.setObject(3, meal.getArea_id(), Types.INTEGER);
        stmt.setString(4, meal.getInstructions());
        stmt.setString(5, meal.getThumb());
        stmt.setString(6, meal.getYoutube());
        stmt.setString(7, meal.getSource());
        stmt.setString(8, meal.getTags());
    }

    private Meal buildMeal(ResultSet rs) throws SQLException {
        Meal meal = new Meal();
        meal.setId(rs.getInt("id"));
        meal.setExternal_id(rs.getInt("external_id"));
        meal.setName(rs.getString("name"));
        meal.setCategory_id((Integer) rs.getObject("category_id"));
        meal.setArea_id((Integer) rs.getObject("area_id"));
        meal.setInstructions(rs.getString("instructions"));
        meal.setThumb(rs.getString("thumb"));
        meal.setYoutube(rs.getString("youtube"));
        meal.setSource(rs.getString("source"));
        meal.setTags(rs.getString("tags"));
        meal.setCreated_at(rs.getTimestamp("created_at"));
        meal.setUpdated_at(rs.getTimestamp("updated_at"));
        return meal;
    }

    private List<Meal> listMealsByIntParam(String sql, int paramId) throws SQLException {
        List<Meal> meals = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paramId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Meal meal = buildMeal(rs);
                    meals.add(meal);
                }
            }
        }
        return meals;
    }
}
