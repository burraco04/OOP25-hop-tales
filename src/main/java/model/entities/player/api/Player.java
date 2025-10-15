package model.entities.player.api;

public interface Player {
    void setLeftPressed(boolean pressed);

    void setRightPressed(boolean pressed);

    void queueJump();

    void update(double deltaSeconds);

    PlayerSnapshot snapshot();
}
