package progetto.world.events.specific;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.movement.FallingComponent;
import progetto.entity.entities.Entity;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.world.events.base.RectangleMapEvent;

public class FallEvent extends RectangleMapEvent {

    private ObjectSet<Entity> entities;

    public FallEvent(Vector2 position, float width, float height) {
        super(position, width, height);
        entities = new ObjectSet<>();
    }

    /**
     * Aggiorna l'evento
     * @param delta
     */
    @Override
    public void update(float delta) {

        for (Entity e : entities) {
            if (!e.contains(FallingComponent.class) && e.contains(PhysicsComponent.class)) {
                if (e.get(PhysicsComponent.class).getBody().getLinearVelocity().len() < 5f){
                    e.add(new FallingComponent());
                }
            }
        }

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
        if (entities.contains(entity)) {
            entities.remove(entity);
        }else{
            entities.add(entity);
        }
    }
}
