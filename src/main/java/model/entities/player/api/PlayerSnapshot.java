package model.entities.player.api;

public final class PlayerSnapshot {
    public final double x;
    public final double y;
    public final double width;
    public final double height;
    public final boolean facingRight;
    public final boolean onGround;

    public PlayerSnapshot(
        final double x,
        final double y,
        final double width,
        final double height,
        final boolean facingRight,
        final boolean onGround
    ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.facingRight = facingRight;
        this.onGround = onGround;
    }
}
