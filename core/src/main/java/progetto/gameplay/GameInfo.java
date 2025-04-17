package progetto.gameplay;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.Core;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.gameplay.manager.MapManager;

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
