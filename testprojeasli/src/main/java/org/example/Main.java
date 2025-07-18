package org.example;

import org.example.database.DatabaseManager;
import org.example.view.GameFrame;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GameFrame(); // اجرای بازی
        });
    }
}
