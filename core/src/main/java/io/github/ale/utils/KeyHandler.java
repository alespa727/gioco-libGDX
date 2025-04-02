package io.github.ale.utils;

import com.badlogic.gdx.Gdx;
import io.github.ale.GameConfig;

public class KeyHandler {

    public static boolean su, giu, sinistra, destra, sprint, usa;

    /**
     * tasti di input premuti
     */

    public static void input() {
        su = Gdx.input.isKeyPressed(GameConfig.getDirezioneNord());
        giu = Gdx.input.isKeyPressed(GameConfig.getDirezioneSud());
        sinistra = Gdx.input.isKeyPressed(GameConfig.getDirezioneOvest());
        destra = Gdx.input.isKeyPressed(GameConfig.getDirezioneEst());
        sprint = Gdx.input.isKeyPressed(GameConfig.getCORRI());
        usa = Gdx.input.isKeyPressed(GameConfig.getUSA());
    }

}
