package org.example.model.Blocks;

public class VoidBlock extends Block {
    public VoidBlock(int row, int col) {
        super(row, col, BlockType.VOID);
    }

    @Override
    public boolean isBuildable() {
        // روی این بلاک نمی‌شود ساخت
        return false;
    }

    @Override
    public boolean isWalkable() {
        // این بلاک قابل عبور نیست
        return false;
    }
}
