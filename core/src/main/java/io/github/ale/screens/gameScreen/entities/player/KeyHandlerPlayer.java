package io.github.ale.screens.gameScreen.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import io.github.ale.screens.settings.Settings;

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

    public void inputmodificato(){
        su = Gdx.input.isKeyPressed(Settings.getPulsanti()[0]);
        giu = Gdx.input.isKeyPressed(Settings.getPulsanti()[2]);
        sinistra = Gdx.input.isKeyPressed(Settings.getPulsanti()[1]);
        destra = Gdx.input.isKeyPressed(Settings.getPulsanti()[3]);
        sprint = Gdx.input.isKeyPressed(Settings.getPulsanti()[4]);
        usa = Gdx.input.isKeyPressed(Settings.getPulsanti()[5]);
    }

}
