package progetto.gameplay.entity.components;

import com.badlogic.gdx.math.Vector2;

public class DirectionComponent {
    public final Vector2 direction = new Vector2(0, -1);

    public DirectionComponent(){}

    public DirectionComponent(Vector2 direction) {
        direction.set(direction.cpy());
    }

    public Vector2 getDirection() {
        return direction;
    }
}
