package progetto.manager.entities;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.systems.base.AutomaticSystem;
import progetto.gameplay.systems.base.System;
import progetto.gameplay.systems.specific.DrawingSystem;

import java.util.LinkedHashMap;

public class SystemManager {
    private final LinkedHashMap<Class<?>, System> logicSystems;
    private final DrawingSystem drawingSystem;

    public SystemManager(Engine engine) {
        logicSystems = new LinkedHashMap<>();
        drawingSystem = new DrawingSystem(engine.info.core.batch);
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
        for (System s : logicSystems.values()) {
            if (s.isActive()) {
                s.update(delta, entities);
            }
        }
    }

    public void addEntities(Entity... entities) {
        for (System s : logicSystems.values()) {
            if(s instanceof AutomaticSystem system){
                system.addEntity(entities);
            }
        }
    }

    public void removeEntities(Entity e) {
        for (System s : logicSystems.values()) {
            if(s instanceof AutomaticSystem system){
                system.removeEntity(e);
            }
        }
    }

    public void draw(float delta, Array<Entity> entities) {
        drawingSystem.update(delta, entities);
    }

}
