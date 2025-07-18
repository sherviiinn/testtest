package org.example.database;

import org.example.model.Units.Unit;
import org.example.database.UnitFactory; // فرض می‌کنیم برای ساخت یونیت‌ها کلاس UnitFactory داری
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitDAO {

    public static void insertUnit(Unit unit) {
        create();
        String sql = """
            INSERT INTO units(type, player_id, row, col, health, damage, range, food_cost, gold_cost, size)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, unit.getType());
            stmt.setInt(2, unit.getPlayerId());
            stmt.setInt(3, unit.getRow());
            stmt.setInt(4, unit.getCol());
            stmt.setInt(5, unit.getHealth());
            stmt.setInt(6, unit.getDamage());
            stmt.setInt(7, unit.getRange());
            stmt.setInt(8, unit.getFoodCost());
            stmt.setInt(9, unit.getGoldCost());
            stmt.setInt(10, unit.getSize());
            stmt.executeUpdate();
            System.out.println("Unit inserted.");
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    public static List<Unit> getUnitsByPlayerId(int playerId) {
        List<Unit> list = new ArrayList<>();
        String sql = "SELECT * FROM units WHERE player_id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Unit u = UnitFactory.createUnit(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getInt("player_id"),
                        rs.getInt("health"),
                        rs.getInt("row"),
                        rs.getInt("col")
                );
                list.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Load units failed: " + e.getMessage());
        }
        return list;
    }

    public static void updateUnit(Unit unit) {
        String sql = "UPDATE units SET health = ?, row = ?, col = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, unit.getHealth());
            stmt.setInt(2, unit.getRow());
            stmt.setInt(3, unit.getCol());
            stmt.setInt(4, unit.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Unit updated successfully.");
            } else {
                System.out.println("No unit found with ID " + unit.getId());
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }
    public static void create(){
        DatabaseManager.createTables();
    }
    public static int getLastUnitIdFromDatabase() {
        String sql = "SELECT MAX(id) AS last_id FROM units";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("last_id");
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Failed to fetch last unit ID: " + e.getMessage());
        }

        return 0; // اگر چیزی پیدا نشد
    }

}
