package view.impl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import model.CoinStorage;
import model.entities.api.Player;
import model.entities.impl.PlayerImpl;
import model.level.LevelBuilder;
import model.level.LevelConstants;
import model.level.LevelInteractions;
import model.level.LevelModel;
import model.level.LevelQueries;
import controller.level.LevelInput;
import controller.level.LevelLogic;
import view.utils.Assets;

/**
 * Pannello principale del livello di gioco.
 * Gestisce ciclo di gioco, input e rendering.
 */
public class FireboyWatergirlLevel extends JPanel implements ActionListener, KeyListener {

    private static final int FPS = 60;

    private final Timer timer = new Timer(1000 / FPS, this);

    private final LevelModel model;
    private final LevelInput input;
    private final Runnable onHome;

    public FireboyWatergirlLevel() {
        this(null);
    }

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
    public void actionPerformed(ActionEvent e) {
        LevelLogic.tick(this, model, input);
        repaint();
    }

    // ====== API usata da Player/Boulder per collisioni ======
    public boolean isSolidAtPixel(int px, int py) {
        return isSolidAtPixel(px, py, null);
    }

    public boolean isSolidAtPixel(int px, int py, Object ignore) {
        return LevelQueries.isSolidAtPixel(model, px, py, ignore);
    }

    public boolean isLavaAtPixel(int px, int py) {
        return LevelQueries.isLavaAtPixel(model, px, py);
    }

    public boolean isOnGoal(Player p) {
        return LevelQueries.isOnGoal(model, p);
    }

    public boolean touchesLava(Player p) {
        return LevelQueries.touchesLava(model, p);
    }

    public void collectCoins(Player p) {
        LevelInteractions.collectCoins(model, p);
    }

    public void handleButtons(Player p) {
        LevelInteractions.handleButtons(model, p);
    }

    public void handleTeleport(Player p) {
        LevelInteractions.handleTeleport(model, p);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        LevelRenderer.render(this, model, g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        input.keyPressed(this, model, e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        input.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}


    public void restartLevel() {
        initializeLevel(false);
        model.setGameOver(false);
        model.setLevelComplete(false);
        resetPlayersToSpawn();
        input.reset();
    }

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
            model.setImgP1(Assets.load("/img/Player_1_frame_1.png"));
            model.setImgP2(Assets.load("/img/Player_1_frame_2.png"));
        }
        LevelBuilder.loadMap(model);
        LevelBuilder.buildAssociations(model);
    }

    private void resetPlayersToSpawn() {
        model.getFireboy().setX(2 * LevelConstants.TILE);
        model.getFireboy().setY(2 * LevelConstants.TILE);
        model.getFireboy().setVelocityX(0);
        model.getFireboy().setVelocityY(0);
        model.getFireboy().setOnGround(false);

        model.getWatergirl().setX((34 - 1) * LevelConstants.TILE);
        model.getWatergirl().setY((35 - 1) * LevelConstants.TILE);
        model.getWatergirl().setVelocityX(0);
        model.getWatergirl().setVelocityY(0);
        model.getWatergirl().setOnGround(false);
    }
}
