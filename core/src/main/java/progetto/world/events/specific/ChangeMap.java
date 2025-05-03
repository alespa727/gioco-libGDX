package progetto.world.events.specific;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import progetto.entity.entities.Entity;
import progetto.input.KeyHandler;
import progetto.core.game.Terminal;
import progetto.core.game.player.Player;
import progetto.world.events.base.MapEvent;
import progetto.world.map.MapManager;

/**
 * Evento di cambio mappa tramite {@link MapManager}
 */
public class ChangeMap extends MapEvent {
    private final MapManager mapManager;
    // Coordinate della prossima mappa
    private final float x;
    private final float y;
    // Prossima mappa
    private final int map;

    /**
     * Crea un evento di cambio mappa
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
    public ChangeMap(Vector2 position, float radius, MapManager mapManager, int map, float x, float y) {
        super(position, radius);
        this.x = x;
        this.y = y;
        this.map = map;
        this.mapManager = mapManager;
    }

    @Override
    public void update(float delta) {
        if (isActive()) {
            if (KeyHandler.usa) {
                Terminal.printMessage("Map " + MapManager.getMapIndex() + " changing to " + map + "..");
                Gdx.app.postRunnable(() -> mapManager.changeMap(map, x + 0.5f, y + 0.5f));
                isActive = false;
            }
        }
    }

    @Override
    public void trigger(Entity entity) {
        if (entity instanceof Player) setActive(true);
    }
}
