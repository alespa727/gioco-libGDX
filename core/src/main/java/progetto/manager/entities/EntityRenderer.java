package progetto.manager.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.specific.living.Humanoid;
import progetto.gameplay.player.ManagerCamera;
import progetto.gameplay.player.Player;
import progetto.manager.input.DebugWindow;
import progetto.core.game.GameInfo;

import java.util.Comparator;

public class EntityRenderer {
    final GameInfo info;
    final Engine engine;
    final Comparator<Entity> comparator;
    final Array<Entity> entities;
    final Queue<Entity> queue;

    float deltaTime;
    float elapsedTime;

    /**
     * Costruttore
     * @param engine manager delle entità
     */
    public EntityRenderer(Engine engine) {
        this.engine = engine;
        comparator = (o1, o2) -> {
            if (o2.getZ() != o1.getZ()) {
                return Integer.compare(o1.getZ(), o2.getZ());
            }
            return Float.compare(o2.getPosition().y, o1.getPosition().y);
        };
        info = engine.info;
        queue = getEntityQueue();
        entities = getEntities();
    }

    /**
     * Processa la coda e aggiorna le entità
     */
    public void updateEntities() {
        this.deltaTime = engine.delta;
        this.elapsedTime = engine.elapsedTime;
        processQueue();
        updateEntityLogic();
    }

    /**
     * Disegna entità e skill
     */
    public void draw() {
        if(DebugWindow.renderPathfinding()){
            drawPaths();
        }
        entities.sort(comparator);

        info.core.batch.begin();
        drawEntities();
        drawSkills();
        info.core.batch.end();
    }

    public void sort(){
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
    private void drawEntities() {
        for (Entity e : entities) {
            if (ManagerCamera.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y)) {
                try {
                    e.draw(info.core.batch, this.elapsedTime);
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.getDirection());
                }
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
                    if(e instanceof Humanoid human) {
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
    public void updateEntityLogic(){
        if(DebugWindow.renderEntities()){
            for (Entity e : entities) {
                if (ManagerCamera.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y) || e instanceof Player) {
                    e.render(this.deltaTime);
                    e.componentManager(this.deltaTime);
                    e.setShouldRender(true);
                } else e.setShouldRender(false);
            }
        }else{
            engine.player().render(this.deltaTime);
            engine.player().componentManager(this.deltaTime);
            engine.player().setShouldRender(true);
        }
    }

    /**
     * Processa la coda, svuotandola e evocando entità dalla coda
     */
    private void processQueue() {
        while(queue.size > 0){
            entities.add(queue.first());
            queue.first().getPhysics().initBody();
            queue.first().create();
            queue.removeFirst().load();
        }
    }

    /**
     * @return array di entità
     */
    public Array<Entity> getEntities() {
        return engine.getEntities();
    }

    /**
     * @return coda di entità da evocare
     */
    public Queue<Entity> getEntityQueue() {
        return engine.getQueue();
    }
}
