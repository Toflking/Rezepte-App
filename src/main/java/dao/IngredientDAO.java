package dao;

import db.Database;
import model.Ingredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Backend
// Alle Methoden für Ingredient Objekte
public class IngredientDAO {
    // SQL Strings
    private static final String CREATE_INGREDIENT = "INSERT INTO ingredients (name) VALUES (?)";
    private static final String GET_INGREDIENT_BY_ID = "SELECT * FROM ingredients WHERE id = ?";
    private static final String GET_INGREDIENT_BY_NAME = "SELECT * FROM ingredients WHERE name = ?";
    private static final String LIST_INGREDIENTS = "SELECT * FROM ingredients";
    private static final String UPDATE_INGREDIENT = "UPDATE ingredients SET name = ? WHERE id = ?";

    // C
    public int createIngredient(String name) throws SQLException {
        try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(CREATE_INGREDIENT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    // R
    public Ingredient getIngredientById(int id) throws SQLException {
        try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(GET_INGREDIENT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildIngredient(rs);
                }
            }
        }
        return null;
    }

    public Ingredient getIngredientByName(String name) throws SQLException {
        try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(GET_INGREDIENT_BY_NAME)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildIngredient(rs);
                }
            }
        }
        return null;
    }

    public List<Ingredient> listIngredients() throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(LIST_INGREDIENTS)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(buildIngredient(rs));
                }
            }
        }
        return ingredients;
    }

    // U
    public boolean updateIngredient(Ingredient ingredient) throws SQLException {
        try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(UPDATE_INGREDIENT)) {
            stmt.setString(1, ingredient.getName());
            stmt.setInt(2, ingredient.getId());
            return stmt.executeUpdate() == 1;
        }
    }
    // Helper
    private Ingredient buildIngredient(ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getInt("id"));
        ingredient.setName(rs.getString("name"));
        return ingredient;
    }
}
