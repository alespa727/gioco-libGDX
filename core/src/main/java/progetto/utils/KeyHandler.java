package progetto.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import progetto.Core;
import progetto.CoreConfig;

public class KeyHandler {

    // Stati dei tasti direzionali e azioni
    public static boolean su, giu, sinistra, destra, sprint, usa;
    public static boolean debug;

    private static boolean usePressedLastFrame = false;

    // Posizione del mouse sullo schermo
    public static final Vector3 mouse = new Vector3(0, 0, 0);

    /**
     * Legge lo stato attuale dei tasti premuti e del mouse
     */
    public static void input() {
        su = Gdx.input.isKeyPressed(CoreConfig.getDirezioneNord());
        giu = Gdx.input.isKeyPressed(CoreConfig.getDirezioneSud());
        sinistra = Gdx.input.isKeyPressed(CoreConfig.getDirezioneOvest());
        destra = Gdx.input.isKeyPressed(CoreConfig.getDirezioneEst());
        sprint = Gdx.input.isKeyPressed(CoreConfig.getCORRI());

        // Gestione manuale del tasto "usa"
        boolean usaCurrentlyPressed = Gdx.input.isKeyPressed(CoreConfig.getUSA());
        usa = usaCurrentlyPressed && !usePressedLastFrame; // true solo se premuto adesso e non nel frame precedente
        usePressedLastFrame = usaCurrentlyPressed; // aggiorna lo stato

        // Ottiene la posizione del mouse (in coordinate schermo)
        mouse.x = Gdx.input.getX();
        mouse.y = Gdx.input.getY();

        // Attiva il debug se CTRL sinistro è premuto
        debug = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
    }
}

