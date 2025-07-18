package org.example.model.Units;

public class Swordsman extends Unit {
    public Swordsman(int id, int playerId, int row, int col) {
        super(id, "Swordsman", playerId, 60, 15, 1, 2, 30, 2, row, col);
    }

    @Override
    public void specialAbility() {
        System.out.println("Swordsman performs a powerful strike!");
    }
}
