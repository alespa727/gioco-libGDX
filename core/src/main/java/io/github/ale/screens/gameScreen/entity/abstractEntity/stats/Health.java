package io.github.ale.screens.gameScreen.entitytypes.abstractEntity.stats;

public class Health {
    private float hp;
    public Health(float hp){
        this.hp = hp;
    }

    public float getHp(){
        return hp;
    }

    public void setHp(float hp){
        this.hp = hp;
    }
}
