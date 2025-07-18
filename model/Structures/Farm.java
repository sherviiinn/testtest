package model.Structures;

public class Farm extends Structure {
    private int foodProduction;
    private int buildCost;
    private int upgradeCost;

    public Farm(int id, int playerId, int row, int col) {
        super(id, "Farm", playerId, row, col, 50, 1, 5);
        this.foodProduction = 5;
        this.buildCost = 5;
        this.upgradeCost = 5;
    }

    @Override
    public void upgrade() {
        if (canUpgrade()) {
            level++;
            foodProduction += 5;
            health += 10;
            upgradeCost += 5;
            System.out.println("Farm upgraded to level " + level);
        } else {
            System.out.println("Farm is already at max level!");
        }
    }

    public boolean canUpgrade() { return level < 3; }
    public int getFoodProduction() { return foodProduction; }
    public int getBuildCost() { return buildCost; }
    public int getUpgradeCost() { return upgradeCost; }
}
