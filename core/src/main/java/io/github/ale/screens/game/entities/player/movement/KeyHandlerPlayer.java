package io.github.ale.screens.game.entities.player.movement;

import com.badlogic.gdx.Gdx;

import io.github.ale.ComandiGioco;

public class KeyHandlerPlayer {

    public boolean su, giu, sinistra, destra, sprint, usa;

    public KeyHandlerPlayer() {

    }

    /**
     * tasti di input premuti
     */

    public void input(){
        su = Gdx.input.isKeyPressed(ComandiGioco.getDirezioneNord());
        giu = Gdx.input.isKeyPressed(ComandiGioco.getDirezioneSud());
        sinistra = Gdx.input.isKeyPressed(ComandiGioco.getDirezioneOvest());
        destra = Gdx.input.isKeyPressed(ComandiGioco.getDirezioneEst());
        sprint = Gdx.input.isKeyPressed(ComandiGioco.getCORRI());
        usa = Gdx.input.isKeyPressed(ComandiGioco.getUSA());
    }

}
