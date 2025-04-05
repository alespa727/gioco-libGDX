package progetto.gameplay.map.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import progetto.GameConfig;
import progetto.gameplay.entities.types.Player;
import progetto.gameplay.entities.types.entity.Entity;
import progetto.gameplay.manager.map.MapManager;

public class ChangeMapEvent extends MapEvent {
    // Gestore mappa
    private final MapManager mapManager;

    // Coordinate della prossima mappa
    private final float x;
    private final float y;

    // Prossima mappa
    private final int map;

    /**
     * Crea l'evento
     */
    public ChangeMapEvent(Vector2 position, float radius, MapManager mapManager, int map, float x, float y) {
        super(position, radius);
        this.x = x;
        this.y = y;
        this.map = map;
        this.mapManager = mapManager;
    }

    /**
     * Aggiorna l'evento
     */
    @Override
    public void update() {
        if (isActive()) {
            if (Gdx.input.isKeyJustPressed(GameConfig.getUSA())) {
                Gdx.app.postRunnable(() -> mapManager.changeMap(map, x + 0.5f, y + 0.5f));
                active = false;
            }
        }
    }

    /**
     * Attiva l'evento
     */
    @Override
    public void trigger(Entity entity) {
        if (entity instanceof Player) setActive(true);
    }
}
