package progetto.entity.systems;

import com.badlogic.gdx.utils.Array;
import progetto.entity.Engine;
import progetto.entity.entities.Entity;
import progetto.entity.systems.base.IteratingSystem;
import progetto.entity.systems.base.System;
import progetto.entity.systems.specific.DrawingSystem;
import progetto.input.DebugWindow;

import java.util.LinkedHashMap;

public class SystemManager {
    private final LinkedHashMap<Class<?>, System> logicSystems;
    private final DrawingSystem drawingSystem;

    public SystemManager(Engine engine) {
        logicSystems = new LinkedHashMap<>();
        drawingSystem = new DrawingSystem(engine.game.core.batch);
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
