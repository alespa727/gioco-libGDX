package progetto.gameplay.world.events;

import com.badlogic.gdx.math.Vector2;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.entity.specific.base.Entity;
import progetto.entity.specific.specific.living.combat.enemy.Enemy;
import progetto.rendering.entity.EntityManager;

/**
 * Evento di spawn di un {@link Enemy}
 */
public class SpawnEnemyEvent extends MapEvent{
    private final EntityManager entityManager;
    private final String type;

    /**
     * Crea un evento di spawn di un {@link Enemy}
     * <p>
     * L'evento ha una posizione e un raggio. Viene subito creata la zona in cui può essere attivato.
     * </p>
     * @param position posizione centrale dell’evento
     * @param radius raggio entro cui l’evento può attivarsi
     * @param entityManager gestore entità
     * @param enemyType tipo di nemico
     */
    public SpawnEnemyEvent(Vector2 position, float radius, EntityManager entityManager, String enemyType) {
        super(position, radius);
        this.entityManager = entityManager;
        this.type = enemyType;
        this.trigger(null);
        this.destroy();
    }

    @Override
    public void update() {

    }

    @Override
    public void trigger(Entity entity) {
        entityManager.summon(EntityFactory.createEnemy(
            type,
            EntityConfigFactory.createEntityConfig(type, entityManager.getIdCount(), position.x,  position.y),
            entityManager,
            1.5f
        ));
    }
}
