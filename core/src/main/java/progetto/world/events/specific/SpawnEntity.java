package progetto.world.events.specific;

import com.badlogic.gdx.math.Vector2;
import progetto.entity.Engine;
import progetto.entity.entities.Entity;
import progetto.entity.entities.specific.EntityConfig;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.world.events.base.MapEvent;

public class SpawnEntity extends MapEvent {

    private final Engine engine;

    public SpawnEntity(Engine engine, String entityType, Vector2 position, float radius) {
        super(position, radius);
        this.engine = engine;

        EntityConfig config = EntityConfigFactory.createEntityConfig(entityType, position.x, position.y);
        Entity entity = EntityFactory.createEntity(entityType, config, engine);

        trigger(entity);
    }

    @Override
    public void update(float delta) {
        // non serve
    }

    /**
     * Attiva l'evento
     *
     * @param entity entità che lo ha triggerato
     *               <p>
     *               Gestire l'evento a seconda dell'entità
     *               </p>
     */
    @Override
    public void trigger(Entity entity) {
        engine.summon(entity);
        destroy();
    }
}
