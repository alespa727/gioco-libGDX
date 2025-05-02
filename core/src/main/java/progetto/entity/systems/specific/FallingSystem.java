package progetto.entity.systems.specific;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import progetto.entity.Engine;
import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.base.Cooldown;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.graphics.DespawnAnimationComponent;
import progetto.entity.components.specific.movement.FallingComponent;
import progetto.entity.entities.Entity;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.entity.systems.base.IteratingSystem;

public class FallingSystem extends IteratingSystem {

    public FallingSystem() {
        super(ComponentFilter.all(FallingComponent.class, PhysicsComponent.class));
    }

    /**
     * Metodo da implementare: contiene la logica per elaborare ogni entità gestita dal sistema.
     *
     * @param entity entità da processare
     * @param delta  tempo passato dal frame precedente
     */
    @Override
    public void processEntity(Entity entity, float delta) {
        Cooldown cooldown = entity.get(FallingComponent.class).getCooldown();
        if (!entity.contains(DespawnAnimationComponent.class)) {
            entity.add(new DespawnAnimationComponent(cooldown.getMaxTime()));
        }
        cooldown.update(delta);
        Body body = entity.get(PhysicsComponent.class).getBody();
        body.applyLinearImpulse(new Vector2(0, -20f), body.getWorldCenter(), true);
        if (cooldown.isReady){
            removeEntity(entity);
            entity.remove(FallingComponent.class);
            entity.unregister();
        }
    }
}
