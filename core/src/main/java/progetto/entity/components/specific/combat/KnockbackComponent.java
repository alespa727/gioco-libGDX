package progetto.entity.components.specific.combat;

import com.badlogic.gdx.math.Vector2;
import progetto.entity.components.base.Component;
import progetto.entity.components.specific.base.Cooldown;

public class KnockbackComponent extends Component {
    private static final float KNOCKBACK_COOLDOWN_TIME = 0.22f;
    public final Cooldown cooldown = new Cooldown(KNOCKBACK_COOLDOWN_TIME);
    public Vector2 hit = new Vector2(0, 0);
}
