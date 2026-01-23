package model;

import java.util.ArrayList;
import java.util.List;

import model.entities.api.Player;
import model.entities.impl.PlayerImpl;
import model.entity.Entity;
import model.objects.CoinManager;



public class World {
    private final List<Entity> entities = new ArrayList<>();
    private final Player player;
    private final CoinManager coinManager;
    private final int levelWidth;

    public World() {
        this.player = new PlayerImpl(GameConstants.STARTING_POSITION, GameConstants.STARTING_POSITION,
                                     GameConstants.PLAYER_WIDTH, GameConstants.PLAYER_HEIGHT);
        this.coinManager = new CoinManager();
        this.levelWidth = GameConstants.LEVEL_1_WIDTH;

    }
    //Serve anche il player per la camera

    public void addEntity(final Entity e) {
        entities.add(e);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }

    public CoinManager getCoinManager() {
        return coinManager;
    }

    // getter larghezza livello
    public int getLevelWidth() {
        return levelWidth;
    }
}
