package progetto.gameplay.entities.components.specific;

import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.components.specific.base.Cooldown;

public class StatusComponet extends Component {
    public boolean hasBeenHit = false;
    public boolean invulnerable = false;

    public final Cooldown cooldown;

    public StatusComponet() {
        this.cooldown = new Cooldown(.273f);
        this.cooldown.reset();
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

}
