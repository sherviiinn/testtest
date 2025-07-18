package model;

import org.example.model.Blocks.*;
import org.example.model.Structures.Structure;
import org.example.model.Units.Unit;

import java.util.Random;

public class GameMap {
    private final int rows;
    private final int cols;
    private final Block[][] map;

    public GameMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        map = new Block[rows][cols];
        initializeMap();
    }

    private void initializeMap() {
        Random rand = new Random();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double chance = rand.nextDouble(); // بین 0.0 تا 1.0

                if (chance < 0.1) {
                    map[r][c] = new VoidBlock(r, c);         // 10%
                } else if (chance < 0.3) {
                    map[r][c] = new ForestBlock(r, c);       // 20%
                } else {
                    map[r][c] = new EmptyBlock(r, c);        // 70%
                }
            }
        }
    }


    public Block getBlock(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return null;
        return map[row][col];
    }
    public void setBlock(int row, int col, Block block) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return;
        map[row][col] = block;
    }

    public boolean placeStructure(int row, int col, Structure structure) {
        Block block = getBlock(row, col);
        if (block != null && block.isBuildable() && !block.hasStructure()) {
            block.setStructure(structure);
            return true;
        }
        return false;
    }

    public boolean placeUnit(int row, int col, Unit unit) {
        Block block = getBlock(row, col);
        if (block != null && block.isWalkable() && !block.hasUnit()) {
            block.setUnit(unit);
            return true;
        }
        return false;
    }

    public boolean moveUnit(int fromRow, int fromCol, int toRow, int toCol) {
        Block fromBlock = getBlock(fromRow, fromCol);
        Block toBlock = getBlock(toRow, toCol);

        if (fromBlock == null || toBlock == null) return false;
        if (!fromBlock.hasUnit()) return false;
        if (!toBlock.isWalkable() || toBlock.hasUnit()) return false;

        Unit unit = fromBlock.getUnit();
        fromBlock.removeUnit();
        toBlock.setUnit(unit);
        unit.setRow(toRow);
        unit.setCol(toCol);
        return true;
    }


    public int getRows() { return rows; }
    public int getCols() { return cols; }
}
