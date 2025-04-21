package progetto.gameplay.systems.base;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.specific.base.Entity;

public abstract class AutomaticSystem{

    private final Array<Entity> filteredEntities;
    private final Queue<Entity> entitiesToRemove;
    private final Array<Class<? extends Component>> componentsForFilter;

    // DA FARE L?HASHSET

    public AutomaticSystem(Array<Class<? extends Component>> requiredComponents) {
        filteredEntities = new Array<>();
        componentsForFilter = new Array<>(requiredComponents);
        entitiesToRemove = new Queue<>();
    }

    public void update(float delta) {
        if (!entitiesToRemove.isEmpty()) {
            filteredEntities.removeValue(entitiesToRemove.removeFirst(), false);
        }

        for (int i = 0; i < filteredEntities.size; i++) {
            Entity e = filteredEntities.get(i);
            processEntity(e, delta);
        }
    }

    public void addEntity(Entity e) {
        if (e.containsComponents(componentsForFilter)){
            filteredEntities.add(e);
        }
    }

    public void removeEntity(Entity e) {
        if (e.containsComponents(componentsForFilter)){
            entitiesToRemove.addLast(e);
        }
    }

    protected abstract void processEntity(Entity entity, float delta);
}
