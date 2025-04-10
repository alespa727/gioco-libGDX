package progetto.gameplay.manager.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.factories.BodyFactory;
import progetto.gameplay.GameInfo;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.gameplay.entity.types.living.combat.Warriors;
import progetto.gameplay.entity.types.living.combat.player.Player;
import progetto.gameplay.manager.ManagerCamera;

import java.util.Comparator;

public class EntityRenderer {
    GameInfo info;
    ManagerEntity managerEntity;
    Comparator<Entity> comparator;
    Array<Entity> entities;
    Queue<Entity> queue;

    float deltaTime;
    float elapsedTime;

    /**
     * Costruttore
     * @param managerEntity manager delle entità
     */
    public EntityRenderer(ManagerEntity managerEntity) {
        this.managerEntity = managerEntity;
        comparator = (o1, o2) -> Float.compare(o2.getPosition().y, o1.getPosition().y);
        info = managerEntity.info;
        queue = getEntityQueue();
        entities = getEntities();
    }

    /**
     * Processa la coda e aggiorna le entità
     */
    public void updateEntities() {
        this.deltaTime = managerEntity.delta;
        this.elapsedTime = managerEntity.elapsedTime;
        processQueue();
        updateEntityLogic();
    }

    /**
     * Disegna entità e skill
     */
    public void draw() {
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
            if (ManagerCamera.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y) && e instanceof Humanoid) {
                try {
                    ((Humanoid) e).getSkillset().draw(info.core.batch, elapsedTime);
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.getDirection());
                }
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
     * Aggiorna le entità
     */
    public void updateEntityLogic(){
        for (Entity e : entities) {
            if (ManagerCamera.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y) || e instanceof Player) {
                e.render(this.deltaTime);
                e.updateComponents(this.deltaTime);
                e.setShouldRender(true);
            } else e.setShouldRender(false);
        }
    }

    /**
     * Processa la coda, svuotandola e evocando entità dalla coda
     */
    private void processQueue() {
        if(!queue.isEmpty()){
            entities.add(queue.last());
            queue.last().getPhysics().initBody();
            if (queue.last() instanceof Warriors ce) {
                ce.directionalRange = BodyFactory.createBody(ce, ce.bodyDef, ce.fixtureDef, ce.shape);
            }
            queue.last().create();
            queue.removeLast().load();
        }
    }

    /**
     * @return array di entità
     */
    public Array<Entity> getEntities() {
        return managerEntity.getEntities();
    }

    /**
     * @return coda di entità da evocare
     */
    public Queue<Entity> getEntityQueue() {
        return managerEntity.getQueue();
    }
}
