package progetto.ECS.components.specific.movement;

import com.badlogic.gdx.math.Vector2;
import progetto.ECS.components.base.Component;

public class DirectionComponent extends Component {
    public final Vector2 direction = new Vector2(0, -1);
}
