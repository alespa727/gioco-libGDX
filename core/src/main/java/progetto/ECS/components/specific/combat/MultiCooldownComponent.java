package progetto.ECS.components.specific.combat;

import progetto.ECS.components.base.Component;
import progetto.ECS.components.specific.base.Cooldown;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MultiCooldownComponent extends Component {
    Map<String, Cooldown> cooldowns = new HashMap<>();

    public MultiCooldownComponent(String[] type, Cooldown[] cooldowns) {
        for (int i = 0; i < type.length; i++) {
            this.cooldowns.put(type[i], cooldowns[i]);
        }
    }

    public MultiCooldownComponent() {
        cooldowns = new HashMap<>();
    }

    public void add(String string, Cooldown cooldown) {
        cooldowns.put(string, cooldown);
    }

    public Cooldown getCooldown(String type) {
        return cooldowns.get(type);
    }

    public Collection<Cooldown> getCooldownTypes() {
        return cooldowns.values();
    }
}
