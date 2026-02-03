package model.entities.api;

import model.entities.commons.Entity;

/**
 * Interface defining the enemies behaviour.
 */
public interface Enemy extends Entity {

    /**
     * @return the type of the enemy.
     */
    EnemyType getType();
  
}
