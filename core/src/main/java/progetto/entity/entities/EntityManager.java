package progetto.entity.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.core.game.GameInfo;
import progetto.entity.Engine;
import progetto.entity.components.specific.ai.StateComponent;
import progetto.entity.components.specific.base.PhysicsComponent;
import progetto.entity.components.specific.graphics.ZLevelComponent;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.player.ManagerCamera;
import progetto.player.Player;
import progetto.input.DebugWindow;

import java.util.Comparator;
import java.util.concurrent.Semaphore;

public class EntityManager {
    final GameInfo info;
    final Engine engine;
    final Comparator<Entity> comparator;
    final Array<Entity> entities;
    final Queue<Entity> queue;

    private float elapsedTime;

    private final Semaphore semaphore;
    private boolean active=false;

    /**
     * Costruttore
     *
     * @param engine manager delle entità
     */
    public EntityManager(Engine engine) {
        this.engine = engine;

        comparator = (o1, o2) -> {
            if (o1.components.contains(ZLevelComponent.class) && o2.components.contains(ZLevelComponent.class)) {
                int z1 = o1.getComponent(ZLevelComponent.class).getZ();
                int z2 = o2.getComponent(ZLevelComponent.class).getZ();
                return Integer.compare(z1, z2);
            }
            return Float.compare(o2.getPosition().y, o1.getPosition().y);
        };
        info = engine.info;
        queue = engine.getQueue();
        entities = engine.getEntities();

        semaphore = new Semaphore(1);
    }

    /**
     * Processa la coda e aggiorna le entità
     */
    public void updateEntities() {
        this.elapsedTime = engine.elapsedTime;
        if (!active) {
            processQueue();
        }
        updateEntityLogic();
    }

    /**
     * Disegna entità e skill
     */
    public void draw() {
        if (DebugWindow.renderPathfinding()) {
            drawPaths();
        }
        entities.sort(comparator);

        info.core.batch.begin();
        drawSkills();
        info.core.batch.end();
    }

    public void sort() {
        entities.sort(comparator);
    }

    /**
     * Disegna le skill
     */
    private void drawSkills() {
        for (Entity e : entities) {
            if (ManagerCamera.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y) && e instanceof Humanoid) {
                ((Humanoid) e).getSkillset().draw(info.core.batch, elapsedTime);
            }
        }
    }

    /**
     * Disegna le entità
     */
    public void drawPaths() {
        info.core.renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : entities) {
            if (ManagerCamera.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y)) {
                try {
                    if (e instanceof Humanoid human) {
                        human.drawPath(info.core.renderer);
                    }
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.getDirection());
                }
            }
        }
        info.core.renderer.end();
    }

    /**
     * Aggiorna le entità
     */
    public void updateEntityLogic() {
        if (DebugWindow.renderEntities()) {
            for (Entity e : entities) {
                if (ManagerCamera.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y) || e instanceof Player) {
                    e.setShouldRender(true);
                } else e.setShouldRender(false);
            }
        } else {
            engine.player().setShouldRender(true);
        }
    }

    /**
     * Processa la coda, svuotandola e evocando entità dalla coda
     */
    private void processQueue() {
        if (queue.isEmpty()) return;
        active = true;

        new Thread(() -> {
            try {
                semaphore.acquire();
                while (queue.size > 0) {
                    Entity e = queue.removeFirst();
                    if(e != null) {
                        loadEntity(e);
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("Thread interrotto durante il processQueue");
                Thread.currentThread().interrupt(); // ripristina lo stato di interruzione
            }finally {
                semaphore.release();
                active = false;
            }

        }).start();

    }

    private void loadEntity(Entity entity) {
        entities.add(entity);
        entity.components.get(PhysicsComponent.class).initBody();
        entity.create();
        engine.addEntityToSystems(entity);
        entity.components.get(StateComponent.class).setLoaded(true);
    }

}
