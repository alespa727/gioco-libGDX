package io.github.ale.screens.game.map.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import io.github.ale.screens.game.entities.player.Player;
import io.github.ale.screens.game.entityType.entity.Entity;
import io.github.ale.screens.game.map.MapManager;

public class ChangeMapEvent extends MapEvent {
    private final MapManager mapManager;
    private float x, y;
    private final int map;

    public ChangeMapEvent(Vector2 position, float radius, World world, MapManager mapManager, int map) {
        super(position, radius, world);
        this.map = map;
        this.mapManager = mapManager;
    }

    @Override
    public void trigger(Entity entity) {
        if (entity instanceof Player) {
            if(entity.stati().isAlive() && Gdx.input.isKeyPressed(Input.Keys.E)) {
                Gdx.app.postRunnable(() -> mapManager.changeMap(map));
            }

        }
    }
}
