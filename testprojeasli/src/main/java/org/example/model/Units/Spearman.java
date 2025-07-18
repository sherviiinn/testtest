package org.example.model.Units;

public class Spearman extends Unit {
    public Spearman(int id, int playerId, int row, int col) {
        super(id, "Spearman", playerId, 50, 12, 2, 2, 25, 2, row, col);
    }

    @Override
    public void specialAbility() {
        System.out.println("Spearman performs a piercing thrust!");
    }
}