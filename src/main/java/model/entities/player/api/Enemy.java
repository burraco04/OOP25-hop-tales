package model.entities.player.api;

import model.entities.player.commons.Entity;

public interface Enemy extends Entity{
    /**
     * @return a snapshot of the enemy state.
     */
    EnemySnapshot getSnapshot();    
}
