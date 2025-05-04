package progetto.world.events.specific;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;
import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.base.Cooldown;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.general.AttributeComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.entities.specific.living.Humanoid;
import progetto.world.events.base.RectangleMapEvent;

public class DamageEvent extends RectangleMapEvent {

    private final ObjectSet<Humanoid> entities;
    private final Cooldown cooldown;
    private final ComponentFilter filter;

    /**
     * Crea un evento nella mappa che può essere attivato quando il player entra nel suo raggio.
     * <p>
     * L'evento ha una posizione e un raggio. Viene subito creata la zona in cui può essere attivato.
     * </p>
     *
     * @param position posizione centrale dell’evento
     */
    public DamageEvent(Vector2 position, float width, float height) {
        super(position, width, height);
        cooldown = new Cooldown(0.5f);
        entities = new ObjectSet<>();
        filter = ComponentFilter.all(PhysicsComponent.class, AttributeComponent.class);
        setActive(true);
    }

    /**
     * Aggiorna l'evento
     */
    @Override
    public void update(float delta) {
        cooldown.update(delta);
        if (cooldown.isReady){
            for (Humanoid e : entities) {
                if(e.get(PhysicsComponent.class).getBody().getLinearVelocity().len() < 5f){
                    e.inflictDamage(20);
                }
            }
            cooldown.reset();
        }
    }

    @Override
    public void trigger(Entity entity) {
        if (entity instanceof Humanoid human && filter.matches(entity)) {
            if (entities.contains(human)) {
                entities.remove(human);
            }else{
                entities.add(human);
            }
        }
    }
}
