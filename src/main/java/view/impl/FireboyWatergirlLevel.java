package view.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.Timer;

import controller.level.LevelInput;
import controller.level.LevelLogic;
import model.GameConstants;
import model.entities.api.Player;
import model.level.LevelBuilder;
import model.level.LevelInteractions;
import model.level.LevelModel;
import model.level.LevelQueries;
import view.utils.Assets;
import view.utils.Draw;

/**
 * Pannello principale del livello di gioco.
 * Gestisce ciclo di gioco, input e rendering.
 */
public final class FireboyWatergirlLevel extends JPanel implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private final Timer timer = new Timer(1000 / GameConstants.LEVEL3_FRAME_TIMER_FPS, this);

    private final transient LevelModel model;
    private final transient LevelInput input;
    private final transient Runnable onHome;
    private String lastSkinFrame1;

    /**
     * Creates the level panel.
     */
    public FireboyWatergirlLevel() {
        this(null);
    }

    /**
     * Creates the level panel.
     *
     * @param onHome callback used to return to the main menu
     */
    public FireboyWatergirlLevel(final Runnable onHome) {
        this.model = new LevelModel();
        this.onHome = onHome;
        this.input = new LevelInput(onHome);
        setFocusable(true);
        addKeyListener(this);

        initializeLevel(true);

        timer.start();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        LevelLogic.tick(this, model, input);
        refreshPlayerSkinsIfNeeded();
        repaint();
    }

    /**
     * Checks if the map is solid at the given pixel coordinates.
     *
     * @param px x coordinate in pixels
     * @param py y coordinate in pixels
     * @return true if the tile is solid
     */
    public boolean isSolidAtPixel(final int px, final int py) {
        return isSolidAtPixel(px, py, null);
    }

    /**
     * Checks if the map is solid at the given pixel coordinates, ignoring an object.
     *
     * @param px x coordinate in pixels
     * @param py y coordinate in pixels
     * @param ignore object to ignore during collision checks
     * @return true if the tile is solid
     */
    public boolean isSolidAtPixel(final int px, final int py, final Object ignore) {
        return LevelQueries.isSolidAtPixel(model, px, py, ignore);
    }

    /**
     * Checks if the map is lava at the given pixel coordinates.
     *
     * @param px x coordinate in pixels
     * @param py y coordinate in pixels
     * @return true if the tile is lava
     */
    public boolean isLavaAtPixel(final int px, final int py) {
        return LevelQueries.isLavaAtPixel(model, px, py);
    }

    /**
     * Checks if the given player is on the goal tile.
     *
     * @param p the player to check
     * @return true if the player is on a goal tile
     */
    public boolean isOnGoal(final Player p) {
        return LevelQueries.isOnGoal(model, p);
    }

    /**
     * Checks if the given player touches lava.
     *
     * @param p the player to check
     * @return true if the player is touching lava
     */
    public boolean touchesLava(final Player p) {
        return LevelQueries.touchesLava(model, p);
    }

    /**
     * Collects coins for the given player if possible.
     *
     * @param p the player collecting coins
     */
    public void collectCoins(final Player p) {
        LevelInteractions.collectCoins(model, p);
    }

    /**
     * Handles button interactions for the given player.
     *
     * @param p the player interacting with buttons
     */
    public void handleButtons(final Player p) {
        LevelInteractions.handleButtons(model, p);
    }

    /**
     * Handles teleporter interactions for the given player.
     *
     * @param p the player interacting with teleporters
     */
    public void handleTeleport(final Player p) {
        LevelInteractions.handleTeleport(model, p);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        LevelRenderer.render(this, model, g);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        input.keyPressed(this, model, e);
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        input.keyReleased(e);
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        // Not used.
    }

    /**
     * Restarts the current level state.
     */
    public void restartLevel() {
        initializeLevel(false);
        model.setGameOver(false);
        model.setLevelComplete(false);
        resetPlayersToSpawn();
        input.reset();
    }

    /**
     * Returns to the main menu if available.
     */
    public void goHome() {
        timer.stop();
        if (onHome != null) {
            onHome.run();
        }
    }

    private void initializeLevel(final boolean loadImages) {
        if (loadImages) {
            model.setImgMap(Assets.load("/img/mappa_finale.png"));
            model.setImgDoor(Assets.load("/img/porta_finale.png"));
            model.setImgCoinGold(Assets.load("/img/coin_gold.png"));
            model.setImgCoinGoldSide(Assets.load("/img/coin_gold_side.png"));
            model.setImgPlatform(Assets.load("/img/piattaforma_finale.png"));
            model.setImgBoulder(Assets.load("/img/masso_finale.png"));
            refreshPlayerSkinsIfNeeded();
        }
        LevelBuilder.loadMap(model);
        LevelBuilder.buildAssociations(model);
    }

    private void refreshPlayerSkinsIfNeeded() {
        final String frame1 = resolveSkinPath(Draw.getPlayerFrame1());
        if (frame1.equals(lastSkinFrame1)) {
            return;
        }
        lastSkinFrame1 = frame1;
        final java.awt.image.BufferedImage img = Assets.load(frame1);
        model.setImgP1(img);
        model.setImgP2(img);
    }

    private static String resolveSkinPath(final String framePath) {
        if (framePath.startsWith("/")) {
            return framePath;
        }
        return "/" + framePath;
    }

    private void resetPlayersToSpawn() {
        model.getFireboy().setX(GameConstants.LEVEL3_FIREBOY_SPAWN_TILE_X * GameConstants.LEVEL3_TILE_PIXEL_SIZE);
        model.getFireboy().setY(GameConstants.LEVEL3_FIREBOY_SPAWN_TILE_Y * GameConstants.LEVEL3_TILE_PIXEL_SIZE);
        model.getFireboy().setVelocityX(0);
        model.getFireboy().setVelocityY(0);
        model.getFireboy().setOnGround(false);

        model.getWatergirl().setX(
                GameConstants.LEVEL3_WATERGIRL_SPAWN_TILE_X * GameConstants.LEVEL3_TILE_PIXEL_SIZE
        );
        model.getWatergirl().setY(
                GameConstants.LEVEL3_WATERGIRL_SPAWN_TILE_Y * GameConstants.LEVEL3_TILE_PIXEL_SIZE
        );
        model.getWatergirl().setVelocityX(0);
        model.getWatergirl().setVelocityY(0);
        model.getWatergirl().setOnGround(false);
    }
}
