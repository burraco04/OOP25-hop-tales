package app.level;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Pannello principale del livello di gioco.
 * Gestisce ciclo di gioco, input e rendering.
 */
public class FireboyWatergirlLevel extends JPanel implements ActionListener, KeyListener {

    public static final int TILE = 24;
    private static final int FPS = 60;

    private final javax.swing.Timer timer = new javax.swing.Timer(1000 / FPS, this);

    private final LevelModel model = new LevelModel();
    private final LevelInput input = new LevelInput();

    public FireboyWatergirlLevel() {
        setFocusable(true);
        addKeyListener(this);

        LevelBuilder.loadImages(model);
        LevelBuilder.loadMap(model);
        LevelBuilder.buildAssociations(model);

        model.totalCoinsSaved = model.CoinStorage.loadTotalCoins();

        // spawn player 1 in alto-sinistra (tile 2,2)
        model.fireboy = new model.entities.api.Player(2 * TILE, 2 * TILE, TILE, model.imgP1);

        // spawn player 2 in basso-destra (tile 35,34 come avevi tu)
        model.watergirl = new model.entities.api.Player((34 - 1) * TILE, (35 - 1) * TILE, TILE, model.imgP2);

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

    public boolean isOnGoal(model.entities.Player p) {
        return LevelQueries.isOnGoal(model, p);
    }

    public boolean touchesLava(model.entities.Player p) {
        return LevelQueries.touchesLava(model, p);
    }

    public void collectCoins(model.entities.Player p) {
        LevelInteractions.collectCoins(model, p);
    }

    public void handleButtons(model.entities.Player p) {
        LevelInteractions.handleButtons(model, p);
    }

    public void handleTeleport(model.entities.Player p) {
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
}
