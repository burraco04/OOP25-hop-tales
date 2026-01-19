package model.entities.api;

import model.entities.commons.Entity;

public interface Enemy extends Entity{
    /**
     * @return a snapshot of the enemy state.
     */
    EnemySnapshot getSnapshot();    
}
