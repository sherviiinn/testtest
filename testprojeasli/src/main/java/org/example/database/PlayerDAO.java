package org.example.database;

import org.example.model.*;
import java.sql.*;

public class PlayerDAO {
    public static void insertPlayer(Player player) {
        String sql = "INSERT INTO players(name, gold, food) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName());
            pstmt.setInt(2, player.getGold());
            pstmt.setInt(3, player.getFood());
            pstmt.executeUpdate();
            System.out.println("Player inserted.");
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    public static Player getPlayerById(int id) {
        String sql = "SELECT * FROM players WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Player(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("gold"),
                        rs.getInt("food")
                );
            }
        } catch (SQLException e) {
            System.out.println("Retrieval failed: " + e.getMessage());
        }
        return null;
    }
}
