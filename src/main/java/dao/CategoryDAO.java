package dao;

import db.Database;
import model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    // SQL Strings
    private static final String CREATE_CATEGORY = "INSERT INTO categories (name) VALUES (?)";
    private static final String GET_CATEGORY_BY_ID = "SELECT * FROM categories WHERE id = ?";
    private static final String GET_CATEGORY_BY_NAME = "SELECT * FROM categories WHERE name = ?";
    private static final String LIST_CATEGORIES = "SELECT * FROM categories";
    private static final String UPDATE_CATEGORY = "UPDATE categories SET name = ? WHERE id = ?";

    // C
    public int createCategory(String name) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    // R
    public Category getCategoryById(int id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_CATEGORY_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildCategory(rs);
                }
            }
        }
        return null;
    }

    public Category getCategoryByName(String name) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_CATEGORY_BY_NAME)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildCategory(rs);
                }
            }
        }
        return null;
    }

    public List<Category> listCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(LIST_CATEGORIES);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(buildCategory(rs));
            }
        }
        return categories;
    }

    // U
    public boolean updateCategory(Category category) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CATEGORY)) {
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());
            return stmt.executeUpdate() == 1;
        }
    }

    private Category buildCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        return category;
    }
}

