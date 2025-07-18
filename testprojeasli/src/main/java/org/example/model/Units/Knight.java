package org.example.model.Units;

public class Knight extends Unit {
    public Knight(int id, int playerId, int row, int col) {
        super(id, "Knight", playerId, 80, 20, 1, 3, 50, 2, row, col);
    }

    @Override
    public void specialAbility() {
        System.out.println("Knight charges powerfully!");
    }
}