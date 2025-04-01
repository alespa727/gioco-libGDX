package io.github.ale.screens.game.map.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import io.github.ale.ComandiGioco;
import io.github.ale.screens.game.entities.player.Player;
import io.github.ale.screens.game.entityType.entity.Entity;
import io.github.ale.screens.game.map.MapManager;

public class ChangeMapEvent extends MapEvent {
    private final MapManager mapManager;
    private float x, y;
    private final int map;

    public ChangeMapEvent(Vector2 position, float radius, World world, MapManager mapManager, int map, float x, float y) {
        super(position, radius, world);
        this.x = x;
        this.y = y;
        this.map = map;
        this.mapManager = mapManager;
    }

    @Override
    public void update() {
        if (isActive()){
            if (Gdx.input.isKeyJustPressed(ComandiGioco.getUSA())) {
                Gdx.app.postRunnable(() -> mapManager.changeMap(map, x+0.5f, y+0.5f));
                active = false;
            }

        }
    }

    @Override
    public void trigger(Entity entity) {
        if (entity instanceof Player) {
            System.out.println("attivato");
        }
    }
}
