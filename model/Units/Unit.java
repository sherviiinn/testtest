package model.Units;

import org.example.model.Blocks.Block;
import org.example.model.Blocks.ForestBlock;
import org.example.model.Structures.Structure;
import org.example.model.Structures.Tower;

public abstract class Unit {
    protected int id;
    protected String type;
    protected int playerId;
    protected int health;
    protected int damage;
    protected int range;
    protected int foodCost;
    protected int goldCost;
    protected int size;
    protected int row, col;

    public Unit(int id, String type, int playerId,
                int health, int damage, int range,
                int foodCost, int goldCost, int size,
                int row, int col) {
        this.id = id;
        this.type = type;
        this.playerId = playerId;
        this.health = health;
        this.damage = damage;
        this.range = range;
        this.foodCost = foodCost;
        this.goldCost = goldCost;
        this.size = size;
        this.row = row;
        this.col = col;
    }

    public abstract void specialAbility();

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int dmg) {
        this.health -= dmg;
        if (health < 0) health = 0;
    }

    public boolean canMoveTo(Block targetBlock) {
        if (!targetBlock.isWalkable()) return false;
        if (targetBlock.hasUnit()) return false;
        return true;
    }

    // حالا اینجا currentBlock هم میدیم که یونیت رو از بلاک قبلی حذف کنه
    public boolean moveTo(Block targetBlock, Block currentBlock) {


        if (canMoveTo(targetBlock)) {
            if (currentBlock != null) {
                currentBlock.removeUnit();
            }
            targetBlock.setUnit(this);
            this.row = targetBlock.getRow();
            this.col = targetBlock.getCol();
            return true;
        }
        return false;
    }

    public boolean attack(Unit targetUnit, Block fromBlock, Block toBlock) {
        if (!this.isAlive()) return false;

        int dist = Math.abs(this.row - targetUnit.getRow()) + Math.abs(this.col - targetUnit.getCol());
        if (dist > this.range) return false;

        // شروع با دمیج پایه
        double finalDamage = this.damage;

        // ✅ اگر از Forest حمله می‌کنیم
        if (fromBlock instanceof ForestBlock) {
            finalDamage *= ((ForestBlock) fromBlock).getAttackBonus();
        }

        // ✅ اگر هدف داخل Forest هست و داره دفاع می‌کنه
        if (toBlock instanceof ForestBlock) {
            finalDamage *= ((ForestBlock) toBlock).getDefenseBonus();
        }

        // نهایی کردن و اعمال دمیج
        int actualDamage = (int) Math.round(finalDamage);
        targetUnit.takeDamage(actualDamage);

        System.out.println(this.type + " attacked " + targetUnit.getType() +
                " → Damage dealt: " + actualDamage);

        return true;
    }


    public void interactWithBlock(Block block) {
        // پیش‌فرض کاری انجام نمی‌دهد، می‌توانید بعداً بسط دهید
    }
    public int getRank() {
        switch (type) {
            case "Peasant": return 1;
            case "Spearman": return 2;
            case "Swordsman": return 3;
            case "Knight": return 4;
            default: return 0;
        }
    }


    // Getters و setters...
    public int getId() { return id; }
    public String getType() { return type; }
    public int getPlayerId() { return playerId; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getDamage() { return damage; }
    public int getRange() { return range; }
    public int getFoodCost() { return foodCost; }
    public int getGoldCost() { return goldCost; }
    public int getSize() { return size; }
    public int getRow() { return row; }
    public int getCol() { return col; }



    public void setRow(int row) { this.row = row; }
    public void setCol(int col) { this.col = col; }

    @Override
    public String toString() {
        return type + " (ID: " + id + ", Player: " + playerId + ", HP: " + health + ")";
    }
}
