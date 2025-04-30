package progetto.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import progetto.core.settings.model.ModelImpostazioni;

public class KeyHandler {

    // Posizione del mouse sullo schermo
    public static final Vector3 mouse = new Vector3(0, 0, 0);
    // Stati dei tasti direzionali e azioni
    public static boolean su, giu, sinistra, destra, sprint, usa;
    private static boolean usePressedLastFrame = false;

    /**
     * Legge lo stato attuale dei tasti premuti e del mouse
     */
    public static void input() {
        su = Gdx.input.isKeyPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("DIREZIONE NORD"));
        giu = Gdx.input.isKeyPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("DIREZIONE SUD"));
        sinistra = Gdx.input.isKeyPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("DIREZIONE OVEST"));
        destra = Gdx.input.isKeyPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("DIREZIONE EST"));
        sprint = Gdx.input.isKeyPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("CORRI"));

        // Gestione manuale del tasto "usa"
        boolean usaCurrentlyPressed = Gdx.input.isKeyPressed(ModelImpostazioni.getComandiModificabili().getHashMap().get("USA"));
        usa = usaCurrentlyPressed && !usePressedLastFrame; // true solo se premuto adesso e non nel frame precedente
        usePressedLastFrame = usaCurrentlyPressed; // aggiorna lo stato

        // Ottiene la posizione del mouse (in coordinate schermo)
        mouse.x = Gdx.input.getX();
        mouse.y = Gdx.input.getY();
    }
}

