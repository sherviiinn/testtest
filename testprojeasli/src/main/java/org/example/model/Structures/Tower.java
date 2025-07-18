package org.example.model.Structures;

import org.example.model.Units.Unit;

public class Tower extends Structure {
    private int attackPower;
    private int range;
    private int buildCost;
    private int upgradeCost;

    public Tower(int id, int playerId, int row, int col) {
        super(id, "Tower", playerId, row, col, 50, 1, 5);
        this.attackPower = 20;
        this.range = 1;
        this.buildCost = 5;
        this.upgradeCost = 5;
    }

    @Override
    public void upgrade() {
        if (canUpgrade()) {
            level++;
            attackPower += 10;
            range++;
            health += 10;
            upgradeCost += 5;
            System.out.println("Tower upgraded to level " + level);
        } else {
            System.out.println("Tower is already at max level!");
        }
    }
    public boolean blocks(Unit unit) {
        return unit.getRank() <= this.getLevel(); // اگر رتبه‌ی یونیت <= سطح Tower، جلوشو می‌گیره
    }


    public boolean canUpgrade() { return level < 3; }
    public int getAttackPower() { return attackPower; }
    public int getRange() { return range; }
    public int getBuildCost() { return buildCost; }
    public int getUpgradeCost() { return upgradeCost; }
}
