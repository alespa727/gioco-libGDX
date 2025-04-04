package progetto.screens.gameplay;

import com.badlogic.gdx.physics.box2d.World;
import progetto.Game;
import progetto.screens.gameplay.manager.entity.EntityManager;
import progetto.screens.gameplay.manager.map.MapManager;

public class GameInfo {
    public Game game;
    public GameScreen screen;
    public World world;
    public EntityManager entityManager;
    public MapManager mapManager;

    public GameInfo() {}

    public GameInfo(Game game, GameScreen screen, World world, EntityManager entityManager, MapManager mapManager) {
        this.game = game;
        this.screen = screen;
        this.world = world;
        this.entityManager = entityManager;
        this.mapManager = mapManager;
    }
}
