package progetto.gameplay.systems.base;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.specific.base.Entity;

public abstract class AutomaticSystem extends System{

    private float elapsedTime;

    private final Array<Entity> filteredEntities;
    private final Queue<Entity> entitiesToRemove;
    private final Array<Class<? extends Component>> componentsForFilter;

    // DA FARE L?HASHSET

    public AutomaticSystem() {
        filteredEntities = new Array<>();
        entitiesToRemove = new Queue<>();
        componentsForFilter = new Array<>();
    }

    public AutomaticSystem(Array<Class<? extends Component>> requiredComponents) {
        filteredEntities = new Array<>();
        componentsForFilter = new Array<>(requiredComponents);
        entitiesToRemove = new Queue<>();
    }

    public void update(float delta, Array<Entity> entities) {
        if (!entitiesToRemove.isEmpty()) {
            filteredEntities.removeValue(entitiesToRemove.removeFirst(), false);
        }

        elapsedTime += delta;

        for (int i = 0; i < entities.size; i++) {
            Entity e = entities.get(i);
            processEntity(e, delta);
        }
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void addEntity(Entity e) {
        if (e.containsComponents(componentsForFilter)) {
            filteredEntities.add(e);
        }
    }

    public void removeEntity(Entity e) {
        if (e.containsComponents(componentsForFilter)) {
            entitiesToRemove.addLast(e);
        }
    }

    public abstract void processEntity(Entity entity, float delta);
}
