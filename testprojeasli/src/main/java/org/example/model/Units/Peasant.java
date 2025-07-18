package org.example.model.Units;

public class Peasant extends Unit {
    public Peasant(int id, int playerId, int row, int col) {
        super(id, "Peasant", playerId, 40, 5, 1, 1, 10, 1, row, col);
    }

    @Override
    public void specialAbility() {
        System.out.println("Peasant gathers resources efficiently!");
    }
}
