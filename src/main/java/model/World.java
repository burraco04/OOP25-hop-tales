package model;

import java.util.ArrayList;
import java.util.List;

import model.entities.api.Player;
import model.entities.impl.PlayerImpl;
import model.objects.CoinManager;
import model.objects.api.Tangible;

/**
 * create class world.
 */
public class World {
    private final List<Tangible> entities = new ArrayList<>();
    private final Player player;
    private final CoinManager coinManager;
    private final int levelWidth;

    public World() {
        this.player = new PlayerImpl(GameConstants.STARTING_POSITION_X, GameConstants.STARTING_POSITION_Y,
                                     GameConstants.PLAYER_WIDTH, GameConstants.PLAYER_HEIGHT);
        this.coinManager = new CoinManager();
        this.levelWidth = GameConstants.LEVEL_1_WIDTH;

    }
    //Serve anche il player per la camera

    /**
     * add all the entities at the world
     *
     * @param list list of entitties
     */
    public void addEntities(final List<Tangible> list) {
        entities.addAll(list);
    }

    /**
     * return the entities
     *
     * @return the entities
     */
    public List<Tangible> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }

    public CoinManager getCoinManager() {
        return coinManager;
    }

    public int getLevelWidth() {
        return levelWidth;
    }
}
