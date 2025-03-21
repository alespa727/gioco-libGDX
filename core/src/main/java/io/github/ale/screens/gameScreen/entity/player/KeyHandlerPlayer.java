package io.github.ale.screens.gameScreen.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyHandlerPlayer {

    public boolean w, s, a, d, shift, e, f, r, left_click;

    public KeyHandlerPlayer() {

    }

    /**
     * tasti di input premuti
     */

    public void input() {
        left_click = Gdx.input.isTouched(Input.Buttons.LEFT);
        r = Gdx.input.isKeyPressed(Input.Keys.R);
        f = Gdx.input.isKeyPressed(Input.Keys.F);
        w = Gdx.input.isKeyPressed(Input.Keys.W);
        s = Gdx.input.isKeyPressed(Input.Keys.S);
        a = Gdx.input.isKeyPressed(Input.Keys.A);
        d = Gdx.input.isKeyPressed(Input.Keys.D);
        shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
        
    }

}
