package progetto.world.events.specific;

import com.badlogic.gdx.math.Vector2;
import progetto.ECS.EntityEngine;
import progetto.ECS.entities.Entity;
import progetto.ECS.entities.specific.living.combat.enemy.BaseEnemy;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.world.events.base.MapEvent;

/**
 * Evento di spawn di un {@link BaseEnemy}
 */
public class SpawnEnemyEvent extends MapEvent {
    private final EntityEngine entityEngine;
    private final String type;

    /**
     * Crea un evento di spawn di un {@link BaseEnemy}
     * <p>
     * L'evento ha una posizione e un raggio. Viene subito creata la zona in cui può essere attivato.
     * </p>
     *
     * @param position  posizione centrale dell’evento
     * @param radius    raggio entro cui l’evento può attivarsi
     * @param entityEngine    gestore entità
     * @param enemyType tipo di nemico
     */
    public SpawnEnemyEvent(Vector2 position, float radius, EntityEngine entityEngine, String enemyType) {
        super(position, radius);
        this.entityEngine = entityEngine;
        this.type = enemyType;
        this.trigger(null);
        this.destroy();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void trigger(Entity entity) {
        entityEngine.summon(EntityFactory.createEntity(
            type,
            EntityConfigFactory.createEntityConfig(type, position.x, position.y),
                entityEngine
        ));
    }
}
