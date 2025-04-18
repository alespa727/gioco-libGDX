package progetto.gameplay.world.events.specific;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.world.events.base.MapEvent;
import progetto.manager.world.MapManager;
import progetto.gameplay.player.Player;
import progetto.manager.input.KeyHandler;
import progetto.manager.input.TerminalCommand;

/**
 * Evento di cambio mappa tramite {@link MapManager}
 */
public class ChangeMapEvent extends MapEvent {
    // Gestore mappa
    private Entity lastEntity;
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
     * @param position posizione centrale dell’evento
     * @param radius raggio entro cui l’evento può attivarsi
     * @param mapManager gestore delle mappe
     * @param map numero della mappa
     * @param x coordinata x
     * @param y coordinata y
     */
    public ChangeMapEvent(Vector2 position, float radius, MapManager mapManager, int map, float x, float y) {
        super(position, radius);
        this.x = x;
        this.y = y;
        this.map = map;
        this.mapManager = mapManager;
    }

    @Override
    public void update() {
        if (isActive()) {
            if (KeyHandler.usa) {
                TerminalCommand.printMessage("Map " + MapManager.getMapIndex() + " changing to " + map + "..");
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
