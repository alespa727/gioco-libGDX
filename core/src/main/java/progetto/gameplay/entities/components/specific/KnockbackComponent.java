package progetto.gameplay.entities.components.specific;

import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.components.specific.base.Cooldown;

public class KnockbackComponent extends Component {
    private static final float KNOCKBACK_COOLDOWN_TIME = 0.22f;
    public final Cooldown cooldown = new Cooldown(KNOCKBACK_COOLDOWN_TIME);
    public Vector2 hit = new Vector2(0, 0);
}
