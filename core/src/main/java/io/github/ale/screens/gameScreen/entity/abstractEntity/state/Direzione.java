package io.github.ale.screens.gameScreen.entity.abstractEntity.state;

import com.badlogic.gdx.math.Vector2;

public class Direzione {
    Vector2 direzione;
    Vector2 last;

    public Direzione(){
        direzione = new Vector2(0, 0);
        last = new Vector2(0, 0);
    }

    public void setDirezione(Vector2 direzione){
        if (!this.direzione.equals(direzione)) {
            last.set(this.direzione);
        }
        this.direzione=direzione;
    }

    public void setLastDirezione(Vector2 direzione){
        direzione.set(0, 0);
        last.set(direzione);
    }
    
    public Vector2 getDirezione(){
        return direzione;
    }
}
