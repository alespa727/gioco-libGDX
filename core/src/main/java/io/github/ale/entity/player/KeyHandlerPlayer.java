package io.github.ale.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyHandlerPlayer {

    boolean w, s, a, d, shift;

    public KeyHandlerPlayer() {

    }

    public void input() {
        
        
        w = Gdx.input.isKeyPressed(Input.Keys.W);
        s = Gdx.input.isKeyPressed(Input.Keys.S);
        a = Gdx.input.isKeyPressed(Input.Keys.A);
        d = Gdx.input.isKeyPressed(Input.Keys.D);
        shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
        
    }

}
