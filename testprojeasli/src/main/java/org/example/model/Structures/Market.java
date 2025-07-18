package org.example.model.Structures;

public class Market extends Structure {
    private int goldProduction;
    private int buildCost;
    private int upgradeCost;

    public Market(int id, int playerId, int row, int col) {
        super(id, "Market", playerId, row, col, 50, 1, 5);
        this.goldProduction = 5;
        this.buildCost = 5;
        this.upgradeCost = 5;
    }

    @Override
    public void upgrade() {
        if (canUpgrade()) {
            level++;
            goldProduction += 5;
            health += 10;
            upgradeCost += 5;
            System.out.println("Market upgraded to level " + level);
        } else {
            System.out.println("Market is already at max level!");
        }
    }

    public boolean canUpgrade() { return level < 3; }
    public int getGoldProduction() { return goldProduction; }
    public int getBuildCost() { return buildCost; }
    public int getUpgradeCost() { return upgradeCost; }
}