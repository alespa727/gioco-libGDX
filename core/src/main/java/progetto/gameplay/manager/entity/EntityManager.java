package progetto.gameplay.manager.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import progetto.Game;
import progetto.gameplay.GameInfo;
import progetto.gameplay.entities.types.*;
import progetto.menu.DefeatScreen;
import progetto.gameplay.entities.types.entity.Entity;
import progetto.gameplay.entities.types.entity.EntityConfig;
import progetto.gameplay.entities.factories.EnemyFactory;
import progetto.utils.BodyBuilder;
import progetto.utils.camera.CameraManager;

import java.util.Comparator;

public final class EntityManager {

    public GameInfo gameInfo;

    public static final short WALL = 0;
    public static final short RANGE = 0x0010;
    public static final short ENEMY = 0x0020;

    public final Comparator<Entity> comparator;

    private final Player player;
    private final Array<Entity> entityArray;
    private final Array<Bullet> bulletArray;
    private Queue<Entity> entityQueue;
    private Queue<Bullet> bulletQueue;

    private int nextEntityId = 0;

    public EntityConfig e;

    public EntityManager(GameInfo gameInfo) {

        this.gameInfo = gameInfo;
        comparator = (o1, o2) -> Float.compare(o2.getPosition().y, o1.getPosition().y);

        entityArray = new Array<>();
        bulletArray = new Array<>();
        entityQueue = new Queue<>();
        bulletQueue = new Queue<>();

        EntityConfig p = new EntityConfig();
        p.id = nextEntityId;
        p.nome = "player";
        p.x = 8.5f;
        p.y = 5.5f;
        p.img = Game.assetManager.get("entities/Finn.png", Texture.class);
        p.width = 10 / 32f;
        p.height = 8 / 16f;
        p.offsetX = 0;
        p.offsetY = -0.25f;
        p.direzione = new Vector2(0, -0.5f);
        p.isAlive = true;
        p.inCollisione = false;
        p.isMoving = false;
        p.hp = 100;
        p.speed = 2.5f;
        p.attackdmg = 10;
        p.imageHeight = 2f;
        p.imageWidth = 2f;

        player = new Player(p, this);

        nextEntityId++;

        summon(player);

        Game.assetManager.load("entities/nemico.png", Texture.class);
        Game.assetManager.finishLoading();

        e = new EntityConfig();
        e.nome = "Finn";
        e.descrizione = "Nemico pericoloso";
        e.x = 9.01f;
        e.y = 8.01f;
        e.img = Game.assetManager.get("entities/nemico.png", Texture.class);
        e.width = 6 / 32f;
        e.height = 8 / 16f;
        e.offsetX = 0;
        e.offsetY = -0.25f;
        e.direzione = new Vector2(0, -0.5f);
        e.isAlive = true;
        e.inCollisione = false;
        e.isMoving = false;
        e.hp = 100;
        e.speed = 1.25f;
        e.attackdmg = 20;
        e.imageHeight = 2f;
        e.imageWidth = 2f;

        int n = 1;
        for (int i = 0; i < n; i++) {
            nextEntityId++;
            e.id = nextEntityId;
            summon(EnemyFactory.createEnemy("Finn", this.e, this, 1.5f));

        }
        System.out.println(player.getPosition());
    }

    public void summon(Entity e) {
        entityQueue.addFirst(e);
    }


    public void createBullet(float x, float y, Vector2 direction, float speed, float damage) {
        EntityConfig config = new EntityConfig();
        config.nome = "Bullet";
        config.x = x;
        config.y = y;
        config.descrizione = "fa male";
        config.direzione = direction;
        config.isAlive = true;
        config.img = Game.assetManager.get("entities/Finn.png", Texture.class);
        summon(new Bullet(config, this, speed, damage));
    }

    public void draw(float elapsedTime) {
        gameInfo.game.batch.begin();
        entityArray.sort(comparator);
        for (Entity e : entityArray) {
            if (CameraManager.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y)) {
                try {
                    e.draw(gameInfo.game.batch, elapsedTime);
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.direzione());
                }
            }
        }
        for (Entity e : entityArray) {
            if (CameraManager.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y) && e instanceof HumanEntity) {
                try {
                    ((HumanEntity) e).getSkillset().draw(gameInfo.game.batch, elapsedTime);
                } catch (Exception ex) {
                    System.out.println("ERRORE" + e.direzione());
                }
            }
        }
        gameInfo.game.batch.end();
    }

    public void render(float delta) {
        if(!entityQueue.isEmpty()){
            entityArray.add(entityQueue.last());
            entityQueue.last().initBody();
            if (entityQueue.last() instanceof CombatEntity) {
                CombatEntity ce = (CombatEntity) entityQueue.last();
                ce.range = BodyBuilder.createBody(ce, ce.bodyDef, ce.fixtureDef, ce.shape);
            }
            entityQueue.last().create();
            entityQueue.removeLast().isLoaded = true;
        }

        if (!player.stati().isAlive()) {
            gameInfo.game.setScreen(new DefeatScreen(gameInfo.game));
        }

        for (Entity e : entityArray) {
            if (CameraManager.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y) || e instanceof Player) {
                e.render(delta);
                e.setRendered(true);
                e.updateNode();
            } else e.setRendered(false);
        }
    }

    public void drawPath() {
        gameInfo.game.renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : entityArray) {
            if (!e.isRendered()){
                continue;
            }
            if (e instanceof Enemy enemy) {
                if (CameraManager.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y))
                    enemy.drawPath(gameInfo.game.renderer);
            }

        }
        gameInfo.game.renderer.end();
    }

    public void drawHitbox() {
        gameInfo.game.renderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entity e : entityArray) {
            if (!e.isRendered() || !e.isLoaded){
                continue;
            }
            if (e.stati().inCollisione()) gameInfo.game.renderer.setColor(Color.RED);
            else gameInfo.game.renderer.setColor(Color.BLACK);
            if (CameraManager.isWithinFrustumBounds(e.getPosition().x, e.getPosition().y))
                e.drawHitbox(gameInfo.game.renderer);
            gameInfo.game.renderer.setColor(Color.WHITE);
            gameInfo.game.renderer.rectLine(e.body.getPosition(), player.body.getPosition(), 0.1f);
        }
        gameInfo.game.renderer.end();
    }

    public void drawDebug() {
        drawHitbox();
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
