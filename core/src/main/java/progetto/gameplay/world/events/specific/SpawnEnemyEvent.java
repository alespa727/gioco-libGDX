package progetto.gameplay.world.events.specific;

import com.badlogic.gdx.math.Vector2;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.combat.enemy.Enemy;
import progetto.gameplay.world.events.base.MapEvent;
import progetto.manager.entities.Engine;

/**
 * Evento di spawn di un {@link Enemy}
 */
public class SpawnEnemyEvent extends MapEvent {
    private final Engine engine;
    private final String type;

    /**
     * Crea un evento di spawn di un {@link Enemy}
     * <p>
     * L'evento ha una posizione e un raggio. Viene subito creata la zona in cui può essere attivato.
     * </p>
     * @param position posizione centrale dell’evento
     * @param radius raggio entro cui l’evento può attivarsi
     * @param engine gestore entità
     * @param enemyType tipo di nemico
     */
    public SpawnEnemyEvent(Vector2 position, float radius, Engine engine, String enemyType) {
        super(position, radius);
        this.engine = engine;
        this.type = enemyType;
        this.trigger(null);
        this.destroy();
    }

    @Override
    public void update() {

    }

    @Override
    public void trigger(Entity entity) {
        engine.summon(EntityFactory.createEnemy(
            type,
            EntityConfigFactory.createEntityConfig(type, engine.getIdCount(), position.x,  position.y),
            engine,
            1.5f
        ));
    }
}
