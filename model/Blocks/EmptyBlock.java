package model.Blocks;

public class EmptyBlock extends Block {
    private int goldProduction;

    public EmptyBlock(int row, int col) {
        super(row, col, BlockType.EMPTY);
        this.goldProduction = 3;  // مقدار فرضی تولید گلد
    }

    @Override
    public boolean isBuildable() {
        // روی EmptyBlock ساخت‌وساز امکان‌پذیر است
        return true;
    }

    @Override
    public boolean isWalkable() {
        // فرض می‌کنیم خالی است و قابلیت عبور دارد
        return true;
    }

    public int getGoldProduction() {
        return goldProduction;
    }
}
