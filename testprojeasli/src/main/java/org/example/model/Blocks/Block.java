package org.example.model.Blocks;

import org.example.model.Units.Unit;
import org.example.model.Structures.Structure;

import java.awt.*;

public abstract class Block {
    protected final int row, col;
    protected final BlockType type;

    protected Unit unit;
    protected Structure structure;
    private int ownerId = -1;

    public Block(int row, int col, BlockType type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }
    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public BlockType getType() { return type; }

    public boolean hasUnit() { return unit != null; }
    public boolean hasStructure() { return structure != null; }

    public Unit getUnit() { return unit; }
    public Structure getStructure() { return structure; }

    public void setUnit(Unit unit) { this.unit = unit; }
    public void removeUnit() { this.unit = null; }

    public void setStructure(Structure structure) { this.structure = structure; }
    public void removeStructure() { this.structure = null; }

    // آیا میشه ساخت‌وساز انجام داد؟
    public abstract boolean isBuildable();

    // آیا میشه روی این بلاک حرکت کرد؟
    public abstract boolean isWalkable();

    // تعامل واحد با بلاک (مثلا ورود، خروج، یا برخورد)
    public void interactWithUnit(Unit unit) {
        // پیش‌فرض: اگر بلاک خالی است، واحد وارد می‌شود
        if (!hasUnit()) {
            setUnit(unit);
        } else {
            // اگر واحد دیگری هست، می‌توانی اینجا منطق جنگ یا تعامل رو بنویسی
            System.out.println("Block at (" + row + "," + col + ") is occupied.");
        }
    }
    public Color getColor() {
        return switch (type) {
            case VOID -> Color.DARK_GRAY;
            case EMPTY -> Color.LIGHT_GRAY;
            case FOREST -> new Color(34, 139, 34);
        };
    }

}
