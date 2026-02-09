package model.level;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import model.GameConstants;
import model.objects.api.AbstractWorldEntity;

/**
 * Builds level entities from the raw map.
 */
public final class LevelBuilder {

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
                            c * GameConstants.LEVEL3_TILE_PIXEL_SIZE, r * GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                            GameConstants.LEVEL3_TILE_PIXEL_SIZE, GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                            model.getImgDoor(), GameConstants.LEVEL3_TILE_PIXEL_SIZE));

                    case '4' -> model.getCoins().add(new model.objects.impl.collectable.Coin(
                            c * GameConstants.LEVEL3_TILE_PIXEL_SIZE, r * GameConstants.LEVEL3_TILE_PIXEL_SIZE));

                    case '*' -> model.getTeleporters().add(new model.objects.impl.Teleporter(
                            c * GameConstants.LEVEL3_TILE_PIXEL_SIZE, r * GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                            GameConstants.LEVEL3_TILE_PIXEL_SIZE, GameConstants.LEVEL3_TILE_PIXEL_SIZE));

                    case '2' -> model.getButtons().add(new model.objects.impl.ButtonPad(
                            c * GameConstants.LEVEL3_TILE_PIXEL_SIZE, r * GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                            GameConstants.LEVEL3_TILE_PIXEL_SIZE, GameConstants.LEVEL3_TILE_PIXEL_SIZE));

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

                final Deque<Point> q = new java.util.ArrayDeque<>();
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

                final int x = minC * GameConstants.LEVEL3_TILE_PIXEL_SIZE;
                final int y = minR * GameConstants.LEVEL3_TILE_PIXEL_SIZE;
                final int w = (maxC - minC + 1) * GameConstants.LEVEL3_TILE_PIXEL_SIZE;
                final int h = (maxR - minR + 1) * GameConstants.LEVEL3_TILE_PIXEL_SIZE;

                model.getBoulders().add(new model.objects.impl.Boulder(
                        x, y, w, h, model.getImgBoulder(), GameConstants.LEVEL3_TILE_PIXEL_SIZE));
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
                final int x = startC * GameConstants.LEVEL3_TILE_PIXEL_SIZE;
                final int y = r * GameConstants.LEVEL3_TILE_PIXEL_SIZE;
                final int w = tilesWide * GameConstants.LEVEL3_TILE_PIXEL_SIZE;
                model.getPlatforms().add(new model.objects.impl.MovingPlatform(
                        x, y, w, GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                        model.getImgPlatform(), GameConstants.LEVEL3_TILE_PIXEL_SIZE));
            }
        }

        if (model.getPlatforms().size() >= 2) {
            model.getPlatforms().sort(Comparator.comparingInt(AbstractWorldEntity::getX));
            final model.objects.impl.MovingPlatform left = model.getPlatforms().get(0);
            final model.objects.impl.MovingPlatform right = model.getPlatforms().get(model.getPlatforms().size() - 1);

            left.setBalanceRole(
                    true,
                    GameConstants.LEVEL3_BALANCE_PLATFORM_LEFT_SIDE_VERTICAL_DELTA_TILES
                            * GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                    GameConstants.LEVEL3_BALANCE_PLATFORM_SPEED_UNITS_PER_TICK
            );
            right.setBalanceRole(
                    false,
                    GameConstants.LEVEL3_BALANCE_PLATFORM_RIGHT_SIDE_VERTICAL_DELTA_TILES
                            * GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                    GameConstants.LEVEL3_BALANCE_PLATFORM_SPEED_UNITS_PER_TICK
            );
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

        addDoorTiles.accept("D1",
                colWithRows.apply(GameConstants.LEVEL3_DOOR1_ROW_INDEXES, GameConstants.LEVEL3_DOOR1_COLUMN_INDEX));
        linkButton.accept(
                rc.apply(GameConstants.LEVEL3_DOOR1_BUTTON_ROW_INDEX, GameConstants.LEVEL3_DOOR1_BUTTON_COLUMN_INDEX),
                "D1"
        );

        addDoorTiles.accept("D2",
                rowWithCols.apply(GameConstants.LEVEL3_DOOR2_ROW_INDEX, GameConstants.LEVEL3_DOOR2_COLUMN_INDEXES));
        linkButton.accept(
                rc.apply(GameConstants.LEVEL3_DOOR2_BUTTON_ROW_INDEX, GameConstants.LEVEL3_DOOR2_BUTTON_COLUMN_INDEX),
                "D2"
        );

        addDoorTiles.accept("D3",
                rowWithCols.apply(GameConstants.LEVEL3_DOOR3_ROW_INDEX, GameConstants.LEVEL3_DOOR3_COLUMN_INDEXES));
        linkButton.accept(
                rc.apply(GameConstants.LEVEL3_DOOR3_BUTTON_ROW_INDEX, GameConstants.LEVEL3_DOOR3_BUTTON_COLUMN_INDEX),
                "D3"
        );

        addDoorTiles.accept("D4",
                colWithRows.apply(GameConstants.LEVEL3_DOOR4_ROW_INDEXES, GameConstants.LEVEL3_DOOR4_COLUMN_INDEX));
        linkButton.accept(
                rc.apply(GameConstants.LEVEL3_DOOR4_BUTTON_ROW_INDEX, GameConstants.LEVEL3_DOOR4_BUTTON_COLUMN_INDEX),
                "D4"
        );

        addDoorTiles.accept("D5",
                colWithRows.apply(GameConstants.LEVEL3_DOOR5_ROW_INDEXES, GameConstants.LEVEL3_DOOR5_COLUMN_INDEX));
        linkButton.accept(
                rc.apply(GameConstants.LEVEL3_DOOR5_BUTTON_ROW_INDEX, GameConstants.LEVEL3_DOOR5_BUTTON_COLUMN_INDEX),
                "D5"
        );

        addDoorTiles.accept("D6",
                rowWithCols.apply(GameConstants.LEVEL3_DOOR6_ROW_INDEX, GameConstants.LEVEL3_DOOR6_COLUMN_INDEXES));
        linkButton.accept(
                rc.apply(GameConstants.LEVEL3_DOOR6_BUTTON_ROW_INDEX, GameConstants.LEVEL3_DOOR6_BUTTON_COLUMN_INDEX),
                "D6"
        );

        final Point t1 = rc.apply(
                GameConstants.LEVEL3_TELEPORT_DESTINATION_1_ROW_INDEX,
                GameConstants.LEVEL3_TELEPORT_DESTINATION_1_COLUMN_INDEX
        );
        for (final int r : GameConstants.LEVEL3_TELEPORT_SOURCE_1_ROW_INDEXES) {
            linkTeleport.accept(rc.apply(r, GameConstants.LEVEL3_TELEPORT_SOURCE_1_COLUMN_INDEX), t1);
        }

        final Point t2 = rc.apply(
                GameConstants.LEVEL3_TELEPORT_DESTINATION_2_ROW_INDEX,
                GameConstants.LEVEL3_TELEPORT_DESTINATION_2_COLUMN_INDEX
        );
        for (final int r : GameConstants.LEVEL3_TELEPORT_SOURCE_2_ROW_INDEXES) {
            linkTeleport.accept(rc.apply(r, GameConstants.LEVEL3_TELEPORT_SOURCE_2_COLUMN_INDEX), t2);
        }

        final Point t3 = rc.apply(
                GameConstants.LEVEL3_TELEPORT_DESTINATION_3_ROW_INDEX,
                GameConstants.LEVEL3_TELEPORT_DESTINATION_3_COLUMN_INDEX
        );
        for (final int r : GameConstants.LEVEL3_TELEPORT_SOURCE_3_ROW_INDEXES) {
            linkTeleport.accept(rc.apply(r, GameConstants.LEVEL3_TELEPORT_SOURCE_3_COLUMN_INDEX), t3);
        }

        final Point t4 = rc.apply(
                GameConstants.LEVEL3_TELEPORT_DESTINATION_4_ROW_INDEX,
                GameConstants.LEVEL3_TELEPORT_DESTINATION_4_COLUMN_INDEX
        );
        for (final int r : GameConstants.LEVEL3_TELEPORT_SOURCE_4_ROW_INDEXES) {
            linkTeleport.accept(rc.apply(r, GameConstants.LEVEL3_TELEPORT_SOURCE_4_COLUMN_INDEX), t4);
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
