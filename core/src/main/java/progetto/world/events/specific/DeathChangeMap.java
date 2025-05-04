package progetto.world.events.specific;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import progetto.core.App;
import progetto.core.game.GameScreen;
import progetto.core.specific.FakeMainMenu;
import progetto.input.KeyHandler;
import progetto.world.map.MapManager;

public class DeathChangeMap extends ChangeMap {

    private App app;


    /**
     * Crea un evento di cambio mappa con morte (Finta) aggiunta
     * <p>
     * L'evento ha una posizione e un raggio. Viene subito creata la zona in cui può essere attivato.
     * </p>
     *
     * @param position   posizione centrale dell’evento
     * @param radius     raggio entro cui l’evento può attivarsi
     * @param mapManager gestore delle mappe
     * @param map        numero della mappa
     * @param x          coordinata x
     * @param y          coordinata y
     */
    public DeathChangeMap(Vector2 position, float radius, MapManager mapManager, int map, float x, float y, GameScreen screen) {
        super(position, radius, mapManager, map, x, y);
        this.app = screen.app;
    }

    @Override
    public void check() {
        if (KeyHandler.usa) {
            Gdx.app.postRunnable(() ->{
                mapManager.changeMap(map, x + 0.5f, y + 0.5f, true);
                new FakeMainMenu(app, "Sei morto", new Color(0.8f, 0.8f, 0.86f, 1));
            });

            isActive = false;
        }
    }
}
