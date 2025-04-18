package progetto.core.game;

import progetto.core.Core;
import progetto.manager.entities.EntityManager;
import progetto.manager.world.MapManager;

public class GameInfo {
    public Core core;
    public GameScreen screen;
    public EntityManager entityManager;
    public MapManager mapManager;

    public GameInfo() {}

    public GameInfo(Core core, GameScreen screen, EntityManager entityManager, MapManager mapManager) {
        this.core = core;
        this.screen = screen;
        this.entityManager = entityManager;
        this.mapManager = mapManager;
    }
}
