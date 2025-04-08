package progetto.gameplay.map.events;

import com.badlogic.gdx.math.Vector2;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.manager.ManagerEntity;

public class SpawnEnemyEvent  extends MapEvent{
    private ManagerEntity managerEntity;
    private String type;

    /**
     * Crea l'evento fisicamente
     *
     * @param position
     * @param radius
     */
    public SpawnEnemyEvent(Vector2 position, float radius, ManagerEntity managerEntity, String entityType) {
        super(position, radius);
        this.managerEntity = managerEntity;
        this.type = entityType;
        this.trigger(null);
        this.despawn();
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
