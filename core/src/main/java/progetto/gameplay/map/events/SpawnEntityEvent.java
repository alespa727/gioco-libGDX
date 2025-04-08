package progetto.gameplay.map.events;

import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entity.factories.EntityConfigFactory;
import progetto.gameplay.entity.factories.EntityFactory;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.behaviors.EntityManager;

public class SpawnEntityEvent extends MapEvent{
    private EntityManager entityManager;
    private String type;

    /**
     * Crea l'evento fisicamente
     *
     * @param position
     * @param radius
     */
    public SpawnEntityEvent(Vector2 position, float radius, EntityManager entityManager, String entityType) {
        super(position, radius);
        this.entityManager = entityManager;
        this.type = entityType;
        this.trigger(null);
        this.despawn();
    }


    @Override
    public void update() {

    }

    @Override
    public void trigger(Entity entity) {
        entityManager.summon(EntityFactory.createEnemy(
            type,
            EntityConfigFactory.createEntityConfig(type, position.x,  position.y),
            entityManager,
            1.5f
        ));
    }
}
