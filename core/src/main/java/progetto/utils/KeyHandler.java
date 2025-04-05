package progetto.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import progetto.GameConfig;

public class KeyHandler {

    public static boolean su, giu, sinistra, destra, sprint, usa;

    public static Vector3 mouse = new Vector3(0, 0, 0);
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
        mouse.x = Gdx.input.getX();
        mouse.y = Gdx.input.getY();
    }



}
