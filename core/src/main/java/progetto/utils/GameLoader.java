package progetto.utils;

import com.badlogic.gdx.physics.box2d.Box2D;
import progetto.gameplay.manager.ManagerWorld;

public class GameLoader {
    public static void loadWorld(){
        Box2D.init();
        ManagerWorld.init();
    }
}
