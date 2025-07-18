package org.example.database;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/realmwar";
    private static final String USER = "postgres";
    private static final String PASSWORD = "shervin2006";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void createTables() {
        String players = """
        CREATE TABLE IF NOT EXISTS players (
            id SERIAL PRIMARY KEY,
            name TEXT NOT NULL,
            gold INTEGER,
            food INTEGER
        );
        """;

        String units = """
        CREATE TABLE IF NOT EXISTS units (
            id SERIAL PRIMARY KEY,
            type TEXT,
            player_id INTEGER,
            health INTEGER,
            damage INTEGER,
            range INTEGER,
            food_cost INTEGER,
            gold_cost INTEGER,
            size INTEGER,
            row INTEGER,
            col INTEGER
        );
        """;

        String structures = """
        CREATE TABLE IF NOT EXISTS structures (
            id SERIAL PRIMARY KEY,
            type TEXT,
            player_id INTEGER,
            row INTEGER,
            col INTEGER,
            health INTEGER,
            level INTEGER
        );
        """;

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(players);
            stmt.execute(units);
            stmt.execute(structures);
            System.out.println("All tables created.");
        } catch (SQLException e) {
            System.out.println("Table creation error: " + e.getMessage());
        }
    }

}
