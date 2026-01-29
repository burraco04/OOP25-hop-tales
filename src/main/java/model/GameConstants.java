package model;

import java.awt.Color;

/**
 * Class used for storing constants shared by classes present in the model section of the project.
 */
public final class GameConstants {
    public static final int STARTING_POSITION_X = 1;
    public static final int STARTING_POSITION_Y = 24;
    public static final int TARGET_UPS = 45;
    public static final int TARGET_FPS = 60;
    public static final int MILLIS_PER_SECOND = 1000;
    public static final int PLAYER_HEIGHT = 420;
    public static final int PLAYER_WIDTH = 240;
    public static final int PLAYER_WIDTH_TILES = 1;
    public static final int PLAYER_HEIGHT_TILES = 2;
    public static final int PLAYER_SPEED = 1;
    public static final int GRAVITY = 1;
    public static final int COIN_VALUE = 5;
    public static final int COIN_POSITION = 50;
    public static final int TILE_SIZE = 30; //pixel
    public static final int LEVEL_1_WIDTH = 1000;
    public static final Color BACK_COLOR = new Color(34, 85, 34);
    public static final int JUMP_HEIGHT = GameConstants.PLAYER_SPEED * 10;
    public static final int JUMP_STEP = 1;
    public static final float STARTING_MUSIC_VOLUME = 0.7f;
    public static final float MIN_VOLUME = 0.0001f;
    public static final float DB_CONSTANT = 20.0f;
    public static final int STARTING_HEALTH = 3;
    public static final float DAMAGE_COOLDOWN = 1f;

    private GameConstants() { }

}
