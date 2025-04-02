package io.github.ale.screens.gameplay;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;
import io.github.ale.Game;
import io.github.ale.screens.gameplay.manager.entity.EntityManager;
import io.github.ale.screens.gameplay.manager.map.MapManager;

public class GameContext {
    public Game game;
    public GameScreen screen;
    public World world;
    public EntityManager entityManager;
    public MapManager mapManager;

    public GameContext() {}

    public GameContext(Game game, GameScreen screen, World world, EntityManager entityManager, MapManager mapManager) {
        this.game = game;
        this.screen = screen;
        this.world = world;
        this.entityManager = entityManager;
        this.mapManager = mapManager;
    }
}
