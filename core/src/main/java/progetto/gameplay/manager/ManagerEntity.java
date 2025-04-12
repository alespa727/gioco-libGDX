package progetto.gameplay.manager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import progetto.Core;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.gameplay.manager.entity.EntityLifeCycleManager;
import progetto.gameplay.manager.entity.EntityRenderer;
import progetto.gameplay.manager.entity.PlayerManager;
import progetto.utils.GameInfo;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.combat.boss.BossInstance;
import progetto.gameplay.entity.types.living.combat.enemy.EnemyInstance;
import progetto.gameplay.player.Player;

public final class ManagerEntity {

    public final GameInfo info;
    private final EntityLifeCycleManager lifeCycleManager;
    private final PlayerManager playerManager;
    private final EntityRenderer renderer;

    private final Array<Entity> entities;
    private final Queue<Entity> queue;

    public static final short WALL = 0;
    public static final short RANGE = 0x0010;
    private static int idCount=0;

    public float delta;
    public float elapsedTime;

    /**
     * @param info informazioni del gioco
     */
    public ManagerEntity(GameInfo info) {
        this.lifeCycleManager = new EntityLifeCycleManager(this);
        this.info = info;
        this.entities = new Array<>();
        this.queue = new Queue<>();
        this.playerManager = new PlayerManager(this, lifeCycleManager);
        this.summon(playerManager.getPlayer());
        Core.assetManager.load("entities/nemico.png", Texture.class);
        Core.assetManager.finishLoading();
        EntityConfig e = EntityConfigFactory.createEntityConfig("Finn", getIdCount(), 8, 12);
        summon(EntityFactory.createEnemy("Finn", e, this, 1.5f));

        this.renderer = new EntityRenderer(this);
    }

    public int getIdCount(){
        idCount++;
        return idCount;
    }

    /**
     * @return la lista di entità
     */
    public Array<Entity> getEntities() {
        return entities;
    }

    /**
     * @return la coda di entità da evocare
     */
    public Queue<Entity> getQueue() {
        return queue;
    }


    /**
     * Pulisce la coda di entità da evocare
     */
    public void clearQueue() {
        queue.clear();
    }

    /**
     * Rimuove tutte le entità
     * @return array di istanze di tutte le entità rimosse
     */
    public Array<EntityInstance> clear() {
        return lifeCycleManager.clear();
    }

    /**
     * Crea un entità
     * @param e da evocare
     * @return l'entità evocata
     */
    public Entity summon(Entity e) {
        return lifeCycleManager.summon(e);
    }

    /**
     * @param instances istanze delle entità da evocare
     */
    public void summon(Array<EntityInstance> instances) {
        for (EntityInstance instance : instances) {
            if (instance==null) continue;

            if (instance instanceof EnemyInstance) summon(EntityFactory.createEnemy(instance.type, (EnemyInstance) instance, this, 1.5f));
            if (instance instanceof BossInstance) summon(EntityFactory.createBoss(instance.type, (BossInstance) instance, this));
        }
    }

    /**
     * Disegna tutte le entità
     */
    public void draw() {
        renderer.draw();
    }

    /**
     * Aggiorna tutte le entità e variabili
     * @param delta tempo trascorso da ultimo frame
     */
    public void render(float delta) {
        this.delta = delta;
        this.elapsedTime += delta;
        renderer.updateEntities();
    }

    /**
     * @return l'entità player
     */
    public Player player() {
        return playerManager.getPlayer();
    }

    /**
     * @param e entità da rimuovere
     */
    public void remove(Entity e) {
        lifeCycleManager.remove(e);
    }

}
