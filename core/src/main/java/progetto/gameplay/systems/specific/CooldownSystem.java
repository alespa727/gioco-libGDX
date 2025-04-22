package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.base.Cooldown;
import progetto.gameplay.entities.components.specific.combat.MultiCooldownComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.System;

import java.util.Collection;

public class CooldownSystem extends System {
    @Override
    public void update(float delta, Array<Entity> list) {
        for (Entity e : list) {
            if (!e.components.contains(MultiCooldownComponent.class) || !e.components.get(MultiCooldownComponent.class).isAwake())
                continue;

            MultiCooldownComponent mc = e.components.get(MultiCooldownComponent.class);

            Collection<Cooldown> cooldowns = mc.getCooldownTypes();

            for (Cooldown c : cooldowns) {
                if (c.shouldAutoUpdate)
                    c.update(delta);
            }

        }
    }
}
