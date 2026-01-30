package game;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class FireboyWatergirlLevel extends JPanel implements ActionListener, KeyListener {

    // ====== MAPPA ======
    private final String[] RAW_MAP = {
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
            "1000000000000*045500401177110000001",
            "1000000000000*045500401111110000001",
            "10000133331111001111111000011119991",
            "10000000000000000001001000000000001",
            "1*000000000000000001*41000000000001",
            "1*000000000000111111*41000000000001",
            "11000000011000000001111000000000001",
            "10000000001000000001000000000200001",
            "10020000001110000001000000001110001",
            "10010000000010000001*00000000000001",
            "10000000000011100001*00001177777111",
            "10000000000000000441111000111111101",
            "10000200000000000111000000000000001",
            "10000100000000000001000110000000401",
            "10000000000000110001000000040004041",
            "10000044000000000001011000010001111",
            "10000000000110000001000000000003001",
            "17777777777777777771000020000003001",
            "11111111111111111111111111777111111",
            "11111111111111111111111111111111111"
    };

    // ====== CONFIG ======
    private static final int TILE = 24;
    private static final int FPS = 60;

    private final javax.swing.Timer timer = new javax.swing.Timer(1000 / FPS, this);

    private int rows, cols;
    private char[][] map;

    // ====== VIEW (auto-scale per far stare tutto nello schermo) ======
    private double viewScale = 1.0;
    private int viewOffsetX = 0;
    private int viewOffsetY = 0;

    // Entities
    private final List<Door> doors = new ArrayList<>();
    private final List<Coin> coins = new ArrayList<>();
    private final List<Teleporter> teleporters = new ArrayList<>();
    private final List<MovingPlatform> platforms = new ArrayList<>();
    private final List<Boulder> boulders = new ArrayList<>();
    private final List<ButtonPad> buttons = new ArrayList<>();

    // Players (test)
    private final Player fireboy = new Player(2 * TILE, 2 * TILE, new Color(255, 80, 80));
    private final Player watergirl = new Player(4 * TILE, 2 * TILE, new Color(80, 120, 255));

    private boolean gameOver = false;
    private boolean levelComplete = false;

    // ASSOCIAZIONI
    private final Map<Point, String> buttonToDoorId = new HashMap<>();
    private final Map<Point, String> doorPosToId = new HashMap<>();
    private final Map<Point, Point> teleportDestTile = new HashMap<>();

    public FireboyWatergirlLevel() {
        setFocusable(true);
        addKeyListener(this);

        loadMap();
        buildAssociations();
        timer.start();
    }

    private void loadMap() {
        rows = RAW_MAP.length;
        cols = RAW_MAP[0].length();
        map = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            if (RAW_MAP[r].length() != cols) {
                throw new IllegalStateException("Riga " + r + " ha lunghezza diversa: " + RAW_MAP[r].length() + " vs " + cols);
            }
            for (int c = 0; c < cols; c++) {
                char ch = RAW_MAP[r].charAt(c);

                // cancella bottone (18,13) 1-based se presente
                if (r == 18 - 1 && c == 13 - 1 && ch == '2') ch = '0';

                map[r][c] = ch;

                switch (ch) {
                    case '3' -> doors.add(new Door(c * TILE, r * TILE, TILE, TILE));
                    case '4' -> coins.add(new Coin(c * TILE, r * TILE, TILE, TILE));
                    case '*' -> teleporters.add(new Teleporter(c * TILE, r * TILE, TILE, TILE));
                    case '8' -> boulders.add(new Boulder(c * TILE, r * TILE, TILE, TILE));
                    case '2' -> buttons.add(new ButtonPad(c * TILE, r * TILE, TILE, TILE));
                    // case '9' -> NON creare tile singoli: li raggruppiamo dopo
                }
            }
        }

        // Crea piattaforme raggruppando i 9 vicini
        buildMovingPlatformsFromMap();
    }

    // Raggruppa tutti i 9 consecutivi (stessa riga) in una piattaforma unica
    private void buildMovingPlatformsFromMap() {
        boolean[][] used = new boolean[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] != '9' || used[r][c]) continue;

                int startC = c;
                int endC = c;
                while (endC < cols && map[r][endC] == '9' && !used[r][endC]) {
                    used[r][endC] = true;
                    endC++;
                }

                int tilesWide = endC - startC;
                int x = startC * TILE;
                int y = r * TILE;
                int w = tilesWide * TILE;
                int h = TILE;

                platforms.add(new MovingPlatform(x, y, w, h));
            }
        }

        // Imposta bilancia sulle 2 piattaforme più estreme (sinistra e destra)
        if (platforms.size() >= 2) {
            platforms.sort(Comparator.comparingInt(p -> p.x));
            MovingPlatform left = platforms.get(0);
            MovingPlatform right = platforms.get(platforms.size() - 1);

            left.setBalanceRole(true, +7 * TILE, 1.0);   // sinistra scende di 7 tile
            right.setBalanceRole(false, -4 * TILE, 1.0); // destra sale di 4 tile
        }
    }

    private void buildAssociations() {
        // Helper: (riga,colonna) 1-based -> Point(col,row) 0-based
        java.util.function.BiFunction<Integer, Integer, Point> RC = (r, c) -> new Point(c - 1, r - 1);

        java.util.function.BiConsumer<String, List<Point>> addDoorTiles = (id, tiles) -> {
            for (Point t : tiles) doorPosToId.put(t, id);
        };

        java.util.function.BiFunction<Integer, int[], List<Point>> rowWithCols = (row, colsArr) -> {
            List<Point> out = new ArrayList<>();
            for (int col : colsArr) out.add(RC.apply(row, col));
            return out;
        };

        java.util.function.BiFunction<int[], Integer, List<Point>> colWithRows = (rowsArr, col) -> {
            List<Point> out = new ArrayList<>();
            for (int row : rowsArr) out.add(RC.apply(row, col));
            return out;
        };

        java.util.function.BiConsumer<Point, String> linkButton = (btnTile, doorId) ->
                buttonToDoorId.put(btnTile, doorId);

        java.util.function.BiConsumer<Point, Point> linkTeleport = (fromTile, toTile) ->
                teleportDestTile.put(fromTile, toTile);

        // BOTTONI -> PORTE
        addDoorTiles.accept("D1", colWithRows.apply(new int[]{7, 8}, 11));
        linkButton.accept(RC.apply(34, 25), "D1");

        addDoorTiles.accept("D2", rowWithCols.apply(9, new int[]{2, 3, 4, 5}));
        linkButton.accept(RC.apply(24, 30), "D2");

        addDoorTiles.accept("D3", rowWithCols.apply(19, new int[]{7, 8, 9, 10}));
        linkButton.accept(RC.apply(7, 19), "D3");

        addDoorTiles.accept("D4", colWithRows.apply(new int[]{33, 34}, 32));
        linkButton.accept(RC.apply(3, 14), "D4");

        addDoorTiles.accept("D5", colWithRows.apply(new int[]{2, 3}, 24));
        linkButton.accept(RC.apply(25, 4), "D5");

        addDoorTiles.accept("D6", rowWithCols.apply(4, new int[]{16, 17}));
        linkButton.accept(RC.apply(29, 6), "D6");

        // TELEPORT
        Point T1 = RC.apply(22, 22);
        for (int r : new int[]{26, 27}) linkTeleport.accept(RC.apply(r, 21), T1);

        Point T2 = RC.apply(27, 22);
        for (int r : new int[]{21, 22}) linkTeleport.accept(RC.apply(r, 21), T2);

        Point T3 = RC.apply(22, 2);
        for (int r : new int[]{14, 15, 16}) linkTeleport.accept(RC.apply(r, 23), T3);

        Point T4 = RC.apply(3, 34);
        for (int r : new int[]{16, 17, 18}) linkTeleport.accept(RC.apply(r, 14), T4);
    }

    // ====== GAME LOOP ======
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver || levelComplete) {
            repaint();
            return;
        }

        fireboy.update(this);
        watergirl.update(this);

        // Lava check
        if (touchesLava(fireboy) || touchesLava(watergirl)) {
            gameOver = true;
        }

        // Coin pickup
        collectCoins(fireboy);
        collectCoins(watergirl);

        // Buttons
        handleButtons(fireboy);
        handleButtons(watergirl);

        // Teleport
        handleTeleport(fireboy);
        handleTeleport(watergirl);

        // Boulder physics + push
        for (Boulder b : boulders) {
            b.updatePhysics(this);
            b.tryPushBy(fireboy, this);
            b.tryPushBy(watergirl, this);
        }

        // ====== BILANCIA ======
        boolean balanceActive = false;
        MovingPlatform leftPlatform = null;

        for (MovingPlatform p : platforms) {
            if (p.isLeftSide) { leftPlatform = p; break; }
        }

        if (leftPlatform != null) {
            for (Boulder b : boulders) {
                if (isBoulderOnPlatform(b, leftPlatform)) {
                    balanceActive = true;
                    break;
                }
            }
        }

        // Update piattaforme (si muovono tutte insieme in base allo stato bilancia)
        for (MovingPlatform p : platforms) {
            p.updateBalance(balanceActive);
        }

        // Trascina i massi che stanno sopra le piattaforme (così restano “appoggiati”)
        for (MovingPlatform p : platforms) {
            int dx = p.deltaX();
            int dy = p.deltaY();
            if (dx == 0 && dy == 0) continue;

            for (Boulder b : boulders) {
                if (isBoulderOnPlatform(b, p)) {
                    b.x += dx;
                    b.y += dy;
                }
            }
        }

        // Goal finale: se entrambi su tile '5'
        if (isOnGoal(fireboy) && isOnGoal(watergirl)) levelComplete = true;

        repaint();
    }

    private boolean isBoulderOnPlatform(Boulder b, MovingPlatform p) {
        Rectangle br = b.rect();
        Rectangle pr = p.rect();

        int bBottom = br.y + br.height;
        boolean xOverlap = br.x + br.width > pr.x && br.x < pr.x + pr.width;

        // tolleranza contatto 3px
        boolean onTop = (bBottom >= pr.y - 3) && (bBottom <= pr.y + 3);
        return xOverlap && onTop;
    }

    // ====== COLLISION & TILE QUERIES ======
    boolean isSolidAtPixel(int px, int py) {
        int c = px / TILE;
        int r = py / TILE;
        if (r < 0 || r >= rows || c < 0 || c >= cols) return true;
        char ch = map[r][c];

        if (ch == '1') return true;

        // porte chiuse = solide
        if (ch == '3') {
            for (Door d : doors) {
                if (d.contains(px, py) && !d.open) return true;
            }
        }

        // piattaforme mobili solide in base alla posizione attuale
        for (MovingPlatform p : platforms) {
            if (p.contains(px, py)) return true;
        }

        return false;
    }

    boolean isLavaAtPixel(int px, int py) {
        int c = px / TILE;
        int r = py / TILE;
        if (r < 0 || r >= rows || c < 0 || c >= cols) return false;
        return map[r][c] == '7';
    }

    boolean isOnGoal(Player p) {
        int cx = p.x + p.w / 2;
        int cy = p.y + p.h / 2;
        int c = cx / TILE, r = cy / TILE;
        if (r < 0 || r >= rows || c < 0 || c >= cols) return false;
        return map[r][c] == '5';
    }

    boolean touchesLava(Player p) {
        return isLavaAtPixel(p.x + 1, p.y + p.h - 1)
                || isLavaAtPixel(p.x + p.w - 2, p.y + p.h - 1);
    }

    void collectCoins(Player p) {
        Iterator<Coin> it = coins.iterator();
        while (it.hasNext()) {
            Coin c = it.next();
            if (!c.collected && c.intersects(p.getRect())) {
                c.collected = true;
                it.remove();
            }
        }
    }

    void handleButtons(Player p) {
        for (ButtonPad b : buttons) {
            if (b.intersects(p.getRect())) {
                Point tilePos = new Point(b.x / TILE, b.y / TILE); // col,row
                String doorId = buttonToDoorId.get(tilePos);
                if (doorId != null) {
                    for (Door d : doors) {
                        Point dTile = new Point(d.x / TILE, d.y / TILE);
                        String id = doorPosToId.get(dTile);
                        if (doorId.equals(id)) d.open = true;
                    }
                }
            }
        }
    }

    void handleTeleport(Player p) {
        for (Teleporter t : teleporters) {
            if (t.intersects(p.getRect())) {
                Point from = new Point(t.x / TILE, t.y / TILE);
                Point dest = teleportDestTile.get(from);
                if (dest != null) {
                    p.x = dest.x * TILE;
                    p.y = dest.y * TILE;
                }
            }
        }
    }

    // ====== DRAW (AUTO-SCALE: tutta la mappa sempre visibile) ======
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // pixel del mondo
        int worldW = cols * TILE;
        int worldH = rows * TILE;

        // scala per farci stare tutto
        double sx = getWidth() / (double) worldW;
        double sy = getHeight() / (double) worldH;
        viewScale = Math.min(sx, sy);

        // centra
        viewOffsetX = (int) ((getWidth() - worldW * viewScale) / 2.0);
        viewOffsetY = (int) ((getHeight() - worldH * viewScale) / 2.0);

        AffineTransform old = g2.getTransform();

        g2.translate(viewOffsetX, viewOffsetY);
        g2.scale(viewScale, viewScale);

        // background
        g2.setColor(new Color(20, 20, 20));
        g2.fillRect(0, 0, worldW, worldH);

        // tiles statici
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = map[r][c];
                int x = c * TILE, y = r * TILE;

                if (ch == '1') {
                    g2.setColor(new Color(90, 90, 90));
                    g2.fillRect(x, y, TILE, TILE);
                } else if (ch == '7') {
                    g2.setColor(new Color(255, 120, 0));
                    g2.fillRect(x, y, TILE, TILE);
                } else if (ch == '5') {
                    g2.setColor(new Color(120, 255, 120));
                    g2.fillRect(x, y, TILE, TILE);
                }
            }
        }

        // entities
        for (Door d : doors) d.draw(g2);
        for (Coin c : coins) c.draw(g2);
        for (Teleporter t : teleporters) t.draw(g2);
        for (MovingPlatform p : platforms) p.draw(g2);
        for (Boulder b : boulders) b.draw(g2);
        for (ButtonPad b : buttons) b.draw(g2);

        fireboy.draw(g2);
        watergirl.draw(g2);

        // ripristina per overlay in pixel schermo
        g2.setTransform(old);

        if (gameOver) drawOverlay(g2, "HAI PERSO", "R = Retry, H = Home");
        else if (levelComplete) drawOverlay(g2, "LIVELLO COMPLETATO", "R = Replay, H = Home");
    }

    private void drawOverlay(Graphics g, String title, String subtitle) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics fm = g2.getFontMetrics();
        int tx = (getWidth() - fm.stringWidth(title)) / 2;
        g2.drawString(title, tx, getHeight() / 2 - 20);

        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        FontMetrics fm2 = g2.getFontMetrics();
        int sx = (getWidth() - fm2.stringWidth(subtitle)) / 2;
        g2.drawString(subtitle, sx, getHeight() / 2 + 20);
    }

    // ====== INPUT ======
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        // Fireboy: frecce
        if (k == KeyEvent.VK_LEFT) fireboy.vx = -3;
        if (k == KeyEvent.VK_RIGHT) fireboy.vx = 3;
        if (k == KeyEvent.VK_UP) fireboy.jump();

        // Watergirl: WASD
        if (k == KeyEvent.VK_A) watergirl.vx = -3;
        if (k == KeyEvent.VK_D) watergirl.vx = 3;
        if (k == KeyEvent.VK_W) watergirl.jump();

        if (k == KeyEvent.VK_R) {
            if (gameOver || levelComplete) restart();
        }
        if (k == KeyEvent.VK_H) {
            if (gameOver || levelComplete) System.out.println("HOME (da collegare al tuo menu)");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT) fireboy.vx = 0;
        if (k == KeyEvent.VK_A || k == KeyEvent.VK_D) watergirl.vx = 0;
    }

    @Override public void keyTyped(KeyEvent e) {}

    private void restart() {
        JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);
        top.dispose();
        main(null);
    }

    // ====== MAIN ======
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Fireboy & Watergirl - Single Level");
            FireboyWatergirlLevel panel = new FireboyWatergirlLevel();

            panel.setPreferredSize(new Dimension(1000, 800)); // finestra comoda; la mappa si adatta
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setContentPane(panel);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setResizable(true);
            f.setVisible(true);
        });
    }

    // ====== CLASSES ======
    static class Player {
        int x, y, w = 20, h = 22;
        int vx = 0;
        double vy = 0;
        boolean onGround = false;
        final Color color;

        Player(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        void jump() {
            if (onGround) {
                vy = -8.5;
                onGround = false;
            }
        }

        Rectangle getRect() { return new Rectangle(x, y, w, h); }

        void update(FireboyWatergirlLevel world) {
            vy += 0.35;
            if (vy > 10) vy = 10;

            int nx = x + vx;
            if (!collides(world, nx, y)) x = nx;

            int ny = (int) (y + vy);
            if (!collides(world, x, ny)) {
                y = ny;
                onGround = false;
            } else {
                if (vy > 0) onGround = true;
                vy = 0;
            }
        }

        private boolean collides(FireboyWatergirlLevel w, int nx, int ny) {
            return w.isSolidAtPixel(nx + 1, ny + 1)
                    || w.isSolidAtPixel(nx + this.w - 2, ny + 1)
                    || w.isSolidAtPixel(nx + 1, ny + this.h - 2)
                    || w.isSolidAtPixel(nx + this.w - 2, ny + this.h - 2);
        }

        void draw(Graphics g) {
            g.setColor(color);
            g.fillRect(x, y, w, h);
        }
    }

    static abstract class Entity {
        int x, y, w, h;
        Entity(int x, int y, int w, int h) { this.x = x; this.y = y; this.w = w; this.h = h; }
        Rectangle rect() { return new Rectangle(x, y, w, h); }
        boolean intersects(Rectangle r) { return rect().intersects(r); }
        boolean contains(int px, int py) { return rect().contains(px, py); }
        abstract void draw(Graphics g);
    }

    static class Door extends Entity {
        boolean open = false;
        Door(int x, int y, int w, int h) { super(x, y, w, h); }

        @Override
        void draw(Graphics g) {
            if (open) {
                g.setColor(new Color(120, 220, 120, 120));
                g.fillRect(x, y, w, h);
            } else {
                g.setColor(new Color(180, 180, 255));
                g.fillRect(x, y, w, h);
            }
        }
    }

    static class Coin extends Entity {
        boolean collected = false;
        Coin(int x, int y, int w, int h) { super(x, y, w, h); }

        @Override
        void draw(Graphics g) {
            g.setColor(new Color(255, 230, 0));
            g.fillOval(x + 6, y + 6, w - 12, h - 12);
        }
    }

    static class Teleporter extends Entity {
        Teleporter(int x, int y, int w, int h) { super(x, y, w, h); }

        @Override
        void draw(Graphics g) {
            g.setColor(new Color(160, 80, 255));
            g.drawRect(x + 2, y + 2, w - 4, h - 4);
            g.drawLine(x + 2, y + 2, x + w - 2, y + h - 2);
            g.drawLine(x + w - 2, y + 2, x + 2, y + h - 2);
        }
    }

    static class ButtonPad extends Entity {
        ButtonPad(int x, int y, int w, int h) { super(x, y, w, h); }

        @Override
        void draw(Graphics g) {
            g.setColor(new Color(255, 80, 200));
            g.fillRect(x + 4, y + 10, w - 8, h - 12);
        }
    }

    static class MovingPlatform extends Entity {
        int startY;
        int targetDy = 0;
        double speed = 1.0;

        boolean isLeftSide = false;

        int prevX, prevY;

        MovingPlatform(int x, int y, int w, int h) {
            super(x, y, w, h);
            startY = y;
            prevX = x;
            prevY = y;
        }

        void setBalanceRole(boolean isLeft, int dyWhenActive, double speed) {
            this.isLeftSide = isLeft;
            this.targetDy = dyWhenActive;
            this.speed = speed;
        }

        void updateBalance(boolean active) {
            prevX = x;
            prevY = y;

            int desiredY = active ? (startY + targetDy) : startY;

            if (y < desiredY) y += (int) Math.ceil(speed);
            if (y > desiredY) y -= (int) Math.ceil(speed);

            if (Math.abs(y - desiredY) <= 1) y = desiredY;
        }

        int deltaX() { return x - prevX; }
        int deltaY() { return y - prevY; }

        @Override
        void draw(Graphics g) {
            g.setColor(new Color(220, 220, 220));
            g.fillRect(x, y, w, h);
        }
    }

    static class Boulder extends Entity {
        double vy = 0;
        boolean onGround = false;

        Boulder(int x, int y, int w, int h) { super(x, y, w, h); }

        void updatePhysics(FireboyWatergirlLevel world) {
            vy += 0.35;
            if (vy > 10) vy = 10;

            int ny = (int) (y + vy);

            if (!collides(world, x, ny)) {
                y = ny;
                onGround = false;
            } else {
                if (vy > 0) onGround = true;
                vy = 0;
            }
        }

        private boolean collides(FireboyWatergirlLevel w, int nx, int ny) {
            return w.isSolidAtPixel(nx + 1, ny + 1)
                    || w.isSolidAtPixel(nx + this.w - 2, ny + 1)
                    || w.isSolidAtPixel(nx + 1, ny + this.h - 2)
                    || w.isSolidAtPixel(nx + this.w - 2, ny + this.h - 2);
        }

        void tryPushBy(Player p, FireboyWatergirlLevel world) {
            Rectangle pr = p.getRect();
            Rectangle br = this.rect();
            if (!pr.intersects(br)) return;

            if (p.vx > 0 && pr.x + pr.width <= br.x + 6) {
                int nx = x + 2;
                if (!world.isSolidAtPixel(nx + w, y + 1) && !world.isSolidAtPixel(nx + w, y + h - 2)) {
                    x = nx;
                }
            } else if (p.vx < 0 && pr.x >= br.x + br.width - 6) {
                int nx = x - 2;
                if (!world.isSolidAtPixel(nx, y + 1) && !world.isSolidAtPixel(nx, y + h - 2)) {
                    x = nx;
                }
            }
        }

        @Override
        void draw(Graphics g) {
            g.setColor(new Color(140, 110, 90));
            g.fillRect(x, y, w, h);
        }
    }
}
