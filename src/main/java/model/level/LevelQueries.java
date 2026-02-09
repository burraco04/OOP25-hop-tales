package model.level;

import model.GameConstants;

/**
 * Query helpers for level geometry and interactions.
 */
public final class LevelQueries {

    private LevelQueries() {
    }

    /**
     * Checks if a pixel hits something solid.
     *
     * @param model level model
     * @param px x coordinate
     * @param py y coordinate
     * @param ignore object to ignore (can be null)
     * @return true if solid
     */
    public static boolean isSolidAtPixel(final LevelModel model, final int px, final int py,
            final Object ignore) {
        final int c = px / GameConstants.LEVEL3_TILE_PIXEL_SIZE;
        final int r = py / GameConstants.LEVEL3_TILE_PIXEL_SIZE;

        if (r < 0 || r >= model.getRows() || c < 0 || c >= model.getCols()) {
            return true;
        }

        final char ch = model.getMap()[r][c];

        if (ch == '1') {
            return true;
        }

        // porte chiuse = solide
        if (ch == '3') {
            for (final model.objects.impl.Door d : model.getDoors()) {
                if (!d.equals(ignore) && d.contains(px, py) && !d.isOpen()) {
                    return true;
                }
            }
        }

        // piattaforme
        for (final model.objects.impl.MovingPlatform p : model.getPlatforms()) {
            if (!p.equals(ignore) && p.contains(px, py)) {
                return true;
            }
        }

        // massi
        for (final model.objects.impl.Boulder b : model.getBoulders()) {
            if (!b.equals(ignore) && b.contains(px, py)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param model level model
     * @param px x coordinate
     * @param py y coordinate
     * @return true if the pixel is on lava
     */
    public static boolean isLavaAtPixel(final LevelModel model, final int px, final int py) {
        final int c = px / GameConstants.LEVEL3_TILE_PIXEL_SIZE;
        final int r = py / GameConstants.LEVEL3_TILE_PIXEL_SIZE;
        if (r < 0 || r >= model.getRows() || c < 0 || c >= model.getCols()) {
            return false;
        }
        return model.getMap()[r][c] == '7';
    }

    /**
     * @param model level model
     * @param player player entity
     * @return true if the player is on the goal tile
     */
    public static boolean isOnGoal(final LevelModel model, final model.entities.api.Player player) {
        final int cx = (int) Math.round(player.getX() + player.getWidth() / 2.0);
        final int cy = (int) Math.round(player.getY() + player.getHeight() / 2.0);
        final int c = cx / GameConstants.LEVEL3_TILE_PIXEL_SIZE;
        final int r = cy / GameConstants.LEVEL3_TILE_PIXEL_SIZE;
        if (r < 0 || r >= model.getRows() || c < 0 || c >= model.getCols()) {
            return false;
        }
        return model.getMap()[r][c] == '5';
    }

    /**
     * @param model level model
     * @param player player entity
     * @return true if the player touches lava
     */
    public static boolean touchesLava(final LevelModel model, final model.entities.api.Player player) {
        final int px = (int) Math.round(player.getX());
        final int py = (int) Math.round(player.getY());
        final int w = (int) Math.round(player.getWidth());
        final int h = (int) Math.round(player.getHeight());
        return isLavaAtPixel(model, px + 1, py + h - 1)
                || isLavaAtPixel(model, px + w - 2, py + h - 1);
    }
}
