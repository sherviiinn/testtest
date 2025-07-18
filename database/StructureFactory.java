package database;

import org.example.model.Player;
import org.example.model.Structures.*;

public class StructureFactory {
    private static int globalStructureId = 0;

    public static Structure createStructure(String type, int id, int playerId,
                                            int row, int col, int health, int level) {
        Structure s = switch (type) {
            case "Farm" -> new Farm(id, playerId, row, col);
            case "Market" -> new Market(id, playerId, row, col);
            case "Barrack" -> new Barrack(id, playerId, row, col);
            case "Tower" -> new Tower(id, playerId, row, col);
            case "TownHall" -> new TownHall(id, playerId, row, col);
            default -> throw new IllegalArgumentException("نوع سازه نامعتبر: " + type);
        };

        if (s != null) {
            s.setHealth(health);
            for (int i = 1; i < level; i++) {
                if (s.canUpgrade()) {
                    s.upgrade();
                }
            }
        }

        return s;
    }

    public static Structure firstcreateStructure(String type, Player player, int row, int col) {
        int id = ++globalStructureId; // تولید id یکتا و جهانی
        int level = 1;

        int health;
        // مقداردهی سلامت پیش‌فرض بر اساس نوع سازه
        switch (type) {
            case "Farm", "Market" -> health = 80;
            case "Barrack" -> health = 150;
            case "Tower" -> health = 200;
            case "TownHall" -> health = 300;
            default -> throw new IllegalArgumentException("نوع سازه نامعتبر: " + type);
        }

        return createStructure(type, id, player.getId(), row, col, health, level);
    }
}
