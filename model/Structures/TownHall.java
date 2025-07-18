package model.Structures;

public class TownHall extends Structure {
    private int foodProduction;
    private int goldProduction;
    private int unitSpace;

    public TownHall(int id, int playerId, int row, int col) {
        super(id, "TownHall", playerId, row, col, 50, 1, 0);
        this.foodProduction = 5;
        this.goldProduction = 5;
        this.unitSpace = 5;
    }

    @Override
    public void upgrade() {
        System.out.println("TownHall cannot be upgraded");
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    public int getFoodProduction() { return foodProduction; }
    public int getGoldProduction() { return goldProduction; }
    public int getUnitSpace() { return unitSpace; }
    public boolean canProduceUnit() { return true; }
}