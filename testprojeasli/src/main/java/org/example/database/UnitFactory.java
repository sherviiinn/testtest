package org.example.database;

import org.example.model.Units.*;

public class UnitFactory {
    private static int globalUnitID = 0;

    public static Unit createUnit(int id, String type,  int playerId, int health , int row , int col) {
        Unit unit = null;

        switch (type) {
            case "Spearman" -> unit = new Spearman(id, playerId, row, col);
            case "Swordsman" -> unit = new Swordsman(id, playerId, row, col);
            case "Knight" -> unit = new Knight(id, playerId, row, col);
            case "Peasant" -> unit = new Peasant(id, playerId, row, col);
            // اینجا انواع واحدهای دیگه رو اضافه کن
            default -> throw new IllegalArgumentException("نوع یونیت نامعتبر: " + type);
        }
        if (unit != null) {
            unit.setHealth(health);

        }

        return unit;
    }

    // یک متد ساده برای ساخت اولین واحد (با مقادیر پیش فرض)
    public static Unit createInitialUnit(String type, int playerId, int id, int row, int col) {
        int health;
        // مقداردهی سلامت پیش‌فرض بر اساس نوع سازه
        switch (type) {
            case "Knight" -> health = 80;
            case "Peasant" -> health = 40;
            case "Swordsman" -> health = 60;
            case "Spearman" -> health = 50;
            default -> throw new IllegalArgumentException("نوع سازه نامعتبر: " + type);
        }
        return createUnit( id,type, playerId,health,row, col);
    }
}
