package progetto.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.core.Core;
import progetto.core.game.GameInfo;
import progetto.entity.systems.specific.*;
import progetto.factories.EntityConfigFactory;
import progetto.factories.EntityFactory;
import progetto.entity.entities.base.Entity;
import progetto.entity.entities.base.EntityConfig;
import progetto.entity.entities.base.EntityInstance;
import progetto.entity.entities.specific.living.combat.boss.BossInstance;
import progetto.entity.entities.specific.living.combat.enemy.EnemyInstance;
import progetto.entity.entities.EntityManager;
import progetto.entity.systems.SystemManager;
import progetto.player.Player;
import progetto.player.PlayerManager;
import progetto.entity.systems.base.System;
import progetto.input.DebugWindow;
import progetto.input.TerminalCommand;

public final class Engine {

    public static final short WALL = 0;
    public static final short RANGE = 0x0010;

    public final GameInfo info;
    private final EntityManager renderer;
    private final SystemManager systemManager;
    private final PlayerManager playerManager;

    private final Array<Entity> entities;
    private final Queue<Entity> queue;

    public float delta;
    public float elapsedTime;

    public Engine(GameInfo info) {
        this.info = info;
        this.entities = new Array<>();
        this.queue = new Queue<>();
        this.systemManager = new SystemManager(this);
        this.playerManager = new PlayerManager(this);

        this.summon(playerManager.getPlayer());

        Core.assetManager.load("entities/Lich.png", Texture.class);
        Core.assetManager.load("entities/nemico.png", Texture.class);
        Core.assetManager.finishLoading();

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 1; j++) {
                EntityConfig e = EntityConfigFactory.createEntityConfig("Finn", 8 + i * 0.3f, 10 + j * 0.3f);
                summon(EntityFactory.createEnemy("Finn", e, this, 4));
                summon(EntityFactory.createSword(10, 10, 0.2f, 1f, new Vector2(0, -0.5f), 50, this, null));
            }
        }

        this.renderer = new EntityManager(this);


        addSystem(
            new CooldownSystem(),
            new UserInputSystem(),
            new PlayerSystem(),
            new DeathSystem(),
            new MovementSystem(),
            new SpeedLimiterSystem(),
            new NodeTrackerSystem(),
            new StatemachineSystem(),
            new SkillSystem(),
            new RangeSystem(),
            new HitSystem(),
            new KnockbackSystem()
        );
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

        Player p = playerManager.getPlayer();

        for (Entity e : entitiesCopy) {
            EntityInstance in = e.unregister();
            if (in != null) {
                instances.add(in);
            }
        }

        entities.clear();
        queue.clear();

        entities.add(p);

        TerminalCommand.printError("Entità rimosse: " + instances.size + instances);
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
        return playerManager.getPlayer();
    }

    public void remove(Entity e) {
        removeEntitiesFromSystems(e);
        entities.removeValue(e, false);
        entities.shrink();
    }
}
