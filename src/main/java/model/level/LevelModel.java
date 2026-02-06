package model.level;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.CoinStorage;
import model.entities.impl.PlayerImpl;

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
            "10000000000000000441110000111111101",
            "10000200000000000111000000000000001",
            "10000100000000000001000010000000401",
            "10000000000000110001000000040004041",
            "10000004400000000001011000010001111",
            "10000000000110000001000000000003001",
            "17777777777777777771000020000003001",
            "11111111111111111111111111777111111",
            "11111111111111111111111111111111111"
    };

    private int rows;
    private int cols;
    private char[][] map;

    private int totalCoinsSaved;

    private double viewScale = 1.0;
    private int viewOffsetX = 0;
    private int viewOffsetY = 0;

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
    private model.entities.impl.PlayerImpl fireboy;
    private model.entities.impl.PlayerImpl watergirl;

    private boolean gameOver = false;
    private boolean levelComplete = false;

    // associazioni
    private final Map<Point, String> buttonToDoorId = new HashMap<>();
    private final Map<Point, String> doorPosToId = new HashMap<>();
    private final Map<Point, Point> teleportDestTile = new HashMap<>();

    public LevelModel() {
        totalCoinsSaved = CoinStorage.loadTotalCoins();
        // spawn player 1 in alto-sinistra (tile 2,2)
        fireboy = new PlayerImpl(2 * LevelConstants.TILE, 2 * LevelConstants.TILE, LevelConstants.TILE, LevelConstants.TILE);

        // spawn player 2 in basso-destra (tile 35,34)
        watergirl = new PlayerImpl((34 - 1) * LevelConstants.TILE, (35 - 1) * LevelConstants.TILE, LevelConstants.TILE, LevelConstants.TILE);
        levelComplete = false;
        gameOver = false;
    }

    public String[] getRawMap() {
        return rawMap;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public char[][] getMap() {
        return map;
    }

    public void setMap(char[][] map) {
        this.map = map;
    }

    public int getTotalCoinsSaved() {
        return totalCoinsSaved;
    }

    public void setTotalCoinsSaved(int totalCoinsSaved) {
        this.totalCoinsSaved = totalCoinsSaved;
    }

    public double getViewScale() {
        return viewScale;
    }

    public void setViewScale(double viewScale) {
        this.viewScale = viewScale;
    }

    public int getViewOffsetX() {
        return viewOffsetX;
    }

    public void setViewOffsetX(int viewOffsetX) {
        this.viewOffsetX = viewOffsetX;
    }

    public int getViewOffsetY() {
        return viewOffsetY;
    }

    public void setViewOffsetY(int viewOffsetY) {
        this.viewOffsetY = viewOffsetY;
    }

    public BufferedImage getImgMap() {
        return imgMap;
    }

    public void setImgMap(BufferedImage imgMap) {
        this.imgMap = imgMap;
    }

    public BufferedImage getImgDoor() {
        return imgDoor;
    }

    public void setImgDoor(BufferedImage imgDoor) {
        this.imgDoor = imgDoor;
    }

    public BufferedImage getImgCoinGold() {
        return imgCoinGold;
    }

    public void setImgCoinGold(BufferedImage imgCoinGold) {
        this.imgCoinGold = imgCoinGold;
    }

    public BufferedImage getImgCoinGoldSide() {
        return imgCoinGoldSide;
    }

    public void setImgCoinGoldSide(BufferedImage imgCoinGoldSide) {
        this.imgCoinGoldSide = imgCoinGoldSide;
    }

    public BufferedImage getImgPlatform() {
        return imgPlatform;
    }

    public void setImgPlatform(BufferedImage imgPlatform) {
        this.imgPlatform = imgPlatform;
    }

    public BufferedImage getImgBoulder() {
        return imgBoulder;
    }

    public void setImgBoulder(BufferedImage imgBoulder) {
        this.imgBoulder = imgBoulder;
    }

    public BufferedImage getImgP1() {
        return imgP1;
    }

    public void setImgP1(BufferedImage imgP1) {
        this.imgP1 = imgP1;
    }

    public BufferedImage getImgP2() {
        return imgP2;
    }

    public void setImgP2(BufferedImage imgP2) {
        this.imgP2 = imgP2;
    }

    public List<model.objects.impl.Door> getDoors() {
        return doors;
    }

    public List<model.objects.impl.collectable.Coin> getCoins() {
        return coins;
    }

    public List<model.objects.impl.Teleporter> getTeleporters() {
        return teleporters;
    }

    public List<model.objects.impl.MovingPlatform> getPlatforms() {
        return platforms;
    }

    public List<model.objects.impl.Boulder> getBoulders() {
        return boulders;
    }

    public List<model.objects.impl.ButtonPad> getButtons() {
        return buttons;
    }

    public model.entities.impl.PlayerImpl getFireboy() {
        return fireboy;
    }

    public void setFireboy(PlayerImpl fireboy) {
        this.fireboy = fireboy;
    }

    public model.entities.impl.PlayerImpl getWatergirl() {
        return watergirl;
    }

    public void setWatergirl(PlayerImpl watergirl) {
        this.watergirl = watergirl;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public void setLevelComplete(boolean levelComplete) {
        this.levelComplete = levelComplete;
    }

    public Map<Point, String> getButtonToDoorId() {
        return buttonToDoorId;
    }

    public Map<Point, String> getDoorPosToId() {
        return doorPosToId;
    }

    public Map<Point, Point> getTeleportDestTile() {
        return teleportDestTile;
    }
}
