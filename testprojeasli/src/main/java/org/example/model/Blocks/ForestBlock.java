package org.example.model.Blocks;

import org.example.model.Units.Unit;
import org.example.model.Structures.Structure;

public class ForestBlock extends Block {
    public static final double ATTACK_BONUS = 1.25;
    public static final double DEFENSE_BONUS = 0.75;
    private int foodProduction;
    private boolean forestPresent;

    public ForestBlock(int row, int col) {
        super(row, col, BlockType.FOREST);
        this.foodProduction = 5;  // مقدار غذای تولیدی در هر دوره
        this.forestPresent = true;
    }

    @Override
    public boolean isBuildable() {
        // اگر جنگل نباشد می‌توان ساخت‌وساز انجام داد
        return !forestPresent;
    }

    @Override
    public boolean isWalkable() {
        // فرض می‌کنیم امکان حرکت وجود دارد ولی ممکن است محدودیت‌هایی باشد
        return true;
    }

    public int getFoodProduction() {
        if (forestPresent) {
            return foodProduction;
        }
        return 0;
    }

    public boolean hasForest() {
        return forestPresent;
    }

    public void removeForest() {
        // وقتی جنگل از بین می‌رود
        this.forestPresent = false;
        System.out.println("Forest at (" + row + ", " + col + ") has been cleared. Now buildable.");
    }


    public double getAttackBonus() {
        return forestPresent ? ATTACK_BONUS : 1.0;
    }
    public double getDefenseBonus() {
        return forestPresent ? DEFENSE_BONUS : 1.0;
    }


    public void onForestLost() {
        removeForest();

    }
}
