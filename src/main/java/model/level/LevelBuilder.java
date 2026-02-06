package model.level;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Builds level entities from the raw map.
 */
public final class LevelBuilder {

    private static final int BALANCE_LEFT_DY_TILES = 7;
    private static final int BALANCE_RIGHT_DY_TILES = -4;

    private static final int[] DOOR1_ROWS = {8, 9};
    private static final int DOOR1_COL = 11;
    private static final int DOOR1_BUTTON_ROW = 35;
    private static final int DOOR1_BUTTON_COL = 25;

    private static final int DOOR2_ROW = 10;
    private static final int[] DOOR2_COLS = {2, 3, 4, 5};
    private static final int DOOR2_BUTTON_ROW = 25;
    private static final int DOOR2_BUTTON_COL = 30;

    private static final int DOOR3_ROW = 20;
    private static final int[] DOOR3_COLS = {7, 8, 9, 10};
    private static final int DOOR3_BUTTON_ROW = 8;
    private static final int DOOR3_BUTTON_COL = 19;

    private static final int[] DOOR4_ROWS = {34, 35};
    private static final int DOOR4_COL = 32;
    private static final int DOOR4_BUTTON_ROW = 4;
    private static final int DOOR4_BUTTON_COL = 14;

    private static final int[] DOOR5_ROWS = {2, 3};
    private static final int DOOR5_COL = 24;
    private static final int DOOR5_BUTTON_ROW = 26;
    private static final int DOOR5_BUTTON_COL = 4;

    private static final int DOOR6_ROW = 5;
    private static final int[] DOOR6_COLS = {16, 17};
    private static final int DOOR6_BUTTON_ROW = 30;
    private static final int DOOR6_BUTTON_COL = 6;

    private static final int T1_ROW = 22;
    private static final int T1_COL = 22;
    private static final int[] T1_FROM_ROWS = {27, 28};
    private static final int T1_FROM_COL = 21;

    private static final int T2_ROW = 27;
    private static final int T2_COL = 23;
    private static final int[] T2_FROM_ROWS = {22, 23};
    private static final int T2_FROM_COL = 21;

    private static final int T3_ROW = 22;
    private static final int T3_COL = 2;
    private static final int[] T3_FROM_ROWS = {15, 16, 17};
    private static final int T3_FROM_COL = 23;

    private static final int T4_ROW = 3;
    private static final int T4_COL = 34;
    private static final int[] T4_FROM_ROWS = {17, 18, 19};
    private static final int T4_FROM_COL = 14;

    private LevelBuilder() {
    }

    /**
     * Reads the raw map and builds the level entities.
     *
     * @param model level model
     */
    public static void loadMap(final LevelModel model) {
        model.getDoors().clear();
        model.getCoins().clear();
        model.getTeleporters().clear();
        model.getPlatforms().clear();
        model.getBoulders().clear();
        model.getButtons().clear();

        final String[] rawMap = model.getRawMap();
        model.setRows(rawMap.length);
        model.setCols(rawMap[0].length());
        model.setMap(new char[model.getRows()][model.getCols()]);

        for (int r = 0; r < model.getRows(); r++) {
            if (rawMap[r].length() != model.getCols()) {
                throw new IllegalStateException("Riga " + r + " length diversa: " + rawMap[r].length()
                        + " vs " + model.getCols());
            }

            for (int c = 0; c < model.getCols(); c++) {
                final char ch = rawMap[r].charAt(c);

                model.getMap()[r][c] = ch;

                switch (ch) {
                    case '3' -> model.getDoors().add(new model.objects.impl.Door(
                            c * LevelConstants.TILE, r * LevelConstants.TILE,
                            LevelConstants.TILE, LevelConstants.TILE,
                            model.getImgDoor(), LevelConstants.TILE));

                    case '4' -> model.getCoins().add(new model.objects.impl.collectable.Coin(
                            c * LevelConstants.TILE, r * LevelConstants.TILE));

                    case '*' -> model.getTeleporters().add(new model.objects.impl.Teleporter(
                            c * LevelConstants.TILE, r * LevelConstants.TILE,
                            LevelConstants.TILE, LevelConstants.TILE));

                    case '2' -> model.getButtons().add(new model.objects.impl.ButtonPad(
                            c * LevelConstants.TILE, r * LevelConstants.TILE,
                            LevelConstants.TILE, LevelConstants.TILE));

                    default -> { }
                }
            }
        }

        buildMovingPlatformsFromMap(model);
        buildBouldersFromMapFloodFill(model);
    }

    // raggruppa blocchi '8' adiacenti e li trasforma in un unico Boulder (rettangolo)
    private static void buildBouldersFromMapFloodFill(final LevelModel model) {
        final boolean[][] vis = new boolean[model.getRows()][model.getCols()];

        for (int r = 0; r < model.getRows(); r++) {
            for (int c = 0; c < model.getCols(); c++) {
                if (model.getMap()[r][c] != '8' || vis[r][c]) {
                    continue;
                }

                final ArrayDeque<Point> q = new ArrayDeque<>();
                q.add(new Point(c, r));
                vis[r][c] = true;

                int minR = r;
                int maxR = r;
                int minC = c;
                int maxC = c;

                while (!q.isEmpty()) {
                    final Point p = q.poll();
                    final int cc = p.x;
                    final int rr = p.y;

                    minR = Math.min(minR, rr);
                    maxR = Math.max(maxR, rr);
                    minC = Math.min(minC, cc);
                    maxC = Math.max(maxC, cc);

                    final int[][] dir = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                    for (final int[] d : dir) {
                        final int nc = cc + d[0];
                        final int nr = rr + d[1];
                        if (nr < 0 || nr >= model.getRows() || nc < 0 || nc >= model.getCols()) {
                            continue;
                        }
                        if (vis[nr][nc]) {
                            continue;
                        }
                        if (model.getMap()[nr][nc] != '8') {
                            continue;
                        }
                        vis[nr][nc] = true;
                        q.add(new Point(nc, nr));
                    }
                }

                final int x = minC * LevelConstants.TILE;
                final int y = minR * LevelConstants.TILE;
                final int w = (maxC - minC + 1) * LevelConstants.TILE;
                final int h = (maxR - minR + 1) * LevelConstants.TILE;

                model.getBoulders().add(new model.objects.impl.Boulder(
                        x, y, w, h, model.getImgBoulder(), LevelConstants.TILE));
            }
        }
    }

    private static void buildMovingPlatformsFromMap(final LevelModel model) {
        final boolean[][] used = new boolean[model.getRows()][model.getCols()];

        for (int r = 0; r < model.getRows(); r++) {
            for (int c = 0; c < model.getCols(); c++) {
                if (model.getMap()[r][c] != '9' || used[r][c]) {
                    continue;
                }

                final int startC = c;
                int endC = c;

                while (endC < model.getCols() && model.getMap()[r][endC] == '9' && !used[r][endC]) {
                    used[r][endC] = true;
                    endC++;
                }

                final int tilesWide = endC - startC;
                final int x = startC * LevelConstants.TILE;
                final int y = r * LevelConstants.TILE;
                final int w = tilesWide * LevelConstants.TILE;
                final int h = LevelConstants.TILE;

                model.getPlatforms().add(new model.objects.impl.MovingPlatform(
                        x, y, w, h, model.getImgPlatform(), LevelConstants.TILE));
            }
        }

        if (model.getPlatforms().size() >= 2) {
            model.getPlatforms().sort(Comparator.comparingInt(p -> p.getX()));
            final model.objects.impl.MovingPlatform left = model.getPlatforms().get(0);
            final model.objects.impl.MovingPlatform right = model.getPlatforms().get(model.getPlatforms().size() - 1);

            left.setBalanceRole(true, BALANCE_LEFT_DY_TILES * LevelConstants.TILE, 1.0);
            right.setBalanceRole(false, BALANCE_RIGHT_DY_TILES * LevelConstants.TILE, 1.0);
        }
    }

    /**
     * Builds level associations (buttons, doors, teleporters).
     *
     * @param model level model
     */
    public static void buildAssociations(final LevelModel model) {
        model.getButtonToDoorId().clear();
        model.getDoorPosToId().clear();
        model.getTeleportDestTile().clear();

        final java.util.function.BiFunction<Integer, Integer, Point> rc =
                (r, c) -> new Point(c - 1, r - 1);

        final java.util.function.BiConsumer<String, List<Point>> addDoorTiles = (id, tiles) -> {
            for (final Point t : tiles) {
                model.getDoorPosToId().put(t, id);
            }
        };

        final java.util.function.BiFunction<Integer, int[], List<Point>> rowWithCols = (row, colsArr) -> {
            final List<Point> out = new ArrayList<>();
            for (final int col : colsArr) {
                out.add(rc.apply(row, col));
            }
            return out;
        };

        final java.util.function.BiFunction<int[], Integer, List<Point>> colWithRows = (rowsArr, col) -> {
            final List<Point> out = new ArrayList<>();
            for (final int row : rowsArr) {
                out.add(rc.apply(row, col));
            }
            return out;
        };

        final java.util.function.BiConsumer<Point, String> linkButton =
                (btnTile, doorId) -> model.getButtonToDoorId().put(btnTile, doorId);

        final java.util.function.BiConsumer<Point, Point> linkTeleport =
                (fromTile, toTile) -> model.getTeleportDestTile().put(fromTile, toTile);

        addDoorTiles.accept("D1", colWithRows.apply(DOOR1_ROWS, DOOR1_COL));
        linkButton.accept(rc.apply(DOOR1_BUTTON_ROW, DOOR1_BUTTON_COL), "D1");

        addDoorTiles.accept("D2", rowWithCols.apply(DOOR2_ROW, DOOR2_COLS));
        linkButton.accept(rc.apply(DOOR2_BUTTON_ROW, DOOR2_BUTTON_COL), "D2");

        addDoorTiles.accept("D3", rowWithCols.apply(DOOR3_ROW, DOOR3_COLS));
        linkButton.accept(rc.apply(DOOR3_BUTTON_ROW, DOOR3_BUTTON_COL), "D3");

        addDoorTiles.accept("D4", colWithRows.apply(DOOR4_ROWS, DOOR4_COL));
        linkButton.accept(rc.apply(DOOR4_BUTTON_ROW, DOOR4_BUTTON_COL), "D4");

        addDoorTiles.accept("D5", colWithRows.apply(DOOR5_ROWS, DOOR5_COL));
        linkButton.accept(rc.apply(DOOR5_BUTTON_ROW, DOOR5_BUTTON_COL), "D5");

        addDoorTiles.accept("D6", rowWithCols.apply(DOOR6_ROW, DOOR6_COLS));
        linkButton.accept(rc.apply(DOOR6_BUTTON_ROW, DOOR6_BUTTON_COL), "D6");

        final Point t1 = rc.apply(T1_ROW, T1_COL);
        for (final int r : T1_FROM_ROWS) {
            linkTeleport.accept(rc.apply(r, T1_FROM_COL), t1);
        }

        final Point t2 = rc.apply(T2_ROW, T2_COL);
        for (final int r : T2_FROM_ROWS) {
            linkTeleport.accept(rc.apply(r, T2_FROM_COL), t2);
        }

        final Point t3 = rc.apply(T3_ROW, T3_COL);
        for (final int r : T3_FROM_ROWS) {
            linkTeleport.accept(rc.apply(r, T3_FROM_COL), t3);
        }

        final Point t4 = rc.apply(T4_ROW, T4_COL);
        for (final int r : T4_FROM_ROWS) {
            linkTeleport.accept(rc.apply(r, T4_FROM_COL), t4);
        }
    }

    static void removeDoorTilesFromMap(final LevelModel model, final String doorId) {
        for (final Map.Entry<Point, String> entry : model.getDoorPosToId().entrySet()) {
            if (doorId.equals(entry.getValue())) {
                final Point t = entry.getKey();
                final int rr = t.y;
                final int cc = t.x;
                if (rr >= 0 && rr < model.getRows() && cc >= 0 && cc < model.getCols()) {
                    model.getMap()[rr][cc] = '0';
                }
            }
        }
        model.getDoorPosToId().entrySet().removeIf(entry -> doorId.equals(entry.getValue()));
    }
}
