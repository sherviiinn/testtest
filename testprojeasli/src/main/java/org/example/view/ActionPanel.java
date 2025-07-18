package org.example.view;

import org.example.controller.GameController;

import javax.swing.*;
import java.awt.*;

public class ActionPanel extends JPanel {

    public ActionPanel(GameController controller, GamePanel gamePanel) {
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(800, 60));

        JButton recruitButton = new JButton("Recruit");

        recruitButton.addActionListener(e -> {
            String[] options = {"Peasant", "Knight","Spearman","Swordsman"};
            String choice = (String) JOptionPane.showInputDialog(
                    this,
                    "Select unit type to recruit:",
                    "Recruit Unit",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice != null) {
                int row = gamePanel.getSelectedRow();
                int col = gamePanel.getSelectedCol();

                if (row == -1 || col == -1) {
                    JOptionPane.showMessageDialog(this, "⚠️ No tile selected.");
                    return;
                }

                controller.produceUnit(choice, row, col);
                gamePanel.repaint();
            }
        });

        add(recruitButton);
    }
}
