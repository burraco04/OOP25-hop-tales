package view.utils;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * used for buy the skin.
 */
public class ShopButton extends JPanel{

    private static final long serialVersionUID = 1L;
    private final Image background;
    private final int ROWS = 2;
    private final int COLS = 2;
    private final int PAD_MIN = 20;
    private final int PAD_MAX = 200;
    private final float PAD_PERCENTUALE = 0.08f;
    private final int GAP_MIN = 10;
    private final int GAP_MAX = 200;
    private final float GAP_PERCENTUALE = 0.20f;
    private final GridLayout grid;
    
    /**
     * implements the top bar. 
     *
     * @param back button back
     * @return the pannel where put the button
     */
    public ShopButton() {

        this.background = CreateBackground.create("/img/Shopback.png");

        this.grid = new GridLayout(ROWS, COLS, 0, 0);
        setLayout(this.grid);

        final JButton skin1 = ShopButtonFactory.build("/img/bozza_player_1_vers_3.png");
        final JButton skin2 = ShopButtonFactory.build("/img/skinsqualo.png");
        final JButton skin3 = ShopButtonFactory.build("/img/skinsqualo.png");
        final JButton skin4 = ShopButtonFactory.build("/img/skinsqualo.png");

        skin1.setText("DEFAULT");
        skin2.setText("10$");
        skin3.setText("5$");
        skin4.setText("15$");

        add(skin1);
        add(skin2);
        add(skin3);
        add(skin4);
    }

    @Override
    public void doLayout() {
        // Dimensioni disponibili
        final int w = getWidth();
        final int h = getHeight();

        // Padding proporzionale 
        final int padX = clamp((int) (w * PAD_PERCENTUALE), PAD_MIN, PAD_MAX);
        final int padY = clamp((int) (h * PAD_PERCENTUALE), PAD_MIN, PAD_MAX);

        // Gap proporzionale 
        final int gapX = clamp((int) (w * GAP_PERCENTUALE), GAP_MIN, GAP_MAX);
        final int gapY = clamp((int) (h * GAP_PERCENTUALE), GAP_MIN, GAP_MAX);

        setBorder(BorderFactory.createEmptyBorder(padY, padX, padY, padX));
        this.grid.setHgap(gapX);
        this.grid.setVgap(gapY);

        super.doLayout();
    }

    /**
     * calcola la dimensione dei gap limitandoli
     *
     * @param v misura che gli passo
     * @param min minimo che voglio
     * @param max massimo che voglio
     * @return risultato
     */
    private static int clamp(final int v, final int min, final int max) {
        return Math.max(min, Math.min(max, v));
    }

    /**
     * cancella e ridipinge il pannello.
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
         g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
