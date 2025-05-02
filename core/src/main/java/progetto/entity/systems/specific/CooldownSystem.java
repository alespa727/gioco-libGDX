package progetto.entity.systems.specific;

import progetto.entity.components.base.ComponentFilter;
import progetto.entity.components.specific.base.Cooldown;
import progetto.entity.components.specific.combat.MultiCooldownComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.systems.base.IteratingSystem;

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
