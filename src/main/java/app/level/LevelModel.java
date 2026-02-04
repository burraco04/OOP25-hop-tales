package app.level;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LevelModel {

    public final String[] RAW_MAP = {
            "11111111111111111111111111111111111",
            "10000000000000100000000300000000001",
            "10000000440000100000000300000000001",
            "10000000110002100000000300000000001",
            "10040000000001133111111111111111111",
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
            "1000000000000*045500401111110000001",
            "1000000000000*045500401111110000001",
            "10000133331111001111111000011110001",
            "10000000000000000001001000000000001",
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
            "10000044000000000001011000010001111",
            "10000000000110000001000000000003001",
            "17777777777777777771000020000003001",
            "11111111111111111111111111777111111",
            "11111111111111111111111111111111111"
    };

    public int rows, cols;
    public char[][] map;

    public int totalCoinsSaved;

    public double viewScale = 1.0;
    public int viewOffsetX = 0;
    public int viewOffsetY = 0;

    // immagini
    public BufferedImage imgMap;
    public BufferedImage imgDoor;
    public BufferedImage imgCoinGold;
    public BufferedImage imgCoinGoldSide;
    public BufferedImage imgPlatform;
    public BufferedImage imgBoulder;
    public BufferedImage imgP1;
    public BufferedImage imgP2;

    // entità
    public final List<model.objects.impl.Door> doors = new ArrayList<>();
    public final List<model.objects.impl.collectable.Coin> coins = new ArrayList<>();
    public final List<model.objects.impl.Teleporter> teleporters = new ArrayList<>();
    public final List<model.objects.impl.MovingPlatform> platforms = new ArrayList<>();
    public final List<model.objects.impl.Boulder> boulders = new ArrayList<>();
    public final List<model.objects.impl.ButtonPad> buttons = new ArrayList<>();

    // players 
    public model.entities.api.Player fireboy;
    public model.entities.api.Player watergirl;

    public boolean gameOver = false;
    public boolean levelComplete = false;

    // associazioni
    public final Map<Point, String> buttonToDoorId = new HashMap<>();
    public final Map<Point, String> doorPosToId = new HashMap<>();
    public final Map<Point, Point> teleportDestTile = new HashMap<>();
}
