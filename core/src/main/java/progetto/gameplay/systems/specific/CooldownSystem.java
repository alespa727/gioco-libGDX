package progetto.gameplay.systems.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.specific.base.Cooldown;
import progetto.gameplay.entities.components.specific.combat.MultiCooldownComponent;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.AutomaticSystem;

import java.util.Collection;

public class CooldownSystem extends AutomaticSystem {

    public CooldownSystem() {
        super(Array.with(new MultiCooldownComponent()));
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
