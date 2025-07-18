package view;

import org.example.controller.GameController;
import org.example.model.Blocks.Block;
import org.example.model.Blocks.EmptyBlock;
import org.example.model.Blocks.ForestBlock;
import org.example.model.Blocks.VoidBlock;
import org.example.model.GameMap;
import org.example.model.Player;
import org.example.model.Units.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class GamePanel extends JPanel {

    private final int TILE_SIZE = 40;
    private final int MAP_WIDTH = 10;
    private final int MAP_HEIGHT = 10;
    private Timer turnTimer;
    private int remainingTime = 30;
    private int selectedRow = -1;
    private int selectedCol = -1;// ثانیه

    private Point selectedFrom = null;
    private GameController gameController;

    public GamePanel() {
        setPreferredSize(new Dimension(TILE_SIZE * MAP_WIDTH, TILE_SIZE * MAP_HEIGHT));
        setBackground(Color.WHITE);

        // مقادیر تستی اولیه
        GameMap gameMap = new GameMap(MAP_WIDTH, MAP_HEIGHT);
        List<Player> players = new ArrayList<>();
        players.add(new Player(1,"Player 1", 0,0)); // فرض: آی‌دی 1
        players.add(new Player(2,"Player 2", 0,0)); // فرض: آی‌دی 2

        gameController = new GameController(players, gameMap);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / TILE_SIZE;
                int row = e.getY() / TILE_SIZE;
                Point clicked = new Point(col, row);


                if (selectedFrom == null) {
                    selectedFrom = clicked;
                    System.out.println("Selected FROM: " + row + "," + col);
                } else {
                    System.out.println("Selected TO: " + row + "," + col);
                    handleAction(selectedFrom, clicked);
                    selectedFrom = null;
                }
                selectedRow = row;
                selectedCol = col;
                repaint();
            }
        });
    }

    private void handleAction(Point from, Point to) {
        Block fromBlock = gameController.getGameMap().getBlock(from.y, from.x);
        Block toBlock = gameController.getGameMap().getBlock(to.y, to.x);

        Unit fromUnit = fromBlock.getUnit();
        Unit toUnit = toBlock.getUnit();

        if (fromUnit == null) {
            System.out.println("❌ No unit to move.");
            return;
        }

        String[] parts = {
                String.valueOf(from.y),
                String.valueOf(from.x),
                String.valueOf(to.y),
                String.valueOf(to.x)
        };

        // ✅ تشخیص حمله vs حرکت
        if (toUnit != null && fromUnit.getPlayerId() != toUnit.getPlayerId()) {
            gameController.handleAttackCommand(parts);
        } else {
            gameController.handleMoveCommand(parts);
        }

        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        GameMap gameMap = gameController.getGameMap();


                for (int r = 0; r < gameMap.getRows(); r++) {
                    for (int c = 0; c < gameMap.getCols(); c++) {
                        Block block = gameMap.getBlock(r, c);

                        // 🎨 رنگ پس‌زمینه بر اساس نوع بلاک
                        if (block instanceof ForestBlock) {
                            g.setColor(new Color(66, 126, 46));
                        } else if (block instanceof EmptyBlock) {
                            g.setColor(new Color(174, 145, 58));
                        } else if (block instanceof VoidBlock) {
                            g.setColor(Color.DARK_GRAY);
                        } else {
                            g.setColor(Color.LIGHT_GRAY);
                        }

                        int x1 = c * TILE_SIZE;
                        int y1 = r * TILE_SIZE;
                        g.fillRect(x1, y1, TILE_SIZE, TILE_SIZE);

                        // 🟥 رنگ کادر اطراف بر اساس مالکیت
                        switch (block.getOwnerId()) {
                            case 1 -> g.setColor(Color.BLUE);
                            case 2 -> g.setColor(Color.WHITE);
                            case 3 -> g.setColor(Color.ORANGE);
                            case 4 -> g.setColor(Color.MAGENTA);
                            default -> g.setColor(Color.BLACK);
                        }
                        g.drawRect(x1, y1, TILE_SIZE, TILE_SIZE);
                        if (block.hasStructure()) {
                            String name = block.getStructure().getType();
                            String shortName = switch (name) {
                                case "TownHall" -> "TH";
                                case "Barrack" -> "BK";
                                case "Farm" -> "FM";
                                case "Market" -> "MK";
                                case "Tower" -> "TW";
                                default -> name.substring(0, 2);
                            };
                            g.drawString(shortName, x1 + 5, y1 + 15);

                            if (block.hasUnit()) {
                                g.setColor(Color.WHITE);

                                String unitSymbol = switch (block.getUnit().getType()) {
                                    case "Peasant" -> "P";
                                    case "Spearman" -> "S";
                                    case "Swordsman" -> "SW";
                                    case "Knight" -> "K";
                                    default -> "?";
                                };

                                g.drawString(unitSymbol, x1 + 20, y1 + 30);
                            }

                        }
                    }

        }

        // نمایش انتخاب شده با رنگ قرمز
        if (selectedFrom != null) {
            g.setColor(Color.RED);
            g.drawRect(selectedFrom.x * TILE_SIZE, selectedFrom.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    public GameController getGameController() {
        return gameController;
    }
    public int getSelectedRow() { return selectedRow; }
    public int getSelectedCol() { return selectedCol; }




}
