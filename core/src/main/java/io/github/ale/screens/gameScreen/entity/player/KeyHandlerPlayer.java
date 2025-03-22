package io.github.ale.screens.gameScreen.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyHandlerPlayer {

    public boolean su, giu, sinistra, destra, sprint, usa;

    public KeyHandlerPlayer() {

    }

    /**
     * tasti di input premuti
     */

    public void input() {
        su = Gdx.input.isKeyPressed(Input.Keys.W);
        giu = Gdx.input.isKeyPressed(Input.Keys.S);
        sinistra = Gdx.input.isKeyPressed(Input.Keys.A);
        destra = Gdx.input.isKeyPressed(Input.Keys.D);
        sprint = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
        usa = Gdx.input.isKeyPressed(Input.Keys.E);
    }

}
