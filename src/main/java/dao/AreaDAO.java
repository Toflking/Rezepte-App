package dao;

import db.Database;
import model.Area;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Backend
// Alle Methoden für Area Objekte
public class AreaDAO {
    // SQL Strings
    private static final String CREATE_AREA = "INSERT INTO areas (name) VALUES (?)";
    private static final String GET_AREA_BY_ID = "SELECT * FROM areas WHERE id = ?";
    private static final String GET_AREA_BY_NAME = "SELECT * FROM areas WHERE name = ?";
    private static final String LIST_AREAS = "SELECT * FROM areas";
    private static final String UPDATE_AREA = "UPDATE areas SET name = ? WHERE id = ?";

    // C
    public int createArea(String name) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_AREA, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    // R
    public Area getAreaById(int id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_AREA_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildArea(rs);
                }
            }
        }
        return null;
    }

    public Area getAreaByName(String name) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_AREA_BY_NAME)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildArea(rs);
                }
            }
        }
        return null;
    }

    public List<Area> listAreas() throws SQLException {
        List<Area> areas = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(LIST_AREAS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                areas.add(buildArea(rs));
            }
        }
        return areas;
    }

    // U
    public boolean updateArea(Area area) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_AREA)) {
            stmt.setString(1, area.getName());
            stmt.setInt(2, area.getId());
            return stmt.executeUpdate() == 1;
        }
    }
    // Helper
    private Area buildArea(ResultSet rs) throws SQLException {
        Area area = new Area();
        area.setId(rs.getInt("id"));
        area.setName(rs.getString("name"));
        return area;
    }
}

