package progetto.ECS.components.specific.base;

import com.badlogic.gdx.math.Vector2;

public class PositionComponent {
    private final Vector2 position;

    public PositionComponent(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }
}
