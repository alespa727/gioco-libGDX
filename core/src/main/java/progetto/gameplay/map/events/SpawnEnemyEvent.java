package progetto.gameplay.map.events;

import com.badlogic.gdx.math.Vector2;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.living.combat.enemy.Enemy;
import progetto.gameplay.manager.ManagerEntity;

/**
 * Evento di spawn di un {@link Enemy}
 */
public class SpawnEnemyEvent extends MapEvent{
    private final ManagerEntity managerEntity;
    private final String type;

    /**
     * Crea un evento di spawn di un {@link Enemy}
     * <p>
     * L'evento ha una posizione e un raggio. Viene subito creata la zona in cui può essere attivato.
     * </p>
     * @param position posizione centrale dell’evento
     * @param radius raggio entro cui l’evento può attivarsi
     * @param managerEntity gestore entità
     * @param enemyType tipo di nemico
     */
    public SpawnEnemyEvent(Vector2 position, float radius, ManagerEntity managerEntity, String enemyType) {
        super(position, radius);
        this.managerEntity = managerEntity;
        this.type = enemyType;
        this.trigger(null);
        this.destroy();
    }

    @Override
    public void update() {

    }

    @Override
    public void trigger(Entity entity) {
        managerEntity.summon(EntityFactory.createEnemy(
            type,
            EntityConfigFactory.createEntityConfig(type, position.x,  position.y),
            managerEntity,
            1.5f
        ));
    }
}
