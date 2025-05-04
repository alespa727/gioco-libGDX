package progetto.ECS.components.specific.movement;

import progetto.ECS.components.base.Component;
import progetto.ECS.components.specific.base.Cooldown;

public class FallingComponent extends Component {
    private final Cooldown cooldown = new Cooldown(0.5f);
    private boolean falling = false;

    public Cooldown getCooldown() {
        if (!falling) {
            cooldown.reset();
            falling = true;
        }
        return cooldown;
    }
}
