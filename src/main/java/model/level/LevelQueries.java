package model.level;

public final class LevelQueries {

    private LevelQueries() {}
    // verifica se un pixel cade su qualcosa di solido (mappa + oggetti)
    public static boolean isSolidAtPixel(LevelModel m, int px, int py, Object ignore) {
        int c = px / LevelConstants.TILE;
        int r = py / LevelConstants.TILE;

        if (r < 0 || r >= m.getRows() || c < 0 || c >= m.getCols()) return true;

        char ch = m.getMap()[r][c];

        if (ch == '1') return true;

        // porte chiuse = solide
        if (ch == '3') {
            for (model.objects.impl.Door d : m.getDoors()) {
                if (d != ignore && d.contains(px, py) && !d.open) return true;
            }
        }

        // piattaforme
        for (model.objects.impl.MovingPlatform p : m.getPlatforms()) {
            if (p != ignore && p.contains(px, py)) return true;
        }

        // massi
        for (model.objects.impl.Boulder b : m.getBoulders()) {
            if (b != ignore && b.contains(px, py)) return true;
        }

        return false;
    }

    public static boolean isLavaAtPixel(LevelModel m, int px, int py) {
        int c = px / LevelConstants.TILE;
        int r = py / LevelConstants.TILE;
        if (r < 0 || r >= m.getRows() || c < 0 || c >= m.getCols()) return false;
        return m.getMap()[r][c] == '7';
    }

    public static boolean isOnGoal(LevelModel m, model.entities.api.Player p) {
        int cx = (int) Math.round(p.getX() + p.getWidth() / 2.0);
        int cy = (int) Math.round(p.getY() + p.getHeight() / 2.0);
        int c = cx / LevelConstants.TILE;
        int r = cy / LevelConstants.TILE;
        if (r < 0 || r >= m.getRows() || c < 0 || c >= m.getCols()) return false;
        return m.getMap()[r][c] == '5';
    }

    public static boolean touchesLava(LevelModel m, model.entities.api.Player p) {
        int px = (int) Math.round(p.getX());
        int py = (int) Math.round(p.getY());
        int w = (int) Math.round(p.getWidth());
        int h = (int) Math.round(p.getHeight());
        return isLavaAtPixel(m, px + 1, py + h - 1)
                || isLavaAtPixel(m, px + w - 2, py + h - 1);
    }
}
