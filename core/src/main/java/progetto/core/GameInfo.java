package progetto.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.screens.GameScreen;
import progetto.rendering.entity.EntityManager;
import progetto.manager.world.MapManager;

public class GameInfo {
    public Core core;
    public GameScreen screen;
    public EntityManager entityManager;
    public MapManager mapManager;
    public SpriteBatch batch;

    public GameInfo() {}

    public GameInfo(Core core, GameScreen screen, EntityManager entityManager, MapManager mapManager) {
        this.core = core;
        this.screen = screen;
        this.entityManager = entityManager;
        this.mapManager = mapManager;
    }
}
