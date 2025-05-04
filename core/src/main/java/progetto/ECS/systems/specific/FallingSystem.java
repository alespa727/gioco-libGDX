package progetto.ECS.systems.specific;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.base.Cooldown;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.graphics.DespawnAnimationComponent;
import progetto.ECS.components.specific.movement.FallingComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.systems.base.IteratingSystem;

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
