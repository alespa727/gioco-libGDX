package progetto.gameplay.manager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.Core;
import progetto.gameplay.GameInfo;
import progetto.factories.EntityConfigFactory;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.entity.types.living.combat.boss.BossInstance;
import progetto.gameplay.entity.types.notliving.Bullet;
import progetto.gameplay.entity.types.living.combat.Warriors;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.gameplay.entity.types.living.combat.enemy.Enemy;
import progetto.gameplay.entity.types.living.combat.enemy.EnemyInstance;
import progetto.gameplay.entity.types.living.combat.player.Player;
import progetto.menu.DefeatScreen;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.factories.EntityFactory;
import progetto.factories.BodyFactory;

import java.util.Comparator;

public final class ManagerEntity {

    public final GameInfo gameInfo;

    public static final short WALL = 0;
    public static final short RANGE = 0x0010;
    public static final short ENEMY = 0x0020;

    public final Comparator<Entity> comparator;

    private final Player player;
    private final Array<Entity> entityArray;
    private final Queue<Entity> entityQueue;
    public Array<EntityInstance> instances;

    private int entityId = 1;

    public ManagerEntity(GameInfo gameInfo) {

        this.gameInfo = gameInfo;
        comparator = (o1, o2) -> Float.compare(o2.getPosition().y, o1.getPosition().y);

        entityArray = new Array<>();
        entityQueue = new Queue<>();

        EntityConfig p = EntityConfigFactory.createPlayerConfig();
        player = new Player(p, this);

        summon(player);

        Core.assetManager.load("entities/nemico.png", Texture.class);
        Core.assetManager.finishLoading();

        EntityConfig e = EntityConfigFactory.createEntityConfig("Finn", 9, 8);

        int n = 0;
        for (int i = 0; i < n; i++) {
            entityId++;
            e.id = entityId;
            summon(EntityFactory.createEnemy("Finn", e, this, 1.5f));
        }
        e = EntityConfigFactory.createEntityConfig("Lich", 12, 12);
        summon(EntityFactory.createBoss("Lich", e, this));
    }

    public Array<EntityInstance> despawnEveryone() {
        instances = new Array<>();
        entityQueue.clear();
        for (int i = 0; i < entityArray.size; i++) {
            Entity e = entityArray.get(i);
            if (!player.equals(e) && !(e instanceof Bullet)) {
                System.out.println(e.getPosition());
                instances.add(e.despawn());
            }
        }
        System.out.println(instances.size);
        return instances;
    }

    public void clearQueue() {
        entityQueue.clear();
    }
    public void summon(Array<EntityInstance> instances) {
        for (EntityInstance instance : instances) {
            if (instance==null) continue;

            if (instance instanceof EnemyInstance) summon(EntityFactory.createEnemy(instance.type, (EnemyInstance) instance, this, 1.5f));
            if (instance instanceof BossInstance) summon(EntityFactory.createBoss(instance.type, (BossInstance) instance, this));
        }
    }

    public Entity summon(Entity e) {
        entityQueue.addFirst(e);
        return entityQueue.first();
    }

    public Bullet createBullet(float x, float y, Vector2 direction, float radius, float speed, float damage, Entity owner) {
        EntityConfig config = new EntityConfig();
        config.nome = "Bullet";
        config.x = x;
        config.y = y;
        config.descrizione = "fa male";
        config.direzione = direction;
        config.isAlive = true;
        config.img = Core.assetManager.get("entities/Finn.png", Texture.class);
        return (Bullet) summon(new Bullet(config, this, radius, speed, damage, owner));
    }

    public void draw(float elapsedTime) {
        gameInfo.core.batch.begin();
        entityArray.sort(comparator);
        for (Entity e : entityArray) {
            if (ManagerCamera.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y)) {
                try {
                    e.draw(gameInfo.core.batch, elapsedTime);
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.direzione());
                }
            }
        }
        for (Entity e : entityArray) {
            if (ManagerCamera.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y) && e instanceof Humanoid) {
                try {
                    ((Humanoid) e).getSkillset().draw(gameInfo.core.batch, elapsedTime);
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.direzione());
                }
            }
        }
        gameInfo.core.batch.end();
    }

    public void render(float delta) {
        if(!entityQueue.isEmpty()){
            entityArray.add(entityQueue.last());
            entityQueue.last().initBody();
            if (entityQueue.last() instanceof Warriors ce) {
                ce.directionalRange = BodyFactory.createBody(ce, ce.bodyDef, ce.fixtureDef, ce.shape);
            }
            entityQueue.last().create();
            entityQueue.removeLast().isLoaded = true;
        }

        if (!player.isAlive()) {
            gameInfo.core.setScreen(new DefeatScreen(gameInfo.core));
        }

        for (Entity e : entityArray) {
            if (ManagerCamera.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y) || e instanceof Player) {
                e.render(delta);
                e.setRendered(true);
                e.updateNode();
            } else e.setRendered(false);
        }
    }

    public void drawPath() {
        gameInfo.core.renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : entityArray) {
            if (!e.isRendered()){
                continue;
            }
            if (e instanceof Enemy enemy) {
                if (ManagerCamera.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y))
                    enemy.drawPath(gameInfo.core.renderer);
            }

        }
        gameInfo.core.renderer.end();
    }

    public void drawDebug() {
        drawPath();
    }

    public Player player() {
        return player;
    }

    public Array<Entity> entities() {
        return entityArray;
    }

    public void removeEntity(Entity e) {
        entityArray.removeValue(e, false);
        entityArray.shrink();
    }

}
