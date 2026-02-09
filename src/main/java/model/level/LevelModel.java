package model.level;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.CoinStorage;
import model.GameConstants;
import model.entities.impl.PlayerImpl;

/**
 * Model for the third level.
 */
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "Level model exposes live references to simplify MVC wiring."
)
public final class LevelModel {

    private final String[] rawMap = {
            "11111111111111111111111111111111111",
            "10000000000000100000000300000000001",
            "10000000440000100000000300000000001",
            "10000000110002100001111111100000001",
            "10040000000001133110000000011111111",
            "11111117777100100000000000000000001",
            "10000111111100100000004400000000001",
            "10880100003001100020001100000000001",
            "10880100003011111111000000111100111",
            "13333100001111100001777777100000001",
            "10000000001111100001111111100000001",
            "10000000111111144000000000000110001",
            "19999111111111111000000000000000001",
            "10000000000000100000111111111111111",
            "1000000000440010000010*000000000001",
            "1000000000110010000010*044000000001",
            "1000000000000*11550010*000000000001",
            "1000000000000*145500411111110000001",
            "1000000000000*145500401111110000001",
            "10000133331111101111111000011110001",
            "11111100000000000001001000000000001",
            "10000000000000000001*41000000000001",
            "10000000000000111111*41000000000001",
            "11100000011000000001111000000009991",
            "10000000001000000001000000000200001",
            "10020000001110000001000000001110001",
            "10010000000010000001*00000000000001",
            "10000000000011100001*00001177777111",
            "10000000000000000441111000111111101",
            "10000200000000000111000000000000001",
            "10000100000000000001000010000000401",
            "10000000000000110001000000040004041",
            "10000004400000000001011000010001111",
            "10000000000110000001000000000003001",
            "17777777777777777771000020000003001",
            "11111111111111111111111111777111111",
            "11111111111111111111111111111111111",
    };

    private int rows;
    private int cols;
    private char[][] map;

    private int totalCoinsSaved;

    private double viewScale = 1.0;
    private int viewOffsetX;
    private int viewOffsetY;

    // immagini
    private BufferedImage imgMap;
    private BufferedImage imgDoor;
    private BufferedImage imgCoinGold;
    private BufferedImage imgCoinGoldSide;
    private BufferedImage imgPlatform;
    private BufferedImage imgBoulder;
    private BufferedImage imgP1;
    private BufferedImage imgP2;

    // entità
    private final List<model.objects.impl.Door> doors = new ArrayList<>();
    private final List<model.objects.impl.collectable.Coin> coins = new ArrayList<>();
    private final List<model.objects.impl.Teleporter> teleporters = new ArrayList<>();
    private final List<model.objects.impl.MovingPlatform> platforms = new ArrayList<>();
    private final List<model.objects.impl.Boulder> boulders = new ArrayList<>();
    private final List<model.objects.impl.ButtonPad> buttons = new ArrayList<>();

    // players 
    private PlayerImpl fireboy;
    private PlayerImpl watergirl;

    private boolean gameOver;
    private boolean levelComplete;

    // associazioni
    private final Map<Point, String> buttonToDoorId = new HashMap<>();
    private final Map<Point, String> doorPosToId = new HashMap<>();
    private final Map<Point, Point> teleportDestTile = new HashMap<>();

    /**
     * Creates the level model with default state.
     */
    public LevelModel() {
        CoinStorage.loadTotalCoins();
        totalCoinsSaved = CoinStorage.getCoins();
        // spawn player 1 in alto-sinistra (tile 2,2)
        fireboy = new PlayerImpl(
                GameConstants.LEVEL3_FIREBOY_SPAWN_TILE_X * GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                GameConstants.LEVEL3_FIREBOY_SPAWN_TILE_Y * GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                GameConstants.LEVEL3_PLAYER_WIDTH_TILES * GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                GameConstants.LEVEL3_PLAYER_HEIGHT_TILES * GameConstants.LEVEL3_TILE_PIXEL_SIZE
        );

        // spawn player 2 in basso-destra (tile 35,34)
        watergirl = new PlayerImpl(
                GameConstants.LEVEL3_WATERGIRL_SPAWN_TILE_X * GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                GameConstants.LEVEL3_WATERGIRL_SPAWN_TILE_Y * GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                GameConstants.LEVEL3_PLAYER_WIDTH_TILES * GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                GameConstants.LEVEL3_PLAYER_HEIGHT_TILES * GameConstants.LEVEL3_TILE_PIXEL_SIZE
        );
    }

    /**
     * @return the raw map rows.
     */
    public String[] getRawMap() {
        return rawMap.clone();
    }

    /**
     * @return the number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param rows number of rows
     */
    public void setRows(final int rows) {
        this.rows = rows;
    }

    /**
     * @return the number of columns.
     */
    public int getCols() {
        return cols;
    }

    /**
     * @param cols number of columns
     */
    public void setCols(final int cols) {
        this.cols = cols;
    }

    /**
     * @return the map tiles.
     */
    @SuppressWarnings("PMD.MethodReturnsInternalArray")
    public char[][] getMap() {
        return map;
    }

    /**
     * @param map tiles map
     */
    public void setMap(final char[][] map) {
        final char[][] copy = new char[map.length][];
        for (int i = 0; i < map.length; i++) {
            copy[i] = map[i].clone();
        }
        this.map = copy;
    }

    /**
     * @return the total coins saved.
     */
    public int getTotalCoinsSaved() {
        return totalCoinsSaved;
    }

    /**
     * @param totalCoinsSaved total saved coins
     */
    public void setTotalCoinsSaved(final int totalCoinsSaved) {
        this.totalCoinsSaved = totalCoinsSaved;
    }

    /**
     * @return the current view scale.
     */
    public double getViewScale() {
        return viewScale;
    }

    /**
     * @param viewScale scale factor
     */
    public void setViewScale(final double viewScale) {
        this.viewScale = viewScale;
    }

    /**
     * @return the view X offset.
     */
    public int getViewOffsetX() {
        return viewOffsetX;
    }

    /**
     * @param viewOffsetX horizontal offset
     */
    public void setViewOffsetX(final int viewOffsetX) {
        this.viewOffsetX = viewOffsetX;
    }

    /**
     * @return the view Y offset.
     */
    public int getViewOffsetY() {
        return viewOffsetY;
    }

    /**
     * @param viewOffsetY vertical offset
     */
    public void setViewOffsetY(final int viewOffsetY) {
        this.viewOffsetY = viewOffsetY;
    }

    /**
     * @return map image.
     */
    public BufferedImage getImgMap() {
        return imgMap;
    }

    /**
     * @param imgMap map image
     */
    public void setImgMap(final BufferedImage imgMap) {
        this.imgMap = imgMap;
    }

    /**
     * @return door image.
     */
    public BufferedImage getImgDoor() {
        return imgDoor;
    }

    /**
     * @param imgDoor door image
     */
    public void setImgDoor(final BufferedImage imgDoor) {
        this.imgDoor = imgDoor;
    }

    /**
     * @return coin image.
     */
    public BufferedImage getImgCoinGold() {
        return imgCoinGold;
    }

    /**
     * @param imgCoinGold coin image
     */
    public void setImgCoinGold(final BufferedImage imgCoinGold) {
        this.imgCoinGold = imgCoinGold;
    }

    /**
     * @return coin side image.
     */
    public BufferedImage getImgCoinGoldSide() {
        return imgCoinGoldSide;
    }

    /**
     * @param imgCoinGoldSide coin side image
     */
    public void setImgCoinGoldSide(final BufferedImage imgCoinGoldSide) {
        this.imgCoinGoldSide = imgCoinGoldSide;
    }

    /**
     * @return platform image.
     */
    public BufferedImage getImgPlatform() {
        return imgPlatform;
    }

    /**
     * @param imgPlatform platform image
     */
    public void setImgPlatform(final BufferedImage imgPlatform) {
        this.imgPlatform = imgPlatform;
    }

    /**
     * @return boulder image.
     */
    public BufferedImage getImgBoulder() {
        return imgBoulder;
    }

    /**
     * @param imgBoulder boulder image
     */
    public void setImgBoulder(final BufferedImage imgBoulder) {
        this.imgBoulder = imgBoulder;
    }

    /**
     * @return player 1 image.
     */
    public BufferedImage getImgP1() {
        return imgP1;
    }

    /**
     * @param imgP1 player 1 image
     */
    public void setImgP1(final BufferedImage imgP1) {
        this.imgP1 = imgP1;
    }

    /**
     * @return player 2 image.
     */
    public BufferedImage getImgP2() {
        return imgP2;
    }

    /**
     * @param imgP2 player 2 image
     */
    public void setImgP2(final BufferedImage imgP2) {
        this.imgP2 = imgP2;
    }

    /**
     * @return doors list.
     */
    public List<model.objects.impl.Door> getDoors() {
        return doors;
    }

    /**
     * @return coins list.
     */
    public List<model.objects.impl.collectable.Coin> getCoins() {
        return coins;
    }

    /**
     * @return teleporters list.
     */
    public List<model.objects.impl.Teleporter> getTeleporters() {
        return teleporters;
    }

    /**
     * @return platforms list.
     */
    public List<model.objects.impl.MovingPlatform> getPlatforms() {
        return platforms;
    }

    /**
     * @return boulders list.
     */
    public List<model.objects.impl.Boulder> getBoulders() {
        return boulders;
    }

    /**
     * @return buttons list.
     */
    public List<model.objects.impl.ButtonPad> getButtons() {
        return buttons;
    }

    /**
     * @return fireboy player.
     */
    public PlayerImpl getFireboy() {
        return fireboy;
    }

    /**
     * @param fireboy fireboy player
     */
    public void setFireboy(final PlayerImpl fireboy) {
        this.fireboy = fireboy;
    }

    /**
     * @return watergirl player.
     */
    public PlayerImpl getWatergirl() {
        return watergirl;
    }

    /**
     * @param watergirl watergirl player
     */
    public void setWatergirl(final PlayerImpl watergirl) {
        this.watergirl = watergirl;
    }

    /**
     * @return true if the game is over.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @param gameOver game over flag
     */
    public void setGameOver(final boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * @return true if the level is complete.
     */
    public boolean isLevelComplete() {
        return levelComplete;
    }

    /**
     * @param levelComplete level completion flag
     */
    public void setLevelComplete(final boolean levelComplete) {
        this.levelComplete = levelComplete;
    }

    /**
     * @return button-to-door map.
     */
    public Map<Point, String> getButtonToDoorId() {
        return buttonToDoorId;
    }

    /**
     * @return door position map.
     */
    public Map<Point, String> getDoorPosToId() {
        return doorPosToId;
    }

    /**
     * @return teleporter destinations map.
     */
    public Map<Point, Point> getTeleportDestTile() {
        return teleportDestTile;
    }
}
