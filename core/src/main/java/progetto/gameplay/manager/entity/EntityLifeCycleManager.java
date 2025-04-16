package progetto.gameplay.manager.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.player.Player;
import progetto.utils.GameInfo;

public class EntityLifeCycleManager {
    final GameInfo info;
    final ManagerEntity managerEntity;
    Array<Entity> entities;
    Queue<Entity> queue;

    Array<EntityInstance> instances;

    public EntityLifeCycleManager(ManagerEntity managerEntity) {
        this.managerEntity = managerEntity;
        info = managerEntity.info;
        queue = getEntityQueue();
        entities = getEntities();
    }

    public Array<EntityInstance> clear() {
        queue = getEntityQueue();
        entities = getEntities();
        instances = new Array<>();
        Player player = managerEntity.player();
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
        return managerEntity.getEntities();
    }

    public Queue<Entity> getEntityQueue() {
        return managerEntity.getQueue();
    }
}
