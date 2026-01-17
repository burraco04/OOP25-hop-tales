package model.entities.player.impl;

import model.entities.player.api.Enemy;
import model.entities.player.api.EnemySnapshot;
import model.entities.player.api.EnemyType;

public final class EnemyImpl implements Enemy{

    private static final double DEFAULT_SPEED = 100.0;

    private final double width;
    private final double height;
    private final EnemyType type;

    private double x;
    private double y;
    private boolean alive = true;
    private double vx = DEFAULT_SPEED;


    public EnemyImpl(double x, double y, double width, double height, EnemyType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }


    @Override
    public void update(double deltaSecond) {
        if (!alive) {
            return;
        }
        x += vx * deltaSecond;
    }

    @Override
    public double getX() { 
        return x; 
    }

    @Override
    public double getY() { 
        return y; 
    }

    @Override
    public double getWidth() { 
        return width; 
    }

    @Override
    public double getHeight() { 
        return height; 
    }

    @Override
    public boolean isAlive() { 
        return alive; 
    }

    @Override
    public EnemySnapshot getSnapshot() {
        return new EnemySnapshot(x, y, width, height, type, alive);
    }


    
}
