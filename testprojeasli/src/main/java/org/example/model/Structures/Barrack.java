package org.example.model.Structures;

public class Barrack extends Structure {
    private int capacity;
    private int buildCost;
    private int upgradeCost;

    public Barrack(int id, int playerId, int row, int col) {
        super(id, "Barrack", playerId, row, col, 50, 1, 5);
        this.capacity = 5;
        this.buildCost = 5;
        this.upgradeCost = 5;
    }

    @Override
    public void upgrade() {
        if (canUpgrade()) {
            level++;
            capacity += 5;
            health += 10;
            if(level == 2) {
                upgradeCost += 3;
            }else if(level == 3) {
                upgradeCost += 4;
            }

            System.out.println("Barrack upgraded to level " + level);
        } else {
            System.out.println("Barrack is already at max level!");
        }
    }

    public boolean canUpgrade() { return level < 3; }
    public int getCapacity() { return capacity; }
    public int getBuildCost() { return buildCost; }
    public int getUpgradeCost() { return upgradeCost; }
    public int getUnitSpace() {
        return this.capacity;
    }

}