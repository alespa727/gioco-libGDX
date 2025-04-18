package progetto.rendering.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.factories.BodyFactory;
import progetto.entity.components.specific.warrior.DirectionalRangeComponent;
import progetto.entity.specific.base.Entity;
import progetto.entity.specific.specific.living.Humanoid;
import progetto.entity.specific.specific.living.combat.Warrior;
import progetto.manager.player.CameraManager;
import progetto.manager.player.Player;
import progetto.menu.DebugWindow;
import progetto.core.GameInfo;

import java.util.Comparator;

public class EntityRenderer {
    final GameInfo info;
    final EntityManager entityManager;
    final Comparator<Entity> comparator;
    final Array<Entity> entities;
    final Queue<Entity> queue;

    float deltaTime;
    float elapsedTime;

    /**
     * Costruttore
     * @param entityManager manager delle entità
     */
    public EntityRenderer(EntityManager entityManager) {
        this.entityManager = entityManager;
        comparator = (o1, o2) -> Float.compare(o2.getPosition().y, o1.getPosition().y);
        info = entityManager.info;
        queue = getEntityQueue();
        entities = getEntities();
    }

    /**
     * Processa la coda e aggiorna le entità
     */
    public void updateEntities() {
        this.deltaTime = entityManager.delta;
        this.elapsedTime = entityManager.elapsedTime;
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

    /**
     * Disegna le skill
     */
    private void drawSkills() {
        for (Entity e : entities) {
            if (CameraManager.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y) && e instanceof Humanoid) {
                ((Humanoid) e).getSkillset().draw(info.core.batch, elapsedTime);
            }
        }
    }

    /**
     * Disegna le entità
     */
    private void drawEntities() {
        for (Entity e : entities) {
            if (CameraManager.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y)) {
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
    private void drawPaths() {
        info.core.renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : entities) {
            if (CameraManager.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y)) {
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
                if (CameraManager.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y) || e instanceof Player) {
                    e.render(this.deltaTime);
                    e.updateComponents(this.deltaTime);
                    e.setShouldRender(true);
                } else e.setShouldRender(false);
            }
        }else{
            entityManager.player().render(this.deltaTime);
            entityManager.player().updateComponents(this.deltaTime);
            entityManager.player().setShouldRender(true);
        }
    }

    /**
     * Processa la coda, svuotandola e evocando entità dalla coda
     */
    private void processQueue() {
        while(queue.size > 0){
            entities.add(queue.first());
            queue.first().getPhysics().initBody();
            if (queue.first() instanceof Warrior ce) {
                DirectionalRangeComponent a = ce.getDirectionRangeComponent();
                ce.setDirectionalRange(BodyFactory.createBody(ce, a.getBodyDef(), a.getFixtureDef()));
            }
            queue.first().create();
            queue.removeFirst().load();
        }
    }

    /**
     * @return array di entità
     */
    public Array<Entity> getEntities() {
        return entityManager.getEntities();
    }

    /**
     * @return coda di entità da evocare
     */
    public Queue<Entity> getEntityQueue() {
        return entityManager.getQueue();
    }
}
