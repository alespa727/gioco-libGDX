package progetto.manager.entities;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.gameplay.player.Player;
import progetto.core.game.GameInfo;

public class EntityLifeCycleManager {
    final GameInfo info;
    final Engine engine;
    Array<Entity> entities;
    Queue<Entity> queue;

    Array<EntityInstance> instances;

    public EntityLifeCycleManager(Engine engine) {
        this.engine = engine;
        info = engine.info;
        queue = getEntityQueue();
        entities = getEntities();
    }

    public Array<EntityInstance> clear() {
        queue = getEntityQueue();
        entities = getEntities();
        instances = new Array<>();
        Player player = engine.player();
        entities.removeValue(player, false);
        if (queue.size > 0) {
            queue.clear();
        }
        for (Entity e : entities) {
            instances.add(e.despawn());
        }

        entities.add(player);
        return instances;
    }

    public Entity summon(Entity e) {
        queue = getEntityQueue();
        entities = getEntities();
        queue.addFirst(e);
        return queue.first();
    }

    public void remove(Entity e) {

    }

    public Array<Entity> getEntities() {
        return engine.getEntities();
    }

    public Queue<Entity> getEntityQueue() {
        return engine.getQueue();
    }
}
