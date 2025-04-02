package io.github.ale;

import com.badlogic.gdx.Gdx;

public class KeyHandler {

    public static boolean su, giu, sinistra, destra, sprint, usa;

    public KeyHandler() {

    }

    /**
     * tasti di input premuti
     */

    public static void input() {
        su = Gdx.input.isKeyPressed(ComandiGioco.getDirezioneNord());
        giu = Gdx.input.isKeyPressed(ComandiGioco.getDirezioneSud());
        sinistra = Gdx.input.isKeyPressed(ComandiGioco.getDirezioneOvest());
        destra = Gdx.input.isKeyPressed(ComandiGioco.getDirezioneEst());
        sprint = Gdx.input.isKeyPressed(ComandiGioco.getCORRI());
        usa = Gdx.input.isKeyPressed(ComandiGioco.getUSA());
    }

}
