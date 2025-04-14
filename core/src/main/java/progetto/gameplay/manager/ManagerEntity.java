package progetto.gameplay.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import progetto.Core;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.gameplay.entity.types.living.combat.boss.Boss;
import progetto.gameplay.entity.types.living.combat.boss.BossInstance;
import progetto.gameplay.entity.types.living.npc.ProvaNpc;
import progetto.gameplay.entity.types.living.npc.WindowDialogo;
import progetto.gameplay.manager.entity.EntityLifeCycleManager;
import progetto.gameplay.manager.entity.EntityRenderer;
import progetto.gameplay.manager.entity.PlayerManager;
import progetto.utils.GameInfo;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.combat.enemy.EnemyInstance;
import progetto.gameplay.player.Player;
import progetto.utils.TerminalCommand;

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
        Core.assetManager.load("entities/Lich.png", Texture.class);
        Core.assetManager.finishLoading();

        for (int i = 0; i < 1; i++) {
            EntityConfig e = EntityConfigFactory.createEntityConfig("Lich", getIdCount(), 8+i, 10);
            summon(EntityFactory.createBoss("Lich", e, this));
        }

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

    public Array<EntityInstance> clear() {
        Array<EntityInstance> instances = new Array<>();
        Array<Entity> entitiesCopy = new Array<>(entities); // Crea una copia della lista

        Player p = playerManager.getPlayer();

        for (Entity e : entitiesCopy) {
            EntityInstance in = e.despawn();
            if (in != null) {
                instances.add(in);
            }

        }

        // Dopo l'iterazione, svuota la lista originale
        entities.clear();
        queue.clear();

        entities.add(p);

        // Verifica il conteggio delle entità rimosse
        TerminalCommand.printError("Entità rimosse: " + instances.size + instances);
        return instances;
    }
    /**
     * Crea un entità
     * @param e da evocare
     * @return l'entità evocata
     */
    public Entity summon(Entity e) {
        queue.addFirst(e);
        return queue.first();
    }

    /**
     * @param instances istanze delle entità da evocare
     */
    public void summon(Array<EntityInstance> instances) {

        for (EntityInstance instance : instances) {
            if (instance==null) continue;

            System.out.println();
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
        TerminalCommand.printWarning("Removing entity " + e.getClass().getSimpleName());
        entities.removeValue(e, false);
        entities.shrink();
    }

}
