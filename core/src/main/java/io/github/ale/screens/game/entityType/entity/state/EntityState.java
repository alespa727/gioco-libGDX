package io.github.ale.screens.game.entityType.entity.state;
public class EntityState{
    private boolean isAlive;
    private boolean inCollisione;
    private boolean isMoving;
    private boolean immortality;

    public boolean isAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean inCollisione() {
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

    public boolean immortality(){
        return immortality;
    }

    public void setImmortality(boolean immortality){
        this.immortality = immortality;
    }


}
