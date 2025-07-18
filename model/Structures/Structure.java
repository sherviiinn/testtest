package model.Structures;

/**
 * کلاس پایه برای سازه‌های بازی
 */
public abstract class Structure {
    protected int id;
    protected String type;
    protected int playerId;
    protected int row, col;
    protected int health;
    protected int level;
    protected int maintenanceCost;

    public Structure(int id, String type, int playerId, int row, int col,
                     int health, int level, int maintenanceCost) {
        this.id = id;
        this.type = type;
        this.playerId = playerId;
        this.row = row;
        this.col = col;
        this.health = health;
        this.level = level;
        this.maintenanceCost = maintenanceCost;
    }

    public abstract void upgrade();

    public abstract boolean canUpgrade();

    public void takeDamage(int dmg) {
        this.health -= dmg;
        if (this.health < 0) this.health = 0;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    // Getter and setter methods

    public int getId() { return id; }
    public String getType() { return type; }
    public int getPlayerId() { return playerId; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getMaintenanceCost() { return maintenanceCost; }

    @Override
    public String toString() {
        return type + " (ID: " + id + ", Player: " + playerId + ", HP: " + health + ", Level: " + level + ")";
    }
}
