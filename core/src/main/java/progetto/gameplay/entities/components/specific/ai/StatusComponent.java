package progetto.gameplay.entities.components.specific.ai;

import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.components.specific.base.Cooldown;

public class StatusComponent extends Component {
    public boolean hasBeenHit = false;
    public boolean invulnerable = false;

    public final Cooldown cooldown = new Cooldown(.273f);

    public StatusComponent() {
        this.cooldown.reset();
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

}
