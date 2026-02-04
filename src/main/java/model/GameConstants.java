package model;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;

import view.utils.ShopButtonFactory;

/**
 * Class used for storing constants shared by classes present in the model section of the project.
 */
public final class GameConstants {
    public static final int STARTING_POSITION_X = 1;
    public static final int STARTING_POSITION_Y = 24;
    public static final int TARGET_UPS = 90;
    public static final int TARGET_FPS = 60;
    public static final int MILLIS_PER_SECOND = 1000;
    public static final int PLAYER_WIDTH_TILES = 1;
    public static final int PLAYER_HEIGHT_TILES = 2;
    public static final double PLAYER_SPEED = 0.25;
    public static final double GRAVITY = 0.4;
    public static final int COIN_VALUE = 5;
    public static final int COIN_POSITION = 50;
    public static final int TILE_SIZE = 30; //pixel
    public static final int LEVEL_1_WIDTH = 1000;
    public static final int LEVEL_2_WIDTH = 800;
    public static final Color BACK_COLOR = new Color(34, 85, 34);
    public static final double JUMP_HEIGHT = PLAYER_SPEED * 25.0;
    public static final double JUMP_STEP = 0.5;
    public static final float STARTING_VOLUME = 0.1f;
    public static final float MIN_VOLUME = 0.0001f;
    public static final float DB_CONSTANT = 20.0f;
    public static final int STARTING_HEALTH = 3;
    public static final float DAMAGE_COOLDOWN = 0.7f;
    public static final int MILLIS_PER_FRAME = 16;
    public static final int ENEMY_WITDH = 1;
    public static final int ENEMY_HEIGHT = 1;
    public static final String FULL_HEART = "full_heart";
    public static final String EMPTY_HEART = "empty_heart";
    public static final int COIN_COUNT_SIZE = 28;
    public static final int SKIN_COST = 20;
    public static final JButton SKINDEFAULT = ShopButtonFactory.build("/img/Player_1_frame_1.png");
    public static final JButton SKINSHARK = ShopButtonFactory.build("/img/squalo_frame_1.png");
    public static final JButton SKINPURPLE = ShopButtonFactory.build("/img/purple_player_frame_1.png");
    public static final JButton SKINGHOST = ShopButtonFactory.build("/img/ghost_frame_1.png");
    public static final Set<JButton> PURCHASED_BUTTON = new HashSet<>(Set.of(SKINDEFAULT));
    public static final Set<JButton> TO_BUY_BUTTONS =  new HashSet<>(Set.of(SKINGHOST, SKINPURPLE, SKINSHARK));

    private GameConstants() { }

}
