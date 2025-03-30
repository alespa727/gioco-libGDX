package io.github.ale.screens.game.entityType.entity.state;

import com.badlogic.gdx.math.Vector2;

public class Direzione {
    Vector2 direzione;

    public Direzione(){
        direzione = new Vector2(0, 0);
    }

    public void setDirezione(Vector2 direzione){
        this.direzione=direzione;
    }

    public void setDirezione(float x, float y){
        this.direzione.set(x, y);
    }

    public void setLastDirezione(Vector2 direzione){
        direzione.set(0, 0);
    }

    public Vector2 getDirezione(){
        return direzione;
    }
}
