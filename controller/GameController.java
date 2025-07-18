package controller;

import org.example.database.StructureDAO;
import org.example.database.StructureFactory;
import org.example.database.UnitDAO;
import org.example.database.UnitFactory;
import org.example.model.Blocks.EmptyBlock;
import org.example.model.Blocks.ForestBlock;
import org.example.model.Player;
import org.example.model.Blocks.Block;
import org.example.model.Structures.*;
import org.example.model.Units.Knight;
import org.example.model.Units.*;
import org.example.model.GameMap;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.database.StructureDAO.getLastUnitIdFromDatabase;

public class GameController {
    private List<Player> players;
    private int currentPlayerIndex;
    private GameMap gameMap;
    private boolean gameOver;
    private static final Map<String, Integer> MAX_STRUCTURE_COUNT = Map.of(
            "Farm", 3,
            "Market", 3,
            "Barrack", 2,
            "Tower", 2
    );


    public int generateUniqueUnitId() {
            return UnitDAO.getLastUnitIdFromDatabase() + 1;
    }
    public int generateUniqueStructureId() {
        return StructureDAO.getLastUnitIdFromDatabase() + 1;
    }


    public GameController(List<Player> players, GameMap gameMap) {
        this.players = players;
        this.gameMap = gameMap;
        this.currentPlayerIndex = 0;
        this.gameOver = false;
        initializePlayersAndStartGame();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        generateResources(getCurrentPlayer());
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        System.out.println("Turn changed to player: " + getCurrentPlayer().getName());
    }
    private void generateResources(Player player) {
        int gold = 0;
        int food = 0;

        for (int r = 0; r < gameMap.getRows(); r++) {
            for (int c = 0; c < gameMap.getCols(); c++) {
                Block block = gameMap.getBlock(r, c);
                for(Unit u : player.getUnits()) {
                    if(u instanceof Peasant){
                        food += 2;
                    }
                }
                if(block instanceof ForestBlock && (player.getId() == block.getOwnerId())){
                    food +=  ((ForestBlock) block).getFoodProduction();
                }
                if (block.hasStructure()) {
                    Structure s = block.getStructure();
                    if (s.getId() == player.getId()) {
                        if (s instanceof Farm) {
                            food += ((Farm) s).getFoodProduction();
                        } else if (s instanceof Market) {
                            gold += ((Market) s).getGoldProduction() ;
                        }else if (s instanceof TownHall) {
                            gold += ((TownHall) s).getGoldProduction();
                            food += ((TownHall) s).getFoodProduction();
                        }
                    }
                }
            }
        }

        player.addGold(gold);
        player.addFood(food);

        System.out.println("⚒ منابع تولیدشده برای " + player.getName() +
                ": + " + food + " غذا، + " + gold + " طلا");
    }
    public void upgradeStructure(int row, int col) {
        Block block = gameMap.getBlock(row, col);
        if (block == null || !block.hasStructure()) {
            System.out.println("❌ No structure found at (" + row + "," + col + ")");
            return;
        }

        Structure structure = block.getStructure();
        Player player = getCurrentPlayer();

        if (structure.getPlayerId() != player.getId()) {
            System.out.println("❌ This structure does not belong to you.");
            return;
        }

        if (!structure.canUpgrade()) {
            System.out.println("❌ Structure cannot be upgraded further.");
            return;
        }

        int cost = 0;
        if (structure instanceof Barrack) {
            cost = ((Barrack) structure).getUpgradeCost();
        } else if (structure instanceof Farm) {
            cost = ((Farm) structure).getUpgradeCost();
        } else if (structure instanceof Market) {
            cost = ((Market) structure).getUpgradeCost();
        } else if (structure instanceof Tower) {
            cost = ((Tower) structure).getUpgradeCost();
        } else {
            System.out.println("⚠️ This structure type has no upgrade cost defined.");
            return;
        }

        if (player.getGold() < cost) {
            System.out.println("❌ Not enough gold to upgrade. Required: " + cost + ", You have: " + player.getGold());
            return;
        }

        player.setGold(player.getGold() - cost);
        structure.upgrade();
        System.out.println("✅ Structure upgraded at (" + row + "," + col + ")");
    }



    // اجرای دستور کلی (move, attack, build و ...)
    public void handleCommand(String command) {

            String[] parts = command.trim().split("\\s+");
            if (parts.length == 0) return;

            switch (parts[0].toLowerCase()) {
                case "move" -> handleMoveCommand(parts);
                case "attack" -> handleAttackCommand(parts);
                case "build" -> handleBuildCommand(parts);
                default -> System.out.println("Unknown command!");
            }
        System.out.println("Received command: " + command);
        }
    public Unit findUnitById(int unitId) {
        for (Player player : players) { // اگر دسترسی به لیست پلیرها داری
            Unit unit = player.findUnitById(unitId);
            if (unit != null) {
                return unit;
                }
            }
            return null;
        }





    public void handleMoveCommand(String[] parts) {
        if (parts.length != 4) {
            System.out.println("Usage: move <unitId> <row> <col>");
            return;
        }

        try {
            int unitId = Integer.parseInt(parts[1]);
            int targetRow = Integer.parseInt(parts[2]);
            int targetCol = Integer.parseInt(parts[3]);
            Unit unit = getCurrentPlayer().findUnitById(unitId);
            if (unit == null) {
                System.out.println("Unit not found!");
                return;
            }
            Block currentBlock = gameMap.getBlock(unit.getRow(), unit.getCol());
            Block targetBlock = gameMap.getBlock(targetRow, targetCol);
            if (currentBlock == null || targetBlock == null) return;

            Unit fromUnit = currentBlock.getUnit();
            Unit toUnit = targetBlock.getUnit();

            if (fromUnit == null) return;

            // 🔁 MERGE اگر یونیت خودی در مقصد وجود دارد
            if (toUnit != null && fromUnit.getPlayerId() == toUnit.getPlayerId()) {
                Unit merged = mergeUnits(fromUnit, toUnit);
                if (merged != null) {
                    currentBlock.removeUnit();
                    targetBlock.setUnit(merged);
                    System.out.println("✅ Units merged into: " + merged.getType());
                    return;
                }
            }
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;

                    int checkRow = targetBlock.getRow() + dr;
                    int checkCol = targetBlock.getCol() + dc;

                    Block neighbor = gameMap.getBlock(checkRow, checkCol);
                    if (neighbor != null && neighbor.hasStructure()) {
                        Structure s = neighbor.getStructure();
                        if (s instanceof Tower && s.getPlayerId() != fromUnit.getPlayerId()) {
                            Tower tower = (Tower) s;
                            if (tower.blocks(fromUnit)) {
                                System.out.println("🚫 Blocked by enemy Tower at (" + checkRow + "," + checkCol + ")");
                                return;
                            }
                        }
                    }
                }
            }
            if (unit.moveTo(targetBlock,currentBlock)) {
                System.out.println("Unit moved successfully.");
            } else {
                System.out.println("Move failed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
    public void handleAttackCommand(String[] parts) {
        if (parts.length != 4) {
            System.out.println("Usage: attack <attackerId> <targetId>");
            return;
        }

        try {
            int fromRow = Integer.parseInt(parts[0]);
            int fromCol = Integer.parseInt(parts[1]);
            int toRow = Integer.parseInt(parts[2]);
            int toCol = Integer.parseInt(parts[3]);

            Block fromBlock = gameMap.getBlock(fromRow, fromCol);
            Block toBlock = gameMap.getBlock(toRow, toCol);

            if (fromBlock == null || toBlock == null) return;

            Unit attacker = fromBlock.getUnit();
            Unit defender = toBlock.getUnit();

            if (attacker == null || defender == null) return;
            if (attacker.getPlayerId() == defender.getPlayerId()) return;

            boolean result = attacker.attack(defender, fromBlock, toBlock);

            if (result) {
                System.out.println("✅ Attack successful!");
                if (!defender.isAlive()) {
                    toBlock.removeUnit();
                    System.out.println("☠️ Defender defeated and removed from battlefield.");
                }
            } else {
                System.out.println("❌ Attack failed.");
            }
            Structure structure = toBlock.getStructure();
            if (structure instanceof TownHall && structure.getHealth() <= 0) {
                checkGameOver();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
    public void endGame() {
        JOptionPane.showMessageDialog(null, "🏆 Player " + getCurrentPlayer().getName() + " wins the game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleBuildCommand(String[] parts) {
        if (parts.length != 4) {
            System.out.println("Usage: build <type> <row> <col>");
            return;
        }

        String structureType = parts[1];
        int row = Integer.parseInt(parts[2]);
        int col = Integer.parseInt(parts[3]);

        Block block = gameMap.getBlock(row, col);

        if (!block.isBuildable() || block.hasStructure()) {
            System.out.println("Can't build here.");
            return;
        }

        Structure structure = StructureFactory.firstcreateStructure(structureType, getCurrentPlayer(), row, col);
        if (structure == null) {
            System.out.println("Invalid structure type.");
            return;
        }

        if (!getCurrentPlayer().canAfford(structure.getMaintenanceCost())) {
            System.out.println("Not enough resources.");
            return;
        }
        int current = getCurrentPlayer().getStructureCount(structureType);
        int limit = MAX_STRUCTURE_COUNT.getOrDefault(structureType, Integer.MAX_VALUE);

        if (current >= limit) {
            System.out.println("❌ You can't build more " + structureType + "s.");
            return;
        }


        block.setStructure(structure);
        getCurrentPlayer().addStructure(structure);
        getCurrentPlayer().payResources(structure.getMaintenanceCost());
        System.out.println(structureType + " built successfully.");
        getCurrentPlayer().incrementStructureCount(structureType);

    }


    public boolean isGameOver() {
        return gameOver;
    }

    // چک کردن شرایط پایان بازی (مثلا نابودی TownHall)
    public void checkGameOver() {
        for (Player player : players) {
            boolean hasTownHall = playerHasTownHall(player);
            if (!hasTownHall) {
                gameOver = true;
                endGame();
                break;
            }
        }
    }



    private boolean playerHasTownHall(Player player) {
        // بررسی اینکه بازیکن حداقل یک TownHall دارد یا نه
        for (Structure structure : player.getStructures()) {
            if (structure.getType().equals("TownHall") && !structure.isDestroyed()) {
                return true;
            }
        }
        return false;
    }
    public GameMap getGameMap() {
        return gameMap;
    }
    public Unit mergeUnits(Unit u1, Unit u2) {
        if (u1 == null || u2 == null) return null;
        if (!u1.getType().equals(u2.getType())) return null;
        if (u1.getPlayerId() != u2.getPlayerId()) return null;

        String newType = null;

        switch (u1.getType()) {
            case "Peasant":
                newType = "Spearman";
                break;
            case "Spearman":
                newType = "Swordsman";
                break;
            case "Swordsman":
                newType = "Knight";
                break;
            default:
                System.out.println("❌ Cannot merge unit type: " + u1.getType());
                return null;
        }

        // آی‌دی جدید باید یونیک باشه؛ فرض می‌گیریم یه متد یا شمارنده داریم
        int newId = generateUniqueUnitId();
        int playerId = u1.getPlayerId();
        int row = u1.getRow();
        int col = u1.getCol();

        switch (newType) {
            case "Spearman":
                return new Spearman(newId, playerId, row, col);
            case "Swordsman":
                return new Swordsman(newId, playerId, row, col);
            case "Knight":
                return new Knight(newId, playerId, row, col);
            default:
                return null;
        }
    }
    public void produceUnit(String unitType, int structureRow, int structureCol) {
        Block structureBlock = gameMap.getBlock(structureRow, structureCol);
        if (structureBlock == null || !structureBlock.hasStructure()) {
            System.out.println("❌ No structure at given location.");
            return;
        }

        Structure s = structureBlock.getStructure();
        Player player = getCurrentPlayer();

        if (s.getPlayerId() != player.getId()) {
            System.out.println("❌ Not your structure.");
            return;
        }

        // بررسی منابع بازیکن
        Unit temp;
        switch (unitType) {
            case "Peasant":
                temp = new Peasant(-1, player.getId(), 0, 0);
                break;
            case "Spearman":
                temp = new Spearman(-1, player.getId(), 0, 0);
                break;
            case "Swordsman":
                temp = new Swordsman(-1, player.getId(), 0, 0);
                break;
            case "Knight":
                temp = new Knight(-1, player.getId(), 0, 0);
                break;
            default:
                System.out.println("❌ Unknown unit type");
                return;
        }

        if (player.getGold() < temp.getGoldCost() || player.getFood() < temp.getFoodCost()) {
            System.out.println("❌ Not enough resources.");
            return;
        }
        if (player.getUsedUnitSpace() + temp.getSize() > player.getTotalUnitSpace()) {
            System.out.println("❌ Not enough unit space.");
            return;
        }

        // پیدا کردن یک بلاک خالی اطراف ساختمان
        int[][] directions = {{0,1},{1,0},{0,-1},{-1,0}};
        for (int[] d : directions) {
            int r = structureRow + d[0];
            int c = structureCol + d[1];
            Block b = gameMap.getBlock(r, c);

            if (b != null && b.isWalkable() && !b.hasUnit()) {
                int id = generateUniqueUnitId();
                Unit newUnit = UnitFactory.createInitialUnit(unitType, id, player.getId(), r, c);

                b.setUnit(newUnit);
                UnitDAO.insertUnit(newUnit);
                player.setGold(player.getGold() - temp.getGoldCost());
                player.setFood(player.getFood() - temp.getFoodCost());

                System.out.println("✅ Produced unit: " + unitType + " at (" + r + "," + c + ")");
                return;
            }
        }

        System.out.println("❌ No adjacent free space to place unit.");
    }
    public void initializePlayersAndStartGame() {
        int mapRows = gameMap.getRows();
        int mapCols = gameMap.getCols();

        // مختصات اولیه برای حداکثر ۴ بازیکن (گوشه‌های نقشه)
        int[][] startingPositions = {
                {1, 1},
                {mapRows - 2, mapCols - 2},
                {1, mapCols - 2},
                {mapRows - 2, 1}
        };

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            int row = startingPositions[i][0];
            int col = startingPositions[i][1];
            Block startBlock = gameMap.getBlock(row, col);

            if (!(startBlock instanceof EmptyBlock)) {
            startBlock = new EmptyBlock(row, col);
            gameMap.setBlock(row, col, startBlock);
            }


            int newId = player.getId();
            TownHall townHall = new TownHall(newId, player.getId(), row, col);

            gameMap.placeStructure(row, col, townHall);
            player.addStructure(townHall);

            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int r = row + dr;
                    int c = col + dc;
                    Block b = gameMap.getBlock(r, c);
                    if (b != null) {
                        b.setOwnerId(player.getId());
                    }
                }
            }
        }
    }
    public List getPlayers() {
        return  players;
    }
}
