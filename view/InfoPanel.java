package view;

import org.example.controller.GameController;
import org.example.model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class InfoPanel extends JPanel {
    private JLabel goldLabel, foodLabel, unitSpaceLabel, turnLabel, gainLabel;
    private GameController controller;
    private Timer timer;
    private int timeLeft;

    public InfoPanel(GameController controller) {
        this.controller = controller;
        setLayout(new GridLayout(2, 3));
        setPreferredSize(new Dimension(800, 60));

        turnLabel = new JLabel("Turn: -");
        goldLabel = new JLabel("Gold: -");
        foodLabel = new JLabel("Food: -");
        unitSpaceLabel = new JLabel("Unit Space: -");
        gainLabel = new JLabel("");
        gainLabel.setForeground(Color.GREEN);

        add(turnLabel);
        add(goldLabel);
        add(foodLabel);
        add(unitSpaceLabel);
        add(gainLabel);
        for (int i=0;i<controller.getPlayers().size();i++) {
            controller.nextTurn();
        }
        startTurnTimer();
    }

    public void updateInfo() {
        Player player = controller.getCurrentPlayer();
        turnLabel.setText("Turn: Player " + player.getId());
        goldLabel.setText("Gold: " + player.getGold());
        foodLabel.setText("Food: " + player.getFood());
        unitSpaceLabel.setText("Unit Space: " + player.getUsedUnitSpace() + "/" + player.getTotalUnitSpace());
    }

    public void showResourceGain(int goldGain, int foodGain) {
        gainLabel.setText("+" + goldGain + " Gold, +" + foodGain + " Food");
        new javax.swing.Timer(3000, e -> gainLabel.setText("")).start();
    }

    public void startTurnTimer() {

        updateInfo();
        timeLeft = 30;

        if (timer != null) timer.cancel();
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    turnLabel.setText("Turn: Player " + controller.getCurrentPlayer().getId() + " [" + timeLeft + "s]");
                    timeLeft--;

                    if (timeLeft < 0) {
                        timer.cancel();
                        controller.nextTurn(); // اطمینان حاصل کن متد endTurn در کنترلر هست
                        startTurnTimer();
                         // بروزرسانی منابع
                    }
                });
            }
        }, 0, 1000);
    }
}
