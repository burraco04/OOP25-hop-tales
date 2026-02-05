package model.level;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class LevelBuilder {

    private LevelBuilder() {}

    //legge RAW_MAP e costruisce gli oggetti del livello
    public static void loadMap(LevelModel m) {
        m.doors.clear();
        m.coins.clear();
        m.teleporters.clear();
        m.platforms.clear();
        m.boulders.clear();
        m.buttons.clear();

        m.rows = m.RAW_MAP.length;
        m.cols = m.RAW_MAP[0].length();
        m.map = new char[m.rows][m.cols];

        for (int r = 0; r < m.rows; r++) {
            if (m.RAW_MAP[r].length() != m.cols) {
                throw new IllegalStateException("Riga " + r + " length diversa: " + m.RAW_MAP[r].length() + " vs " + m.cols);
            }

            for (int c = 0; c < m.cols; c++) {
                char ch = m.RAW_MAP[r].charAt(c);

                

                m.map[r][c] = ch;

                switch (ch) {
                    case '3' -> m.doors.add(new model.objects.impl.Door(
                            c * LevelConstants.TILE, r * LevelConstants.TILE,
                            LevelConstants.TILE, LevelConstants.TILE,
                            m.imgDoor, LevelConstants.TILE));

                    case '4' -> m.coins.add(new model.objects.impl.collectable.Coin(
                            c * LevelConstants.TILE, r * LevelConstants.TILE));

                    case '*' -> m.teleporters.add(new model.objects.impl.Teleporter(
                            c * LevelConstants.TILE, r * LevelConstants.TILE,
                            LevelConstants.TILE, LevelConstants.TILE));

                    case '2' -> m.buttons.add(new model.objects.impl.ButtonPad(
                            c * LevelConstants.TILE, r * LevelConstants.TILE,
                            LevelConstants.TILE, LevelConstants.TILE));

                    default -> { /* niente */ }
                }
            }
        }

        buildMovingPlatformsFromMap(m);
        buildBouldersFromMapFloodFill(m);
    }
// raggruppa blocchi '8' adiacenti e li trasforma in un unico Boulder (rettangolo)
    private static void buildBouldersFromMapFloodFill(LevelModel m) {
        boolean[][] vis = new boolean[m.rows][m.cols];

        for (int r = 0; r < m.rows; r++) {
            for (int c = 0; c < m.cols; c++) {
                if (m.map[r][c] != '8' || vis[r][c]) continue;

                ArrayDeque<Point> q = new ArrayDeque<>();
                q.add(new Point(c, r));
                vis[r][c] = true;

                int minR = r, maxR = r, minC = c, maxC = c;

                while (!q.isEmpty()) {
                    Point p = q.poll();
                    int cc = p.x, rr = p.y;

                    minR = Math.min(minR, rr);
                    maxR = Math.max(maxR, rr);
                    minC = Math.min(minC, cc);
                    maxC = Math.max(maxC, cc);

                    int[][] dir = {{1,0},{-1,0},{0,1},{0,-1}};
                    for (int[] d : dir) {
                        int nc = cc + d[0], nr = rr + d[1];
                        if (nr < 0 || nr >= m.rows || nc < 0 || nc >= m.cols) continue;
                        if (vis[nr][nc]) continue;
                        if (m.map[nr][nc] != '8') continue;
                        vis[nr][nc] = true;
                        q.add(new Point(nc, nr));
                    }
                }

                int x = minC * LevelConstants.TILE;
                int y = minR * LevelConstants.TILE;
                int w = (maxC - minC + 1) * LevelConstants.TILE;
                int h = (maxR - minR + 1) * LevelConstants.TILE;

                m.boulders.add(new model.objects.impl.Boulder(x, y, w, h, m.imgBoulder, LevelConstants.TILE));
            }
        }
    }

    private static void buildMovingPlatformsFromMap(LevelModel m) {
        boolean[][] used = new boolean[m.rows][m.cols];

        for (int r = 0; r < m.rows; r++) {
            for (int c = 0; c < m.cols; c++) {
                if (m.map[r][c] != '9' || used[r][c]) continue;

                int startC = c;
                int endC = c;

                while (endC < m.cols && m.map[r][endC] == '9' && !used[r][endC]) {
                    used[r][endC] = true;
                    endC++;
                }

                int tilesWide = endC - startC;
                int x = startC * LevelConstants.TILE;
                int y = r * LevelConstants.TILE;
                int w = tilesWide * LevelConstants.TILE;
                int h = LevelConstants.TILE;

                m.platforms.add(new model.objects.impl.MovingPlatform(x, y, w, h, m.imgPlatform, LevelConstants.TILE));
            }
        }

        if (m.platforms.size() >= 2) {
            m.platforms.sort(Comparator.comparingInt(p -> p.getX()));
            model.objects.impl.MovingPlatform left = m.platforms.get(0);
            model.objects.impl.MovingPlatform right = m.platforms.get(m.platforms.size() - 1);

            left.setBalanceRole(true, +7 * LevelConstants.TILE, 1.0);
            right.setBalanceRole(false, -4 * LevelConstants.TILE, 1.0);
        }
    }

    public static void buildAssociations(LevelModel m) {
        java.util.function.BiFunction<Integer, Integer, Point> RC = (r, c) -> new Point(c - 1, r - 1);

        java.util.function.BiConsumer<String, List<Point>> addDoorTiles = (id, tiles) -> {
            for (Point t : tiles) m.doorPosToId.put(t, id);
        };

        java.util.function.BiFunction<Integer, int[], List<Point>> rowWithCols = (row, colsArr) -> {
            List<Point> out = new ArrayList<>();
            for (int col : colsArr) out.add(RC.apply(row, col));
            return out;
        };

        java.util.function.BiFunction<int[], Integer, List<Point>> colWithRows = (rowsArr, col) -> {
            List<Point> out = new ArrayList<>();
            for (int row : rowsArr) out.add(RC.apply(row, col));
            return out;
        };

        java.util.function.BiConsumer<Point, String> linkButton = (btnTile, doorId) ->
                m.buttonToDoorId.put(btnTile, doorId);

        java.util.function.BiConsumer<Point, Point> linkTeleport = (fromTile, toTile) ->
                m.teleportDestTile.put(fromTile, toTile);

        addDoorTiles.accept("D1", colWithRows.apply(new int[]{8, 9}, 11));
        linkButton.accept(RC.apply(35, 25), "D1");

        addDoorTiles.accept("D2", rowWithCols.apply(10, new int[]{2, 3, 4, 5}));
        linkButton.accept(RC.apply(25, 30), "D2");

        addDoorTiles.accept("D3", rowWithCols.apply(20, new int[]{7, 8, 9, 10}));
        linkButton.accept(RC.apply(8, 19), "D3");

        addDoorTiles.accept("D4", colWithRows.apply(new int[]{34, 35}, 32));
        linkButton.accept(RC.apply(4, 14), "D4");

        addDoorTiles.accept("D5", colWithRows.apply(new int[]{2, 3}, 24));
        linkButton.accept(RC.apply(26, 4), "D5");

        addDoorTiles.accept("D6", rowWithCols.apply(5, new int[]{16, 17}));
        linkButton.accept(RC.apply(30, 6), "D6");

        Point T1 = RC.apply(22, 22);
        for (int r : new int[]{27, 28}) linkTeleport.accept(RC.apply(r, 21), T1);

        Point T2 = RC.apply(27, 22);
        for (int r : new int[]{22, 23}) linkTeleport.accept(RC.apply(r, 21), T2);

        Point T3 = RC.apply(22, 2);
        for (int r : new int[]{15, 16, 17}) linkTeleport.accept(RC.apply(r, 23), T3);

        Point T4 = RC.apply(3, 34);
        for (int r : new int[]{17, 18, 19}) linkTeleport.accept(RC.apply(r, 14), T4);
    }

    static void removeDoorTilesFromMap(LevelModel m, String doorId) {
        for (Map.Entry<Point, String> e : m.doorPosToId.entrySet()) {
            if (doorId.equals(e.getValue())) {
                Point t = e.getKey();
                int rr = t.y;
                int cc = t.x;
                if (rr >= 0 && rr < m.rows && cc >= 0 && cc < m.cols) {
                    m.map[rr][cc] = '0';
                }
            }
        }
        m.doorPosToId.entrySet().removeIf(en -> doorId.equals(en.getValue()));
    }
}
