package progetto.ECS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.core.game.GameScreen;
import progetto.ECS.components.specific.general.Saveable;
import progetto.ECS.entities.EntityManager;
import progetto.ECS.entities.Entity;
import progetto.ECS.entities.specific.EntityInstance;
import progetto.ECS.entities.specific.living.combat.boss.Boss;
import progetto.ECS.entities.specific.living.combat.boss.BossInstance;
import progetto.ECS.entities.specific.living.combat.enemy.BaseEnemy;
import progetto.ECS.entities.specific.living.combat.enemy.EnemyInstance;
import progetto.ECS.systems.SystemManager;
import progetto.ECS.systems.base.System;
import progetto.factories.EntityFactory;
import progetto.input.DebugWindow;
import progetto.core.game.Terminal;
import progetto.core.game.player.Player;

/**
 * Motore principale per la gestione delle entità e dei sistemi ECS.
 * Si occupa di aggiornare, disegnare, salvare e ripristinare entità,
 * oltre a gestire la coda di spawn e la comunicazione con i sistemi.
 */
public final class EntityEngine {

    public static final short WALL = 0;
    public static final short RANGE = 0x0010;

    public final GameScreen game;
    private final EntityManager renderer;
    private final SystemManager systemManager;

    private final Array<Entity> entities;
    private final Queue<Entity> queue;

    public float delta;
    public float elapsedTime;

    /**
     * Costruisce un nuovo EntityEngine.
     *
     * @param gameScreen la schermata di gioco principale.
     */
    public EntityEngine(GameScreen gameScreen) {
        this.game = gameScreen;
        this.entities = new Array<>();
        this.queue = new Queue<>();
        this.systemManager = new SystemManager(this);
        this.renderer = new EntityManager(this);
    }

    /**
     * Aggiunge un'entità ai sistemi ECS registrati.
     *
     * @param e l'entità da aggiungere.
     */
    public void addEntityToSystems(Entity e) {
        systemManager.addEntities(e);
    }

    /**
     * Rimuove un'entità da tutti i sistemi ECS registrati.
     *
     * @param e l'entità da rimuovere.
     */
    public void removeEntitiesFromSystems(Entity e) {
        systemManager.removeEntities(e);
    }

    /**
     * Aggiunge uno o più sistemi all'engine.
     *
     * @param systems i sistemi da aggiungere.
     */
    public void addSystem(System... systems) {
        systemManager.add(systems);
    }

    /**
     * Restituisce la lista di tutte le entità attive.
     *
     * @return array di entità attive.
     */
    public Array<Entity> getEntities() {
        return entities;
    }

    /**
     * Restituisce la coda di spawn delle entità.
     *
     * @return la coda delle entità.
     */
    public Queue<Entity> getQueue() {
        return queue;
    }

    /**
     * Svuota la coda di spawn.
     */
    public void clearQueue() {
        queue.clear();
    }

    /**
     * Rimuove tutte le entità dall'engine eccetto il player,
     * restituendo le istanze salvabili per il ripristino successivo.
     *
     * @return le istanze delle entità rimosse.
     */
    public Array<EntityInstance> clear() {
        Array<EntityInstance> instances = new Array<>();
        Array<Entity> entitiesCopy = new Array<>(entities);

        Player p = player();

        int count = 0;
        for (Entity e : entitiesCopy) {
            EntityInstance in = e.unregister();
            count++;
            if (in != null) {
                instances.add(in);
            }
        }

        entities.clear();
        queue.clear();

        entities.add(p);

        //Terminal.printError("Entità rimosse: " + count + instances);
        return instances;
    }

    /**
     * Salva tutte le entità marcate come Saveable.
     *
     * @return lista delle entità salvate come istanze.
     */
    public Array<EntityInstance> save() {
        Array<EntityInstance> instances = new Array<>();
        Array<Entity> entitiesCopy = new Array<>(entities);

        for (Entity e : entitiesCopy) {
            if (e.contains(Saveable.class)) {
                if (e instanceof BaseEnemy baseEnemy) {
                    EnemyInstance in = new EnemyInstance(baseEnemy);
                    instances.add(in);
                }
                if (e instanceof Boss boss) {
                    BossInstance in = new BossInstance(boss);
                    instances.add(in);
                }
            }
        }

        return instances;
    }

    /**
     * Aggiunge un'entità alla coda di spawn.
     *
     * @param e l'entità da spawnare.
     * @return l'entità aggiunta.
     */
    public Entity summon(Entity e) {
        queue.addFirst(e);
        return queue.first();
    }

    /**
     * Esegue lo spawn di un array di istanze di entità.
     *
     * @param instances le istanze da spawnare.
     */
    public void summon(Array<EntityInstance> instances) {
        for (EntityInstance instance : instances) {
            switch (instance) {
                case null -> {
                    continue;
                }
                case EnemyInstance enemyInstance -> summon(EntityFactory.createEnemy(instance.type, enemyInstance, this));
                case BossInstance bossInstance -> summon(EntityFactory.createBoss(instance.type, bossInstance, this));
                default -> {
                }
            }
        }
    }

    /**
     * Disegna tutte le entità ordinate e opzionalmente le pathfinding debug.
     */
    public void draw() {
        renderer.sort();
        if (DebugWindow.renderPathfinding()) renderer.drawPaths();
        systemManager.draw(Gdx.graphics.getDeltaTime(), entities);
    }

    /**
     * Aggiorna l'engine e tutti i sistemi per questo frame.
     *
     * @param delta il tempo trascorso dall'ultimo frame.
     */
    public void render(float delta) {
        this.delta = delta;
        this.elapsedTime += delta;
        for (Entity e : entities) {
            if (e.shouldAddToSystems()) {
                addEntityToSystems(e);
            }
        }
        systemManager.update(delta, entities);
        renderer.updateEntities();
    }

    /**
     * Restituisce il player attuale.
     *
     * @return l'entità player.
     */
    public Player player() {
        return game.getPlayer();
    }

    /**
     * Rimuove un'entità dal motore e dai sistemi.
     *
     * @param e l'entità da rimuovere.
     */
    public void remove(Entity e) {
        entities.removeValue(e, false);
        removeEntitiesFromSystems(e);
        entities.shrink();
    }

    /**
     * Processa la coda di spawn delle entità.
     */
    public void processQueue() {
        renderer.processQueue();
    }
}
