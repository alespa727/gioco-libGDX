package progetto.entity.components.specific.base;

import progetto.entity.components.base.Component;

public class StatusComponent extends Component {
    public final Cooldown cooldown = new Cooldown(.273f);
    public boolean hasBeenHit = false;
    public boolean invulnerable = false;

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
