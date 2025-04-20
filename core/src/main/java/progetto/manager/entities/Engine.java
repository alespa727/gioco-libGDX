package progetto.manager.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.core.Core;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.specific.base.Entity;
import progetto.gameplay.entities.specific.base.EntityConfig;
import progetto.gameplay.entities.specific.base.EntityInstance;
import progetto.gameplay.entities.specific.specific.living.combat.boss.BossInstance;
import progetto.gameplay.entities.specific.specific.living.combat.enemy.EnemyInstance;
import progetto.gameplay.systems.base.System;
import progetto.gameplay.player.Player;
import progetto.core.game.GameInfo;
import progetto.gameplay.systems.specific.*;
import progetto.manager.input.DebugWindow;
import progetto.manager.input.TerminalCommand;
import progetto.gameplay.player.PlayerManager;

import java.util.HashMap;
import java.util.Map;

public final class Engine {

    public final GameInfo info;
    private final EntityRenderer renderer;

    public Array<System> systems;
    public Map<Integer, Map<Class<? extends Component>, Component>> components;

    private final PlayerManager playerManager;
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
    public Engine(GameInfo info) {
        this.info = info;
        this.entities = new Array<>();
        this.queue = new Queue<>();
        this.playerManager = new PlayerManager(this);
        this.summon(playerManager.getPlayer());
        Core.assetManager.load("entities/nemico.png", Texture.class);
        Core.assetManager.load("entities/Lich.png", Texture.class);
        Core.assetManager.finishLoading();

        for (int i = 0; i < 500; i++) {
            for (int j = 0; j < 10; j++) {
                EntityConfig e = EntityConfigFactory.createEntityConfig("Finn", getIdCount(), 8+i, 10+j);
                summon(EntityFactory.createEnemy("Finn", e, this, 5));
            }
        }

        this.renderer = new EntityRenderer(this);
        System[] systems = new System[]{
            new DeathSystem(),
            new DrawingSystem(info.core.batch),
            new HitSystem(),
            new SpeedLimiterSystem(),
            new UserInputSystem(),
            new KnockbackSystem(),
            new NodeTrackerSystem(),
            new RangeSystem(),
            new PlayerSystem(),
            new MovementSystem(),
            new SkillSystem(),
        };
        this.systems = new Array<>(systems);
        this.components = new HashMap<>();
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

            if (instance instanceof EnemyInstance) summon(EntityFactory.createEnemy(instance.type, (EnemyInstance) instance, this, 1.5f));
            if (instance instanceof BossInstance) summon(EntityFactory.createBoss(instance.type, (BossInstance) instance, this));
        }

    }

    /**
     * Disegna tutte le entità
     */
    public void draw() {
        renderer.sort();
        if(DebugWindow.renderPathfinding()) renderer.drawPaths();
        for (System s : systems) {
            if (s instanceof DrawingSystem && s.isActive()) systems.get(1).update(Gdx.graphics.getDeltaTime(), entities);
        }
    }

    /**
     * Aggiorna tutte le entità e variabili
     * @param delta tempo trascorso da ultimo frame
     */
    public void render(float delta) {
        this.delta = delta;
        this.elapsedTime += delta;
        updateSystems();
        renderer.updateEntities();
    }

    public void updateSystems(){
        for (System s : systems) {
            if (s instanceof DrawingSystem) {
                continue;
            }
            if (s.isActive()) s.update(delta, entities);
        }
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
        entities.removeValue(e, false);
        entities.shrink();
    }

}
