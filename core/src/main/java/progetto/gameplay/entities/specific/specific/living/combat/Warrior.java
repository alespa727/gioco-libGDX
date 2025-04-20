package progetto.gameplay.entities.specific.specific.living.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.components.specific.AttackRangeComponent;
import progetto.gameplay.entities.components.specific.KnockbackComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.entities.specific.specific.living.HumanoidInstances;
import progetto.manager.entities.Engine;

public abstract class Warrior extends Humanoid {
    public Texture weapon = new Texture(Gdx.files.internal("entities/Finn/attack/sword.png"));

    public Warrior(HumanoidInstances instance, Engine engine) {
        super(instance, engine);

        Component[] components = new Component[]{
            new AttackRangeComponent(),
            new KnockbackComponent()
        };

        componentManager.add(components);
    }

    public Warrior(EntityConfig config, Engine manager) {
        super(config, manager);

        Component[] components = new Component[]{
            new AttackRangeComponent(),
            new KnockbackComponent()
        };

        componentManager.add(components);
    }

    @Override
    public void create() {
        super.create();
    }

    public float getRangeRadius() {
        return  componentManager.get(AttackRangeComponent.class).getRangeRadius();
    }

    public Body getDirectionalRange() {
        return componentManager.get(AttackRangeComponent.class).getDirectionalRange();
    }

    public void setDirectionalRange(Body directionalRange) {
        componentManager.get(AttackRangeComponent.class).directionalRange = directionalRange;
    }

    // === RANGE ===

    public AttackRangeComponent getDirectionRangeComponent() {
        return componentManager.get(AttackRangeComponent.class);
    }

    // === COMBATTIMENTO ===
    public void hit(Entity entity, float damage, float hitForce) {
        Body bodyThatHits = entity.getPhysics().getBody();
        Body bodyToHit = getPhysics().getBody();
        if (!getHumanStates().isInvulnerable()){
            componentManager.get(KnockbackComponent.class).hit.set(new Vector2(bodyThatHits.getPosition()).sub(bodyToHit.getPosition()).nor().scl(-1*hitForce));
            inflictDamage(damage);
            componentManager.get(KnockbackComponent.class).cooldown.reset();
        }
    }
}
