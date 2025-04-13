package progetto.gameplay;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entity.types.EntityInstance;

public class GameInstance {
    Vector2 position;
    EntityInstance player;

    Array<EntityInstance> entities;
    int map;

}
