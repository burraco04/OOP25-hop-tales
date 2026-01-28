package model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import model.objects.api.WorldObject;
import model.objects.impl.brick.Brick;
import model.objects.impl.collectable.Powerup;

public final class Collider {
    private final Set<World.TileKey> solidTiles;
    private final Set<World.TileKey> collectableTiles;
    private final Map<World.TileKey, WorldObject> collectableMap;
    private final Set<World.TileKey> powerupBlockTiles;
    private final List<WorldObject> entities;

    Collider(
        final Set<World.TileKey> solidTiles,
        final Set<World.TileKey> collectableTiles,
        final Map<World.TileKey, WorldObject> collectableMap,
        final Set<World.TileKey> powerupBlockTiles,
        final List<WorldObject> entities
    ) {
        this.solidTiles = solidTiles;
        this.collectableTiles = collectableTiles;
        this.collectableMap = collectableMap;
        this.powerupBlockTiles = powerupBlockTiles;
        this.entities = entities;
    }

    public boolean collidesWithSolid(final int x, final int y) {
        for (int dx = 0; dx < GameConstants.PLAYER_WIDTH_TILES; dx++) {
            for (int dy = 0; dy < GameConstants.PLAYER_HEIGHT_TILES; dy++) {
                if (solidTiles.contains(new World.TileKey(x + dx, y + dy))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean collidesWithCollectable(final int x, final int y) {
        for (int dx = 0; dx < GameConstants.PLAYER_WIDTH_TILES; dx++) {
            for (int dy = 0; dy < GameConstants.PLAYER_HEIGHT_TILES; dy++) {
                final World.TileKey key = new World.TileKey(x + dx, y + dy);
                if (collectableTiles.contains(key)) {
                    collectableTiles.remove(key);
                    entities.remove(collectableMap.get(key));
                    collectableMap.remove(key, collectableMap.get(key));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean collidesWithPowerupBlockFromBelow(final int x, final int y) {
        if (y <= 0) {
            return false;
        }
        final int checkY = y - 1;
        for (int dx = 0; dx < GameConstants.PLAYER_WIDTH_TILES; dx++) {
            final int blockX = x + dx;
            final World.TileKey blockKey = new World.TileKey(blockX, checkY);
            if (powerupBlockTiles.contains(blockKey)) {
                powerupBlockTiles.remove(blockKey);
                replaceEntityAt(blockX, checkY, World.POWERUP_BLOCK_TYPE, new Brick(blockX, checkY));
                spawnPowerupAbove(blockX, checkY);
                return true;
            }
        }
        return false;
    }

    private void spawnPowerupAbove(final int blockX, final int blockY) {
        final int powerupY = blockY - 1;
        if (powerupY < 0) {
            return;
        }
        final World.TileKey powerupKey = new World.TileKey(blockX, powerupY);
        if (solidTiles.contains(powerupKey) || collectableTiles.contains(powerupKey)) {
            return;
        }
        final WorldObject powerup = new Powerup(blockX, powerupY);
        entities.add(powerup);
        collectableTiles.add(powerupKey);
        collectableMap.put(powerupKey, powerup);
    }

    private void replaceEntityAt(final int x, final int y, final String type, final WorldObject replacement) {
        for (int i = 0; i < entities.size(); i++) {
            final WorldObject obj = entities.get(i);
            if (type.equals(obj.getType()) && obj.getX() == x && obj.getY() == y) {
                entities.set(i, replacement);
                return;
            }
        }
        entities.add(replacement);
    }
}
