package io.github.ale.entity.abstractEnity.state;

public class EntityState {
    private boolean isAlive;
    private boolean inCollisione;
    private boolean isMoving;

    public boolean isAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isInCollisione() {
        return inCollisione;
    }

    public void setInCollisione(boolean inCollisione) {
        this.inCollisione = inCollisione;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }
}
