package progetto.gameplay.entity.types.living.combat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.entity.components.warrior.DirectionalRange;
import progetto.gameplay.entity.components.warrior.KnockbackComponent;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.gameplay.entity.types.living.HumanoidInstances;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.manager.ManagerEntity;

public abstract class Warrior extends Humanoid {

    // === COSTANTI ===

    // === ATTRIBUTI DI COMBATTIMENTO ===
    private Vector2 hitDirection = new Vector2();

    public Warrior(HumanoidInstances instance, ManagerEntity managerEntity) {
        super(instance, managerEntity);
        initCombatState();
        addComponent(new KnockbackComponent(this));
        addComponent(new DirectionalRange(this));
    }

    public Warrior(EntityConfig config, ManagerEntity manager) {
        super(config, manager);
        initCombatState();
        addComponent(new KnockbackComponent(this));
        addComponent(new DirectionalRange(this));
    }

    public float getRangeRadius() {
        return getComponent(DirectionalRange.class).getRangeRadius();
    }

    public Body getDirectionalRange() {
        return getComponent(DirectionalRange.class).getDirectionalRange();
    }


    public void setDirectionalRange(Body directionalRange) {
        getComponent(DirectionalRange.class).setDirectionalRange(directionalRange);
    }

    public Vector2 getHitDirection() {
        return hitDirection;
    }

    private void initCombatState() {
        hitDirection.setZero();
    }

    // === RANGE ===

    public DirectionalRange getDirectionRangeComponent() {
        return getComponent(DirectionalRange.class);
    }

    // === COMBATTIMENTO ===
    public void hit(Entity entity, float damage, float hitForce) {
        Body bodyThatHits = entity.getPhysics().getBody();
        Body bodyToHit = getPhysics().getBody();
        if (!getHumanStates().isInvulnerable()){
            hitDirection = new Vector2(bodyThatHits.getPosition()).sub(bodyToHit.getPosition()).nor().scl(-1*hitForce);
            inflictDamage(damage);
            getComponent(KnockbackComponent.class).reset();
        }
    }
}
