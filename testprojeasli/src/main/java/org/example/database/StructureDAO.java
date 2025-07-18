package org.example.database;

import org.example.model.Structures.Structure;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StructureDAO {
    public static void insertStructure(Structure structure) {
        String sql = """
            INSERT INTO structures(type, player_id, row, col, health, level)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, structure.getType());
            stmt.setInt(2, structure.getPlayerId());
            stmt.setInt(3, structure.getRow());
            stmt.setInt(4, structure.getCol());
            stmt.setInt(5, structure.getHealth());
            stmt.setInt(6, structure.getLevel());
            stmt.executeUpdate();
            System.out.println("Structure inserted.");
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }
    public static List<Structure> getStructuresByPlayerId(int playerId) {
        List<Structure> list = new ArrayList<>();
        String sql = "SELECT * FROM structures WHERE player_id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Structure s = StructureFactory.createStructure(
                        rs.getString("type"),
                        rs.getInt("id"),
                        rs.getInt("player_id"),
                        rs.getInt("row"),
                        rs.getInt("col"),
                        rs.getInt("health"),
                        rs.getInt("level")
                );
                list.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Load structures failed: " + e.getMessage());
        }
        return list;
    }
    public static void updateStructure(Structure structure) {
        String sql = "UPDATE structures SET health = ?, level = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, structure.getHealth());
            stmt.setInt(2, structure.getLevel());
            stmt.setInt(3, structure.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Structure updated successfully.");
            } else {
                System.out.println("No structure found with ID " + structure.getId());
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }
    public static void create(){
        DatabaseManager.createTables();
    }
    public static int getLastUnitIdFromDatabase() {
        String sql = "SELECT MAX(id) AS last_id FROM structures";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("last_id");
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Failed to fetch last structure ID: " + e.getMessage());
        }

        return 0; // اگر چیزی پیدا نشد
    }

}
