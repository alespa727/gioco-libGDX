package progetto.gameplay.map.events;

import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entity.factories.EntityConfigFactory;
import progetto.gameplay.entity.factories.EntityFactory;
import progetto.gameplay.entity.types.abstractEntity.Entity;
import progetto.gameplay.manager.entity.EntityManager;

public class SpawnEntityEvent extends MapEvent{
    private EntityManager entityManager;

    /**
     * Crea l'evento fisicamente
     *
     * @param position
     * @param radius
     */
    public SpawnEntityEvent(Vector2 position, float radius, EntityManager entityManager) {
        super(position, radius);
        this.entityManager = entityManager;
        trigger(null);
    }


    @Override
    public void update() {

    }

    @Override
    public void trigger(Entity entity) {
        entityManager.summon(EntityFactory.createEnemy(
            "Finn",
            EntityConfigFactory.createEntityConfig("Finn", 15, 15),
            entityManager,
            1.5f
        ));
    }
}
