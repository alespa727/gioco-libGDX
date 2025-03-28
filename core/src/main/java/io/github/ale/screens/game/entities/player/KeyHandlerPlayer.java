package io.github.ale.screens.game.entities.player;

import com.badlogic.gdx.Gdx;

import io.github.ale.screens.settings.Settings;

public class KeyHandlerPlayer {

    public boolean su, giu, sinistra, destra, sprint, usa;

    public KeyHandlerPlayer() {

    }

    /**
     * tasti di input premuti
     */

    public void input(){
        su = Gdx.input.isKeyPressed(Settings.getPulsanti()[0]);
        giu = Gdx.input.isKeyPressed(Settings.getPulsanti()[2]);
        sinistra = Gdx.input.isKeyPressed(Settings.getPulsanti()[1]);
        destra = Gdx.input.isKeyPressed(Settings.getPulsanti()[3]);
        sprint = Gdx.input.isKeyPressed(Settings.getPulsanti()[4]);
        usa = Gdx.input.isKeyPressed(Settings.getPulsanti()[5]);
    }

}
