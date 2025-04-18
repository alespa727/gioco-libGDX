package progetto.gameplay.entities.components.specific.entity;

import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entities.components.base.Component;

public class DirectionComponent extends Component {
    public final Vector2 direction = new Vector2(0, -1);

    public DirectionComponent(){}

    public DirectionComponent(Vector2 direction) {
        direction.set(direction.cpy());
    }

    public Vector2 getDirection() {
        return direction;
    }
}
