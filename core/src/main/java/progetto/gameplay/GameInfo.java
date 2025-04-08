package progetto.gameplay;

import progetto.Core;
import progetto.gameplay.entity.behaviors.EntityManager;
import progetto.gameplay.entity.behaviors.manager.map.MapManager;

public class GameInfo {
    public Core game;
    public Game screen;
    public EntityManager entityManager;
    public MapManager mapManager;

    public GameInfo() {}

    public GameInfo(Core game, Game screen, EntityManager entityManager, MapManager mapManager) {
        this.game = game;
        this.screen = screen;
        this.entityManager = entityManager;
        this.mapManager = mapManager;
    }
}
