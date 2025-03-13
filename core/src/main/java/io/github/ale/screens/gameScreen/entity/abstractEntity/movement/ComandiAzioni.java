package io.github.ale.screens.gameScreen.entity.abstractEntity.movement;

import io.github.ale.screens.gameScreen.enums.Azioni;

public class ComandiAzioni {
    private float x, y;
    private Azioni azione;


    public ComandiAzioni(Azioni azione, float x, float y){
        this.azione = azione;
        this.x = x;
        this.y = y;
    }

    
    public Azioni getAzione() {
        return azione;
    }

    public void setAzione(Azioni azione) {
        this.azione = azione;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
