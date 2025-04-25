package progetto.entity.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.entity.Engine;
import progetto.entity.components.specific.ai.StateComponent;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.graphics.ZLevelComponent;
import progetto.entity.components.specific.movement.DirectionComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.input.DebugWindow;
import progetto.player.ManagerCamera;

import java.util.Comparator;
import java.util.concurrent.Semaphore;

public class EntityManager {
    final Engine engine;
    final Comparator<Entity> comparator;
    final Array<Entity> entities;
    final Queue<Entity> queue;

    private float elapsedTime;

    private final Semaphore semaphore;
    private boolean active = false;

    /**
     * Costruttore
     *
     * @param engine manager delle entità
     */
    public EntityManager(Engine engine) {
        this.engine = engine;

        comparator = (o1, o2) -> {
            if (o1.components.contains(ZLevelComponent.class) && o2.components.contains(ZLevelComponent.class)) {
                int z1 = o1.get(ZLevelComponent.class).getZ();
                int z2 = o2.get(ZLevelComponent.class).getZ();
                if (z1 == z2) return Float.compare(o2.get(PhysicsComponent.class).getPosition().y, o1.get(PhysicsComponent.class).getPosition().y);
                return Integer.compare(z1, z2);
            }
            return Float.compare(o2.get(PhysicsComponent.class).getPosition().y, o1.get(PhysicsComponent.class).getPosition().y);
        };
        queue = engine.getQueue();
        entities = engine.getEntities();

        semaphore = new Semaphore(1);
    }

    /**
     * Processa la coda e aggiorna le entità
     */
    public void updateEntities() {
        this.elapsedTime = engine.elapsedTime;
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

        engine.game.core.batch.begin();
        drawSkills();
        engine.game.core.batch.end();
    }

    public void sort() {
        entities.sort(comparator);
    }

    /**
     * Disegna le skill
     */
    private void drawSkills() {
        for (Entity e : entities) {
            if (ManagerCamera.isWithinFrustumBounds(e.get(PhysicsComponent.class).getPosition().x, e.get(PhysicsComponent.class).getPosition().y) && e instanceof Humanoid) {
                ((Humanoid) e).getSkillset().draw(engine.game.core.batch, elapsedTime);
            }
        }
    }

    /**
     * Disegna le entità
     */
    public void drawPaths() {
        engine.game.core.renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : entities) {
            if (ManagerCamera.isWithinFrustumBounds(e.get(PhysicsComponent.class).getPosition().x, e.get(PhysicsComponent.class).getPosition().y)) {
                try {
                    if (e instanceof Humanoid human) {
                        human.drawPath(engine.game.core.renderer);
                    }
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.get(DirectionComponent.class).direction);
                }
            }
        }
        engine.game.core.renderer.end();
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
        entity.components.get(PhysicsComponent.class).initBody();
        entity.create();
        engine.addEntityToSystems(entity);
        entity.components.get(StateComponent.class).setLoaded(true);
    }

}
