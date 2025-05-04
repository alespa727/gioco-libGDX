package progetto.ECS.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.ECS.EntityEngine;
import progetto.ECS.components.specific.base.StateComponent;
import progetto.ECS.components.specific.base.PhysicsComponent;
import progetto.ECS.components.specific.graphics.ZLevelComponent;
import progetto.ECS.components.specific.movement.DirectionComponent;
import progetto.ECS.entities.specific.living.Humanoid;
import progetto.input.DebugWindow;
import progetto.core.CameraManager;

import java.util.Comparator;

public class EntityManager {
    final EntityEngine entityEngine;
    final Comparator<Entity> comparator;
    final Array<Entity> entities;
    final Queue<Entity> queue;

    private float elapsedTime;
    /**
     * Costruttore
     *
     * @param entityEngine manager delle entità
     */
    public EntityManager(EntityEngine entityEngine) {
        this.entityEngine = entityEngine;

        comparator = (o1, o2) -> {
            if (o1.contains(ZLevelComponent.class) && o2.contains(ZLevelComponent.class)) {
                int z1 = o1.get(ZLevelComponent.class).getZ();
                int z2 = o2.get(ZLevelComponent.class).getZ();
                if (z1 == z2 && o1.contains(PhysicsComponent.class) && o2.contains(PhysicsComponent.class)) return Float.compare(o2.get(PhysicsComponent.class).getPosition().y, o1.get(PhysicsComponent.class).getPosition().y);
                return Integer.compare(z1, z2);
            }
            if (!o1.contains(PhysicsComponent.class) && !o2.contains(PhysicsComponent.class)) {
                return 0;
            }
            return Float.compare(o2.get(PhysicsComponent.class).getPosition().y, o1.get(PhysicsComponent.class).getPosition().y);
        };
        queue = entityEngine.getQueue();
        entities = entityEngine.getEntities();
    }

    /**
     * Processa la coda e aggiorna le entità
     */
    public void updateEntities() {
        this.elapsedTime = entityEngine.elapsedTime;
        processQueue();

    }

    /**
     * Disegna entità e skill
     */
    public void draw() {
        if (DebugWindow.renderPathfinding()) {
            drawPaths();
        }
        entities.sort(comparator);

        entityEngine.game.app.batch.begin();
        drawSkills();
        entityEngine.game.app.batch.end();
    }

    public void sort() {
        entities.sort(comparator);
    }

    /**
     * Disegna le skill
     */
    private void drawSkills() {
        for (Entity e : entities) {
            if (CameraManager.isWithinFrustumBounds(e.get(PhysicsComponent.class).getPosition().x, e.get(PhysicsComponent.class).getPosition().y) && e instanceof Humanoid) {
                ((Humanoid) e).getSkillset().draw(entityEngine.game.app.batch, elapsedTime);
            }
        }
    }

    /**
     * Disegna le entità
     */
    public void drawPaths() {
        entityEngine.game.app.renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : entities) {
            if (CameraManager.isWithinFrustumBounds(e.get(PhysicsComponent.class).getPosition().x, e.get(PhysicsComponent.class).getPosition().y)) {
                try {
                    if (e instanceof Humanoid human) {
                        human.drawPath(entityEngine.game.app.renderer);
                    }
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.get(DirectionComponent.class).direction);
                }
            }
        }
        entityEngine.game.app.renderer.end();
    }

    /**
     * Processa la coda, svuotandola e evocando entità dalla coda
     */
    public void processQueue() {
        if (queue.isEmpty()) return;

        while (queue.size > 0) {
            Entity e = queue.removeFirst();
            if (e != null) {
                loadEntity(e);
            }
        }
    }

    private void loadEntity(Entity entity) {
        entities.add(entity);
        if (entity.contains(PhysicsComponent.class)) {
            entity.get(PhysicsComponent.class).initBody();
        }
        entity.create();
        entityEngine.addEntityToSystems(entity);
        entity.get(StateComponent.class).setLoaded(true);
    }

}
