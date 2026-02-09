package model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.entities.api.Enemy;
import model.objects.api.WorldObject;
import model.objects.impl.collectable.Coin;
import model.objects.impl.collectable.Powerup;

/**
 * Tests for collider tile-based checks.
 */
class ColliderTest {

    @Test
    void testCollidesWithSolid() {
        final Set<Point> solids = new HashSet<>();
        solids.add(new Point(2, 3));
        final Collider collider = buildCollider(solids);

        assertTrue(collider.collidesWithSolid(2, 3, 1, 1));
        assertFalse(collider.collidesWithSolid(0, 0, 1, 1));
    }

    @Test
    void testCollidesWithHazard() {
        final Set<Point> hazards = new HashSet<>();
        hazards.add(new Point(4, 5));
        final Collider collider = buildCollider(new HashSet<>(), hazards);

        assertTrue(collider.collidesWithHazard(4, 5));
        assertFalse(collider.collidesWithHazard(1, 1));
    }

    @Test
    void testEnteringCastle() {
        final Set<Point> doors = new HashSet<>();
        doors.add(new Point(10, 10));
        final Collider collider = buildCollider(new HashSet<>(), new HashSet<>(), doors);

        assertTrue(collider.enteringCastle(10, 10, 1, 1));
        assertFalse(collider.enteringCastle(9, 9, 1, 1));
    }

    @Test
    void testCollectCoinRemovesTile() {
        final Set<Point> collectables = new HashSet<>();
        final Set<Point> coins = new HashSet<>();
        final Map<Point, WorldObject> collectableMap = new HashMap<>();
        final List<WorldObject> entities = new ArrayList<>();
        final Point coinKey = new Point(3, 3);
        final Coin coin = new Coin(3, 3);

        collectables.add(coinKey);
        coins.add(coinKey);
        collectableMap.put(coinKey, coin);
        entities.add(coin);

        final Collider collider = buildCollider(
            new HashSet<>(),
            collectables,
            coins,
            new HashSet<>(),
            collectableMap,
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>(),
            entities,
            new ArrayList<>(),
            1
        );

        assertTrue(collider.collidesWithCoin(3, 3));
        assertFalse(collectables.contains(coinKey));
        assertFalse(coins.contains(coinKey));
    }

    @Test
    void testCollectPowerupRemovesTile() {
        final Set<Point> collectables = new HashSet<>();
        final Set<Point> powerups = new HashSet<>();
        final Map<Point, WorldObject> collectableMap = new HashMap<>();
        final List<WorldObject> entities = new ArrayList<>();
        final Point powerupKey = new Point(5, 5);
        final Powerup powerup = new Powerup(5, 5);

        collectables.add(powerupKey);
        powerups.add(powerupKey);
        collectableMap.put(powerupKey, powerup);
        entities.add(powerup);

        final Collider collider = buildCollider(
            new HashSet<>(),
            collectables,
            new HashSet<>(),
            powerups,
            collectableMap,
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>(),
            entities,
            new ArrayList<>(),
            1
        );

        assertTrue(collider.collidesWithPowerup(5, 5));
        assertFalse(collectables.contains(powerupKey));
        assertFalse(powerups.contains(powerupKey));
    }

    private static Collider buildCollider(final Set<Point> solids) {
        return buildCollider(solids, new HashSet<>(), new HashSet<>());
    }

    private static Collider buildCollider(final Set<Point> solids, final Set<Point> hazards) {
        return buildCollider(solids, hazards, new HashSet<>());
    }

    private static Collider buildCollider(final Set<Point> solids, final Set<Point> hazards, final Set<Point> doors) {
        return buildCollider(
            solids,
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>(),
            new HashMap<>(),
            new HashSet<>(),
            hazards,
            doors,
            new ArrayList<>(),
            new ArrayList<>(),
            1
        );
    }

    private static Collider buildCollider(
        final Set<Point> solids,
        final Set<Point> collectables,
        final Set<Point> coins,
        final Set<Point> powerups,
        final Map<Point, WorldObject> collectableMap,
        final Set<Point> powerupBlocks,
        final Set<Point> hazards,
        final Set<Point> doors,
        final List<WorldObject> entities,
        final List<Enemy> enemies,
        final int levelId
    ) {
        return new Collider(
            solids,
            collectables,
            coins,
            powerups,
            collectableMap,
            powerupBlocks,
            hazards,
            doors,
            entities,
            enemies,
            levelId
        );
    }
}
