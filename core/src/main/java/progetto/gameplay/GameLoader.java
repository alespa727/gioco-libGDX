package progetto.gameplay;

import com.badlogic.gdx.physics.box2d.Box2D;
import progetto.gameplay.manager.WorldManager;

public class GameLoader {
    public static void loadWorld(){
        Box2D.init();
        WorldManager.init();
    }
}
