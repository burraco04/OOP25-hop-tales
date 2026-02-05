package view.impl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.CoinStorage;
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

    private final javax.swing.Timer timer = new javax.swing.Timer(1000 / FPS, this);

    private final LevelModel model = new LevelModel();
    private final LevelInput input;
    private final Runnable onHome;

    public FireboyWatergirlLevel() {
        this(null);
    }

    public FireboyWatergirlLevel(final Runnable onHome) {
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

    public boolean isOnGoal(model.entities.api.Player p) {
        return LevelQueries.isOnGoal(model, p);
    }

    public boolean touchesLava(model.entities.api.Player p) {
        return LevelQueries.touchesLava(model, p);
    }

    public void collectCoins(model.entities.api.Player p) {
        LevelInteractions.collectCoins(model, p);
    }

    public void handleButtons(model.entities.api.Player p) {
        LevelInteractions.handleButtons(model, p);
    }

    public void handleTeleport(model.entities.api.Player p) {
        LevelInteractions.handleTeleport(model, p);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
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

    void restart() {
        JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);
        top.dispose();
        main(null);
    }

    public void restartLevel() {
        initializeLevel(false);
        input.reset();
    }

    public void goHome() {
        timer.stop();
        if (onHome != null) {
            onHome.run();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Fireboy & Watergirl - Single Level");
            FireboyWatergirlLevel panel = new FireboyWatergirlLevel();

            panel.setPreferredSize(new Dimension(1000, 800));
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setContentPane(panel);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setResizable(true);
            f.setVisible(true);

            panel.requestFocusInWindow();
        });
    }

    private void initializeLevel(final boolean loadImages) {
        if (loadImages) {
            model.imgMap = Assets.load("/img/mappa_finale.png");
            model.imgDoor = Assets.load("/img/porta_finale.png");
            model.imgCoinGold = Assets.load("/img/coin_gold.png");
            model.imgCoinGoldSide = Assets.load("/img/coin_gold_side.png");
            model.imgPlatform = Assets.load("/img/piattaforma_finale.png");
            model.imgBoulder = Assets.load("/img/masso_finale.png");
            model.imgP1 = Assets.load("/img/Player_1_frame_1.png");
            model.imgP2 = Assets.load("/img/Player_1_frame_2.png");
        }
        LevelBuilder.loadMap(model);
        LevelBuilder.buildAssociations(model);

        model.totalCoinsSaved = CoinStorage.loadTotalCoins();

        // spawn player 1 in alto-sinistra (tile 2,2)
        model.fireboy = new PlayerImpl(2 * LevelConstants.TILE, 2 * LevelConstants.TILE, LevelConstants.TILE, LevelConstants.TILE);

        // spawn player 2 in basso-destra (tile 35,34 come avevi tu)
        model.watergirl = new PlayerImpl((34 - 1) * LevelConstants.TILE, (35 - 1) * LevelConstants.TILE,
                LevelConstants.TILE, LevelConstants.TILE);

        model.gameOver = false;
        model.levelComplete = false;
    }
}
