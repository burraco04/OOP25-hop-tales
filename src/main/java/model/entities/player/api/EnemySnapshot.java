package model.entities.player.api;

public final class EnemySnapshot {

    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final EnemyType type;
    private final boolean alive;

    public EnemySnapshot(
            final double x,
            final double y,
            final double width,
            final double height,
            final EnemyType type,
            final boolean alive
    ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.alive = alive;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public EnemyType getType() {
        return type;
    }

    public boolean isAlive() {
        return alive;
    }
}
