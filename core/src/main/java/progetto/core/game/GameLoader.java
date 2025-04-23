package progetto.core.game;

import com.badlogic.gdx.physics.box2d.Box2D;
import progetto.world.WorldManager;

public class GameLoader {
    public static void loadWorld() {
        Box2D.init();
        WorldManager.init();
    }
}
