package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FireboyWatergirlLevel extends JPanel implements ActionListener, KeyListener {

    // ====== MAPPA ======
    private final String[] RAW_MAP = {
            "11111111111111111111111111111111111",
            "10000000000000100000000300000000001",
            "10000000440000100000000300000000001",
            "10000000110002100000011110000000001",
            "10040000000001133111100001111111111",
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
            "11000000011000000001111000000009991",
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
        Point spawnW = findSpawnBottomLeft();
        watergirl.x = spawnW.x * TILE;
        watergirl.y = spawnW.y * TILE;

        buildAssociations();
        timer.start();
    }

    private void loadMap() {
        boulders.clear();
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
                    //case '8' -> boulders.add(new Boulder(c * TILE, r * TILE, TILE, TILE));
                    case '2' -> buttons.add(new ButtonPad(c * TILE, r * TILE, TILE, TILE));
                    // case '9' -> NON creare tile singoli: li raggruppiamo dopo
                }
            }
        }

        // Crea piattaforme raggruppando i 9 vicini
        buildMovingPlatformsFromMap();
       buildBouldersFromMapFloodFill();


    }

    private void buildBouldersFromMapFloodFill() {
    boolean[][] vis = new boolean[rows][cols];

    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (map[r][c] != '8' || vis[r][c]) continue;

            // flood fill per prendere tutto il "blocco" connesso di 8
            ArrayDeque<Point> q = new ArrayDeque<>();
            q.add(new Point(c, r));
            vis[r][c] = true;

            int minR = r, maxR = r, minC = c, maxC = c;

            while (!q.isEmpty()) {
                Point p = q.poll();
                int cc = p.x, rr = p.y;

                minR = Math.min(minR, rr);
                maxR = Math.max(maxR, rr);
                minC = Math.min(minC, cc);
                maxC = Math.max(maxC, cc);

                int[][] dir = {{1,0},{-1,0},{0,1},{0,-1}};
                for (int[] d : dir) {
                    int nc = cc + d[0], nr = rr + d[1];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                    if (vis[nr][nc]) continue;
                    if (map[nr][nc] != '8') continue;
                    vis[nr][nc] = true;
                    q.add(new Point(nc, nr));
                }
            }

            int x = minC * TILE;
            int y = minR * TILE;
            int w = (maxC - minC + 1) * TILE;
            int h = (maxR - minR + 1) * TILE;

            boulders.add(new Boulder(x, y, w, h));
        }
    }

    // DEBUG: ti stampa quanti massi hai davvero
    System.out.println("BOULDERS = " + boulders.size());
}

private boolean isCrushedByBoulder(Player p, Boulder b) {
    Rectangle pr = p.getRect();
    Rectangle br = b.rect();

    // devono sovrapporsi in orizzontale
    boolean xOverlap = pr.x + pr.width > br.x && pr.x < br.x + br.width;
    if (!xOverlap) return false;

    // player deve essere sotto il masso (contatto quasi)
    int pTop = pr.y;
    int bBottom = br.y + br.height;

    // tolleranza contatto 2-3 px
    boolean bOnTopOfPlayer = (bBottom >= pTop - 2) && (bBottom <= pTop + 6);
    if (!bOnTopOfPlayer) return false;

    // e sopra la testa del player deve esserci qualcosa di solido (soffitto)
    // controlliamo 2 punti sopra la testa
    boolean ceiling =
            isSolidAtPixel(pr.x + 2, pr.y - 2) ||
            isSolidAtPixel(pr.x + pr.width - 3, pr.y - 2);

    return ceiling;
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

    private void openAndRemoveDoor(String doorId) {
    // rimuove le tile di quella porta sia dalla lista "doors" sia dalla mappa (3 -> 0)
    Iterator<Door> it = doors.iterator();
    while (it.hasNext()) {
        Door d = it.next();

        Point dTile = new Point(d.x / TILE, d.y / TILE); // col,row
        String id = doorPosToId.get(dTile);

        if (doorId.equals(id)) {
            // 1) rimuovi fisicamente la porta dalla MAPPA
            int r = d.y / TILE;
            int c = d.x / TILE;
            if (r >= 0 && r < rows && c >= 0 && c < cols) {
                map[r][c] = '0';
            }

            // 2) rimuovi l'oggetto door così "sparisce" del tutto
            it.remove();
        }
    }
}

private void removeDoorTilesFromMap(String doorId) {
    // Scorri TUTTE le tile che appartengono a quella porta e mettile a vuoto
    for (Map.Entry<Point, String> e : doorPosToId.entrySet()) {
        if (doorId.equals(e.getValue())) {
            Point t = e.getKey(); // t.x=col, t.y=row (0-based)
            int r = t.y;
            int c = t.x;
            if (r >= 0 && r < rows && c >= 0 && c < cols) {
                map[r][c] = '0';
            }
        }
    }

    // opzionale: pulisci anche la mappa di associazione (non è obbligatorio ma è pulito)
    doorPosToId.entrySet().removeIf(en -> doorId.equals(en.getValue()));
}

private boolean isPlayerOnPlatform(Player pl, MovingPlatform p) {
    Rectangle pr = pl.getRect();
    Rectangle plat = p.rect();

    int pBottom = pr.y + pr.height;
    boolean xOverlap = pr.x + pr.width > plat.x && pr.x < plat.x + plat.width;

    // tolleranza contatto 3px
    boolean onTop = (pBottom >= plat.y - 3) && (pBottom <= plat.y + 3);

    return xOverlap && onTop;
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
        addDoorTiles.accept("D1", colWithRows.apply(new int[]{8, 9}, 11));
        linkButton.accept(RC.apply(35, 25), "D1");

        addDoorTiles.accept("D2", rowWithCols.apply(10, new int[]{2, 3, 4, 5}));
        linkButton.accept(RC.apply(25, 30), "D2");

        addDoorTiles.accept("D3", rowWithCols.apply(20, new int[]{7, 8, 9, 10}));
        linkButton.accept(RC.apply(8, 19), "D3");

        addDoorTiles.accept("D4", colWithRows.apply(new int[]{34, 35}, 32));
        linkButton.accept(RC.apply(4, 14), "D4");

        addDoorTiles.accept("D5", colWithRows.apply(new int[]{3, 4}, 24));
        linkButton.accept(RC.apply(26, 4), "D5");

        addDoorTiles.accept("D6", rowWithCols.apply(5, new int[]{16, 17}));
        linkButton.accept(RC.apply(30, 6), "D6");

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

        // Boulder physics + push
        for (Boulder b : boulders) {
            b.tryPushBy(fireboy, this);
            b.tryPushBy(watergirl, this);
        }

        fireboy.update(this);
        watergirl.update(this);

        for (Boulder b : boulders) {
            b.updatePhysics(this);
        }

        // SCHIACCIAMENTO: se un masso cade su un player e sopra c'è un solido -> game over
        for (Boulder b : boulders) {
            if (b.vy > 0) { // solo se sta scendendo
                if (isCrushedByBoulder(fireboy, b) || isCrushedByBoulder(watergirl, b)) {
                    gameOver = true;
                    break;
                }
            }
        }


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

        // Trascina anche i player che stanno sopra le piattaforme
        for (MovingPlatform p : platforms) {
            int dx = p.deltaX();
            int dy = p.deltaY();
            if (dx == 0 && dy == 0) continue;

            if (isPlayerOnPlatform(fireboy, p)) {
                fireboy.x += dx;
                fireboy.y += dy;
                fireboy.vy = 0;
                fireboy.onGround = true;
            }

            if (isPlayerOnPlatform(watergirl, p)) {
                watergirl.x += dx;
                watergirl.y += dy;
                watergirl.vy = 0;
                watergirl.onGround = true;
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
    // versione standard (player, ecc.)
boolean isSolidAtPixel(int px, int py) {
    return isSolidAtPixel(px, py, null);
}

// versione con "ignore" (per massi, piattaforme, ecc.)
boolean isSolidAtPixel(int px, int py, Object ignore) {
    int c = px / TILE;
    int r = py / TILE;

    if (r < 0 || r >= rows || c < 0 || c >= cols) return true;

    char ch = map[r][c];

    // muri
    if (ch == '1') return true;

    // porte chiuse = solide
    if (ch == '3') {
        for (Door d : doors) {
            if (d != ignore && d.contains(px, py)) return true;
        }
    }

    // piattaforme mobili
    for (MovingPlatform p : platforms) {
        if (p != ignore && p.contains(px, py)) return true;
    }

    // massi
    for (Boulder b : boulders) {
        if (b != ignore && b.contains(px, py)) return true;
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
                // 1) svuota davvero la mappa (questa è la cosa che ti serve per far cadere il masso)
                removeDoorTilesFromMap(doorId);

                // 2) rimuovi anche gli oggetti Door dalla lista (così non si disegnano più)
                doors.removeIf(d -> {
                    Point dt = new Point(d.x / TILE, d.y / TILE);
                    String id = doorPosToId.get(dt);
                    return doorId.equals(id); // se hai già ripulito doorPosToId sopra, allora questa riga non becca niente
                });

                // quindi meglio: rimuovi per tile direttamente guardando la mappa (tile non più '3')
                doors.removeIf(d -> map[d.y / TILE][d.x / TILE] != '3');
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
    

    private Point findSpawnBottomLeft() {
    // cerco da BASSO verso ALTO, e da SINISTRA verso DESTRA
    // e voglio una tile "vuota" (0) con sotto qualcosa di solido (1)
    for (int r = rows - 2; r >= 0; r--) {          // -2 perché guardo anche r+1
        for (int c = 0; c < cols; c++) {

            char here = map[r][c];
            char below = map[r + 1][c];

            boolean emptyHere = (here == '0');     // spawn SOLO su vuoto
            boolean solidBelow = (below == '1');   // “pavimento” sotto

            // Evita di spawnare dentro roba pericolosa/speciale (lava, porte, teleporter, goal ecc.)
            boolean safe = here != '7' && here != '3' && here != '*' && here != '5' && here != '8' && here != '2' && here != '9';

            if (emptyHere && solidBelow && safe) {
                return new Point(c, r); // col, row
            }
        }
    }
    // fallback: se non trova niente, metto una posizione “ragionevole”
    return new Point(1, rows - 2);
}



    
}
