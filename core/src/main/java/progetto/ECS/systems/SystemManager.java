package progetto.ECS.systems;

import com.badlogic.gdx.utils.Array;
import progetto.ECS.EntityEngine;
import progetto.ECS.entities.Entity;
import progetto.ECS.systems.base.IteratingSystem;
import progetto.ECS.systems.base.System;
import progetto.ECS.systems.specific.DrawingSystem;
import progetto.input.DebugWindow;

import java.util.LinkedHashMap;

public class SystemManager {
    private final LinkedHashMap<Class<?>, System> logicSystems;
    private final DrawingSystem drawingSystem;

    public SystemManager(EntityEngine entityEngine) {
        logicSystems = new LinkedHashMap<>();
        drawingSystem = new DrawingSystem(entityEngine.game.app.batch);
    }

    public void add(System... systems) {
        for (System s : systems) {
            this.logicSystems.put(s.getClass(), s);
        }
    }

    public void remove(Class<?> system) {
        logicSystems.remove(system);
    }

    public void update(float delta, Array<Entity> entities) {
        if (!DebugWindow.renderEntities()) return;
        for (System s : logicSystems.values()) {
            s.update(delta, entities);
        }
    }

    public void addEntities(Entity... entities) {
        for (System s : logicSystems.values()) {
            if(s instanceof IteratingSystem system){
                system.addEntity(entities);
            }
        }
    }

    public void removeEntities(Entity e) {
        for (System s : logicSystems.values()) {
            if(s instanceof IteratingSystem system){
                system.removeEntity(e);
            }
        }
    }

    public void draw(float delta, Array<Entity> entities) {

        drawingSystem.update(delta, entities);
    }

}
