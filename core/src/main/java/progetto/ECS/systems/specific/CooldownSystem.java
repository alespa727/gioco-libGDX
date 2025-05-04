package progetto.ECS.systems.specific;

import progetto.ECS.components.base.ComponentFilter;
import progetto.ECS.components.specific.base.Cooldown;
import progetto.ECS.components.specific.combat.MultiCooldownComponent;
import progetto.ECS.entities.Entity;
import progetto.ECS.systems.base.IteratingSystem;

import java.util.Collection;

public class CooldownSystem extends IteratingSystem {

    public CooldownSystem() {
        super(ComponentFilter.all(MultiCooldownComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float delta) {
        if (!entity.components.contains(MultiCooldownComponent.class) || !entity.components.get(MultiCooldownComponent.class).isAwake())
            return;

        MultiCooldownComponent mc = entity.components.get(MultiCooldownComponent.class);

        Collection<Cooldown> cooldowns = mc.getCooldownTypes();

        for (Cooldown c : cooldowns) {
            if (c.shouldAutoUpdate)
                c.update(delta);
        }
    }
}
