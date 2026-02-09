package model;

import java.awt.Color;

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
    public static final int TILE_SIZE_X = 20;
    public static final int TILE_SIZE_Y = 30;
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
    public static final int LEVEL3_TILE_PIXEL_SIZE = 24;
    public static final int LEVEL3_FRAME_TIMER_FPS = 60;
    public static final int LEVEL3_FIREBOY_SPAWN_TILE_X = 2;
    public static final int LEVEL3_FIREBOY_SPAWN_TILE_Y = 2;
    public static final int LEVEL3_WATERGIRL_SPAWN_TILE_X = 33;
    public static final int LEVEL3_WATERGIRL_SPAWN_TILE_Y = 34;
    public static final int LEVEL3_PLAYER_WIDTH_TILES = 1;
    public static final int LEVEL3_PLAYER_HEIGHT_TILES = 1;
    public static final double LEVEL3_PLAYER_GRAVITY_PER_TICK = 0.35;
    public static final double LEVEL3_PLAYER_MAX_FALL_SPEED = 10.0;
    public static final double LEVEL3_PLAYER_JUMP_SPEED = -8.0;
    public static final int LEVEL3_PLAYER_MOVE_SPEED_PIXELS = 3;
    public static final int LEVEL3_PLATFORM_VERTICAL_TOLERANCE_PIXELS = 3;
    public static final int LEVEL3_CRUSH_TOP_TOLERANCE_PIXELS = 2;
    public static final int LEVEL3_CRUSH_BOTTOM_TOLERANCE_PIXELS = 6;
    public static final double LEVEL3_BOULDER_GRAVITY_PER_TICK = 0.35;
    public static final double LEVEL3_BOULDER_MAX_FALL_SPEED = 10.0;
    public static final int LEVEL3_BOULDER_CORNER_OFFSET_PIXELS = 1;
    public static final int LEVEL3_BOULDER_OPPOSITE_CORNER_OFFSET_PIXELS = 2;
    public static final double LEVEL3_PLATFORM_DEFAULT_SPEED = 1.0;
    public static final int LEVEL3_OVERLAY_ALPHA = 170;
    public static final int LEVEL3_OVERLAY_TITLE_FONT_SIZE = 40;
    public static final int LEVEL3_OVERLAY_SUBTITLE_FONT_SIZE = 18;
    public static final int LEVEL3_OVERLAY_TITLE_VERTICAL_OFFSET = 20;
    public static final int LEVEL3_OVERLAY_SUBTITLE_VERTICAL_OFFSET = 20;
    public static final int LEVEL3_BALANCE_PLATFORM_LEFT_SIDE_VERTICAL_DELTA_TILES = 7;
    public static final int LEVEL3_BALANCE_PLATFORM_RIGHT_SIDE_VERTICAL_DELTA_TILES = -4;
    public static final double LEVEL3_BALANCE_PLATFORM_SPEED_UNITS_PER_TICK = 1.0;
    public static final int[] LEVEL3_DOOR1_ROW_INDEXES = {8, 9};
    public static final int LEVEL3_DOOR1_COLUMN_INDEX = 11;
    public static final int LEVEL3_DOOR1_BUTTON_ROW_INDEX = 35;
    public static final int LEVEL3_DOOR1_BUTTON_COLUMN_INDEX = 25;
    public static final int LEVEL3_DOOR2_ROW_INDEX = 10;
    public static final int[] LEVEL3_DOOR2_COLUMN_INDEXES = {2, 3, 4, 5};
    public static final int LEVEL3_DOOR2_BUTTON_ROW_INDEX = 25;
    public static final int LEVEL3_DOOR2_BUTTON_COLUMN_INDEX = 30;
    public static final int LEVEL3_DOOR3_ROW_INDEX = 20;
    public static final int[] LEVEL3_DOOR3_COLUMN_INDEXES = {7, 8, 9, 10};
    public static final int LEVEL3_DOOR3_BUTTON_ROW_INDEX = 8;
    public static final int LEVEL3_DOOR3_BUTTON_COLUMN_INDEX = 19;
    public static final int[] LEVEL3_DOOR4_ROW_INDEXES = {34, 35};
    public static final int LEVEL3_DOOR4_COLUMN_INDEX = 32;
    public static final int LEVEL3_DOOR4_BUTTON_ROW_INDEX = 4;
    public static final int LEVEL3_DOOR4_BUTTON_COLUMN_INDEX = 14;
    public static final int[] LEVEL3_DOOR5_ROW_INDEXES = {2, 3};
    public static final int LEVEL3_DOOR5_COLUMN_INDEX = 24;
    public static final int LEVEL3_DOOR5_BUTTON_ROW_INDEX = 26;
    public static final int LEVEL3_DOOR5_BUTTON_COLUMN_INDEX = 4;
    public static final int LEVEL3_DOOR6_ROW_INDEX = 5;
    public static final int[] LEVEL3_DOOR6_COLUMN_INDEXES = {16, 17};
    public static final int LEVEL3_DOOR6_BUTTON_ROW_INDEX = 30;
    public static final int LEVEL3_DOOR6_BUTTON_COLUMN_INDEX = 6;
    public static final int LEVEL3_TELEPORT_DESTINATION_1_ROW_INDEX = 22;
    public static final int LEVEL3_TELEPORT_DESTINATION_1_COLUMN_INDEX = 22;
    public static final int[] LEVEL3_TELEPORT_SOURCE_1_ROW_INDEXES = {27, 28};
    public static final int LEVEL3_TELEPORT_SOURCE_1_COLUMN_INDEX = 21;
    public static final int LEVEL3_TELEPORT_DESTINATION_2_ROW_INDEX = 27;
    public static final int LEVEL3_TELEPORT_DESTINATION_2_COLUMN_INDEX = 23;
    public static final int[] LEVEL3_TELEPORT_SOURCE_2_ROW_INDEXES = {22, 23};
    public static final int LEVEL3_TELEPORT_SOURCE_2_COLUMN_INDEX = 21;
    public static final int LEVEL3_TELEPORT_DESTINATION_3_ROW_INDEX = 22;
    public static final int LEVEL3_TELEPORT_DESTINATION_3_COLUMN_INDEX = 2;
    public static final int[] LEVEL3_TELEPORT_SOURCE_3_ROW_INDEXES = {15, 16, 17};
    public static final int LEVEL3_TELEPORT_SOURCE_3_COLUMN_INDEX = 23;
    public static final int LEVEL3_TELEPORT_DESTINATION_4_ROW_INDEX = 3;
    public static final int LEVEL3_TELEPORT_DESTINATION_4_COLUMN_INDEX = 34;
    public static final int[] LEVEL3_TELEPORT_SOURCE_4_ROW_INDEXES = {17, 18, 19};
    public static final int LEVEL3_TELEPORT_SOURCE_4_COLUMN_INDEX = 14;
    public static final JButton SKINDEFAULT = ShopButtonFactory.build("/img/Player_1_frame_1.png");
    public static final JButton SKINSHARK = ShopButtonFactory.build("/img/squalo_frame_1.png");
    public static final JButton SKINPURPLE = ShopButtonFactory.build("/img/purple_player_frame_1.png");
    public static final JButton SKINGHOST = ShopButtonFactory.build("/img/ghost_frame_1.png");

    private GameConstants() { }

}
