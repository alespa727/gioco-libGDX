package progetto.gameplay.manager.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.gameplay.GameInfo;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.combat.player.Player;
import progetto.gameplay.entity.types.notliving.Bullet;
public class EntityLifeCycleManager {
    GameInfo info;
    ManagerEntity managerEntity;
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
        if (queue.size > 0) {
            queue.clear();
        }
        for (Entity e : entities) {
            if (e instanceof Player) continue;
            if (e instanceof Bullet) System.out.println("PROIETTILE");
            instances.add(e.despawn());
        }
        System.out.println(instances.size);
        return instances;
    }

    public Entity summon(Entity e) {
        queue = getEntityQueue();
        entities = getEntities();
        queue.addFirst(e);
        return queue.first();
    }

    public void remove(Entity e) {
        entities.removeValue(e, false);
        entities.shrink();
    }

    public Array<Entity> getEntities() {
        return managerEntity.getEntities();
    }

    public Queue<Entity> getEntityQueue() {
        return managerEntity.getQueue();
    }
}
