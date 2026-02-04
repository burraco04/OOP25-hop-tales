package app.level;

public final class LevelQueries {

    private LevelQueries() {}
    // verifica se un pixel cade su qualcosa di solido (mappa + oggetti)
    public static boolean isSolidAtPixel(LevelModel m, int px, int py, Object ignore) {
        int c = px / FireboyWatergirlLevel.TILE;
        int r = py / FireboyWatergirlLevel.TILE;

        if (r < 0 || r >= m.rows || c < 0 || c >= m.cols) return true;

        char ch = m.map[r][c];

        if (ch == '1') return true;

        // porte chiuse = solide
        if (ch == '3') {
            for (model.objects.impl.Door d : m.doors) {
                if (d != ignore && d.contains(px, py) && !d.open) return true;
            }
        }

        // piattaforme
        for (model.objects.impl.MovingPlatform p : m.platforms) {
            if (p != ignore && p.contains(px, py)) return true;
        }

        // massi
        for (model.objects.impl.Boulder b : m.boulders) {
            if (b != ignore && b.contains(px, py)) return true;
        }

        return false;
    }

    public static boolean isLavaAtPixel(LevelModel m, int px, int py) {
        int c = px / FireboyWatergirlLevel.TILE;
        int r = py / FireboyWatergirlLevel.TILE;
        if (r < 0 || r >= m.rows || c < 0 || c >= m.cols) return false;
        return m.map[r][c] == '7';
    }

    public static boolean isOnGoal(LevelModel m, model.entities.Player p) {
        int cx = p.x + p.w / 2;
        int cy = p.y + p.h / 2;
        int c = cx / FireboyWatergirlLevel.TILE;
        int r = cy / FireboyWatergirlLevel.TILE;
        if (r < 0 || r >= m.rows || c < 0 || c >= m.cols) return false;
        return m.map[r][c] == '5';
    }

    public static boolean touchesLava(LevelModel m, model.entities.Player p) {
        return isLavaAtPixel(m, p.x + 1, p.y + p.h - 1)
                || isLavaAtPixel(m, p.x + p.w - 2, p.y + p.h - 1);
    }
}
