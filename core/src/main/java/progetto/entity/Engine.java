package progetto.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.core.Core;
import progetto.core.game.GameScreen;
import progetto.entity.entities.EntityManager;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.entities.base.EntityInstance;
import progetto.entity.entities.specific.living.combat.boss.BossInstance;
import progetto.entity.entities.specific.living.combat.enemy.EnemyInstance;
import progetto.entity.entities.specific.notliving.Casa;
import progetto.entity.systems.SystemManager;
import progetto.entity.systems.base.System;
import progetto.factories.EntityFactory;
import progetto.input.DebugWindow;
import progetto.input.TerminalCommand;
import progetto.player.Player;

public final class Engine {

    public static final short WALL = 0;
    public static final short RANGE = 0x0010;

    public final GameScreen game;
    private final EntityManager renderer;
    private final SystemManager systemManager;

    private final Array<Entity> entities;
    private final Queue<Entity> queue;

    public float delta;
    public float elapsedTime;

    public Engine(GameScreen gameScreen) {
        this.game = gameScreen;
        this.entities = new Array<>();
        this.queue = new Queue<>();
        this.systemManager = new SystemManager(this);
        this.renderer = new EntityManager(this);

    }

    public void addEntityToSystems(Entity e) {
        systemManager.addEntities(e);
    }

    public void removeEntitiesFromSystems(Entity e) {
        systemManager.removeEntities(e);
    }

    public void addSystem(System... systems) {
        systemManager.add(systems);
    }

    public Array<Entity> getEntities() {
        return entities;
    }

    public Queue<Entity> getQueue() {
        return queue;
    }

    public void clearQueue() {
        queue.clear();
    }

    public Array<EntityInstance> clear() {
        Array<EntityInstance> instances = new Array<>();
        Array<Entity> entitiesCopy = new Array<>(entities);

        Player p = player();

        int count=0;
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

        TerminalCommand.printError("Entità rimosse: " + count + instances);
        return instances;
    }

    public Entity summon(Entity e) {
        queue.addFirst(e);
        return queue.first();
    }

    public void summon(Array<EntityInstance> instances) {
        for (EntityInstance instance : instances) {
            if (instance == null) continue;

            if (instance instanceof EnemyInstance)
                summon(EntityFactory.createEnemy(instance.type, (EnemyInstance) instance, this, 1.5f));
            else if (instance instanceof BossInstance)
                summon(EntityFactory.createBoss(instance.type, (BossInstance) instance, this));
        }
    }

    public void draw() {
        renderer.sort();
        if (DebugWindow.renderPathfinding()) renderer.drawPaths();
        systemManager.draw(Gdx.graphics.getDeltaTime(), entities);
    }

    public void render(float delta) {
        this.delta = delta;
        this.elapsedTime += delta;
        systemManager.update(delta, entities);
        renderer.updateEntities();
    }

    public Player player() {
        return game.getPlayer();
    }

    public void remove(Entity e) {
        entities.removeValue(e, false);
        removeEntitiesFromSystems(e);

        entities.shrink();
    }

    public void processQueue(){
        renderer.processQueue();
    }
}
