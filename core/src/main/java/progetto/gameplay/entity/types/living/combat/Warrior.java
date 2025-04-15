package progetto.gameplay.entity.types.living.combat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.entity.components.warrior.DirectionalRangeComponent;
import progetto.gameplay.entity.components.warrior.HitDirectionComponent;
import progetto.gameplay.entity.components.warrior.KnockbackComponent;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.manager.ManagerEntity;

public abstract class Warrior extends Humanoid {

    public Warrior(HumanoidInstances instance, ManagerEntity managerEntity) {
        super(instance, managerEntity);
        addComponent(new DirectionalRangeComponent(this));
        addComponent(new HitDirectionComponent(new Vector2(0, 0)));
        addComponent(new KnockbackComponent(this));
    }

    public Warrior(EntityConfig config, ManagerEntity manager) {
        super(config, manager);
        addComponent(new DirectionalRangeComponent(this));
        addComponent(new HitDirectionComponent(new Vector2(0, 0)));
        addComponent(new KnockbackComponent(this));
    }

    @Override
    public void create() {
        super.create();
        initCombatState();
    }

    public float getRangeRadius() {
        return getComponent(DirectionalRangeComponent.class).getRangeRadius();
    }

    public Body getDirectionalRange() {
        return getComponent(DirectionalRangeComponent.class).getDirectionalRange();
    }

    public void setDirectionalRange(Body directionalRange) {
        getComponent(DirectionalRangeComponent.class).setDirectionalRange(directionalRange);
    }

    public Vector2 getHitDirection() {
        return getComponent(HitDirectionComponent.class).getDirection();
    }

    private void initCombatState() {
        getHitDirection().setZero();
    }

    // === RANGE ===

    public DirectionalRangeComponent getDirectionRangeComponent() {
        return getComponent(DirectionalRangeComponent.class);
    }

    // === COMBATTIMENTO ===
    public void hit(Entity entity, float damage, float hitForce) {
        Body bodyThatHits = entity.getPhysics().getBody();
        Body bodyToHit = getPhysics().getBody();
        if (!getHumanStates().isInvulnerable()){
            getHitDirection().set(new Vector2(bodyThatHits.getPosition()).sub(bodyToHit.getPosition()).nor().scl(-1*hitForce));
            inflictDamage(damage);
            getComponent(KnockbackComponent.class).reset();
        }
    }
}
