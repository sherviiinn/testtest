package view;

import org.example.controller.GameController;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Realm War");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);

        GamePanel gamePanel = new GamePanel();
        ActionPanel actionPanel = new ActionPanel(gamePanel.getGameController(), gamePanel);
        InfoPanel infoPanel = new InfoPanel(gamePanel.getGameController());
        add(infoPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);


        setLocationRelativeTo(null); // وسط صفحه
        setVisible(true);
    }
}
