package model.entities.player.impl;

import model.Model;
import model.entities.player.api.Player;
import model.entities.player.api.PlayerSnapshot;

import java.util.List;
import java.util.Objects;

public final class PlayerImpl implements Player {
    private static final double GRAVITY = 2000.0;
    private static final double MOVE_ACCELERATION = 1200.0;
    private static final double MAX_RUN_SPEED = 350.0;
    private static final double FRICTION = 1400.0;
    private static final double JUMP_SPEED = 750.0;
    private static final double MAX_FALL_SPEED = 1800.0;

    private final List<Model.Platform> platforms;
    private final double worldWidth;
    private final double worldHeight;
    private final double width;
    private final double height;

    private double x;
    private double y;
    private double vx;
    private double vy;
    private boolean onGround;
    private boolean facingRight = true;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean jumpQueued;

    public PlayerImpl(
        final double startX,
        final double startY,
        final double width,
        final double height,
        final double worldWidth,
        final double worldHeight,
        final List<Model.Platform> platforms
    ) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.platforms = Objects.requireNonNull(platforms);
    }

    @Override
    public void setLeftPressed(final boolean pressed) {
        this.leftPressed = pressed;
    }

    @Override
    public void setRightPressed(final boolean pressed) {
        this.rightPressed = pressed;
    }

    @Override
    public void queueJump() {
        this.jumpQueued = true;
    }

    @Override
    public void update(final double deltaSeconds) {
        if (deltaSeconds <= 0.0) {
            return;
        }
        applyHorizontalMovement(deltaSeconds);
        applyVerticalMovement(deltaSeconds);
    }

    private void applyHorizontalMovement(final double deltaSeconds) {
        double acceleration = 0.0;
        if (leftPressed && !rightPressed) {
            acceleration = -MOVE_ACCELERATION;
            facingRight = false;
        } else if (rightPressed && !leftPressed) {
            acceleration = MOVE_ACCELERATION;
            facingRight = true;
        }

        if (acceleration == 0.0) {
            final double frictionEffect = FRICTION * deltaSeconds;
            if (vx > 0.0) {
                vx = Math.max(0.0, vx - frictionEffect);
            } else if (vx < 0.0) {
                vx = Math.min(0.0, vx + frictionEffect);
            }
        } else {
            vx += acceleration * deltaSeconds;
            if (Math.abs(vx) > MAX_RUN_SPEED) {
                vx = Math.signum(vx) * MAX_RUN_SPEED;
            }
        }

        double newX = x + vx * deltaSeconds;
        final double minX = 0.0;
        final double maxX = worldWidth - width;

        for (final Model.Platform platform : platforms) {
            if (!overlapsVertically(y, height, platform)) {
                continue;
            }
            if (!intersects(newX, y, width, height, platform)) {
                continue;
            }
            if (vx > 0.0) {
                newX = platform.x - width;
            } else if (vx < 0.0) {
                newX = platform.x + platform.width;
            }
            vx = 0.0;
        }

        x = clamp(newX, minX, maxX);
    }

    private void applyVerticalMovement(final double deltaSeconds) {
        if (jumpQueued) {
            if (onGround) {
                vy = -JUMP_SPEED;
                onGround = false;
            }
            jumpQueued = false;
        }

        vy += GRAVITY * deltaSeconds;
        if (vy > MAX_FALL_SPEED) {
            vy = MAX_FALL_SPEED;
        }

        double newY = y + vy * deltaSeconds;
        boolean grounded = false;

        for (final Model.Platform platform : platforms) {
            if (!intersects(x, newY, width, height, platform)) {
                continue;
            }
            if (vy > 0.0) {
                newY = platform.y - height;
                grounded = true;
            } else if (vy < 0.0) {
                newY = platform.y + platform.height;
            }
            vy = 0.0;
        }

        if (newY + height >= worldHeight) {
            newY = worldHeight - height;
            grounded = true;
            vy = 0.0;
        } else if (newY <= 0.0) {
            newY = 0.0;
            if (vy < 0.0) {
                vy = 0.0;
            }
        }

        y = newY;
        onGround = grounded;
    }

    @Override
    public PlayerSnapshot snapshot() {
        return new PlayerSnapshot(x, y, width, height, facingRight, onGround);
    }

    private static double clamp(final double value, final double min, final double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static boolean overlapsVertically(final double y, final double height, final Model.Platform platform) {
        return y < platform.y + platform.height && y + height > platform.y;
    }

    private static boolean intersects(
        final double x,
        final double y,
        final double width,
        final double height,
        final Model.Platform platform
    ) {
        return x < platform.x + platform.width
            && x + width > platform.x
            && y < platform.y + platform.height
            && y + height > platform.y;
    }
}
