package org.example.model;

import org.example.model.Structures.Barrack;
import org.example.model.Structures.TownHall;
import org.example.model.Units.Unit;
import org.example.model.Structures.Structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private int id;
    private String name;
    private int gold;
    private int food;
    private int usedUnitSpace = 0;

    private List<Unit> units;
    private List<Structure> structures;

    public Player(int id, String name, int gold, int food) {
        this.id = id;
        this.name = name;
        this.gold = gold;
        this.food = food;
        this.units = new ArrayList<>();
        this.structures = new ArrayList<>();
    }
    private Map<String, Integer> structureCount = new HashMap<>();

    public int getStructureCount(String type) {
        return structureCount.getOrDefault(type, 0);
    }

    public void incrementStructureCount(String type) {
        structureCount.put(type, getStructureCount(type) + 1);
    }


    public int getId() { return id; }
    public String getName() { return name; }
    public int getGold() { return gold; }
    public int getFood() { return food; }
    public List<Unit> getUnits() { return units; }
    public List<Structure> getStructures() { return structures; }

    public void addGold(int amount) {
        gold += amount;
    }

    public boolean canAfford(int goldCost) {
        return this.gold >= goldCost ;
    }

    public boolean payResources(int goldCost) {
        if (canAfford(goldCost)) {
            this.gold -= goldCost;
            return true;
        }
        return false;
    }


    public int getUsedUnitSpace() {
        return usedUnitSpace;
    }

    public void addToUsedUnitSpace(int amount) {
        usedUnitSpace += amount;
    }

    public void removeFromUsedUnitSpace(int amount) {
        usedUnitSpace -= amount;
        if (usedUnitSpace < 0) usedUnitSpace = 0;
    }


    // اگر لیست یونیت‌ها رو داشته باشیم:
    public Unit findUnitById(int unitId) {
        for (Unit u : units) {
            if (u.getId() == unitId)
                return u;
        }
        return null;
    }


    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public void addFood(int amount) {
        food += amount;
    }

    public boolean spendFood(int amount) {
        if (food >= amount) {
            food -= amount;
            return true;
        }
        return false;
    }
    public int getTotalUnitSpace() {
        int total = 0;

        for (Structure s : structures) { // ← این باید لیست ساختمان‌های خودت باشه
            if (s instanceof TownHall) {
                total += 5; // فرض: TownHall همیشه ۵
            } else if (s instanceof Barrack) {
                total += ((Barrack) s).getUnitSpace();
            }
        }

        return total;
    }


    public boolean canSpendGold(int amount) {
        return gold >= amount;
    }

    public boolean canSpendFood(int amount) {
        return food >= amount;
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public void addStructure(Structure structure) {
        structures.add(structure);
    }

    public void removeStructure(Structure structure) {
        structures.remove(structure);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gold=" + gold +
                ", food=" + food +
                ", units=" + units.size() +
                ", structures=" + structures.size() +
                '}';
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
    public void setFood(int food) {
        this.food = food;
    }
}
