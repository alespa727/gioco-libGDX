package progetto.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import progetto.CoreConfig;

public class KeyHandler {

    public static boolean su, giu, sinistra, destra, sprint, usa;

    public static boolean debug;

    public static final Vector3 mouse = new Vector3(0, 0, 0);

    /**
     * tasti di input premuti
     */
    public static void input() {
        su = Gdx.input.isKeyPressed(CoreConfig.getDirezioneNord());
        giu = Gdx.input.isKeyPressed(CoreConfig.getDirezioneSud());
        sinistra = Gdx.input.isKeyPressed(CoreConfig.getDirezioneOvest());
        destra = Gdx.input.isKeyPressed(CoreConfig.getDirezioneEst());
        sprint = Gdx.input.isKeyPressed(CoreConfig.getCORRI());
        usa = Gdx.input.isKeyPressed(CoreConfig.getUSA());
        mouse.x = Gdx.input.getX();
        mouse.y = Gdx.input.getY();

        debug = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
    }



}
