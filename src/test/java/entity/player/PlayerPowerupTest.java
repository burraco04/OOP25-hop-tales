package entity.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import model.GameConstants;
import model.entities.impl.PlayerImpl;

/**
 * Tests for player powerup and damage interactions.
 */
class PlayerPowerupTest {

    @Test
    void testPowerupConsumedBeforeDamage() {
        final PlayerImpl player = new PlayerImpl(
            0,
            0,
            GameConstants.PLAYER_WIDTH_TILES,
            GameConstants.PLAYER_HEIGHT_TILES
        );
        player.setPowerUp(true);
        final int healthBefore = player.getHealthPoints();

        final boolean damaged = player.applyDamage();
        //the Player should get damaged and lose the Powerup but his health points should stay unchanged.
        assertTrue(damaged);
        assertFalse(player.hasPowerUp());
        assertEquals(healthBefore, player.getHealthPoints());
    }

    @Test
    void testDamageReducesHealthWhenNoPowerup() {
        final PlayerImpl player = new PlayerImpl(
            0,
            0,
            GameConstants.PLAYER_WIDTH_TILES,
            GameConstants.PLAYER_HEIGHT_TILES
        );
        final int healthBefore = player.getHealthPoints();

        final boolean damaged = player.applyDamage();

        assertTrue(damaged);
        assertEquals(healthBefore - 1, player.getHealthPoints());
    }
}
