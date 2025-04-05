package progetto.gameplay.entity.types.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;

import progetto.gameplay.entity.types.abstractEntity.Entity;
import progetto.gameplay.entity.types.abstractEntity.EntityConfig;
import progetto.gameplay.entity.types.abstractEntity.EntityInstance;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.gameplay.manager.map.WorldManager;
import progetto.utils.BodyBuilder;

public class Bullet extends Entity {

    // === Attributi specifici ===
    public final float damage;
    private final float velocity;

    // === Costruttore ===
    public Bullet(EntityConfig config, EntityManager manager, float velocity, float damage) {
        super(config, manager);
        this.velocity = velocity;
        this.damage = damage;
    }

    // === Override metodi principali ===

    @Override
    public void updateEntity(float delta) {
        // Logica di aggiornamento del proiettile (vuota per ora)
    }

    @Override
    public void updateEntityType(float delta) {
        // Eventuale logica di aggiornamento legata al tipo (vuota per ora)
    }

    @Override
    public void create() {
        System.out.println("Proiettile creato");
    }

    @Override
    public void initBody() {
        bodyDef = BodyBuilder.createBodyDef(BodyDef.BodyType.KinematicBody, config().x, config().y);
        bodyDef.fixedRotation = true;
        bodyDef.position.set(config().x, config().y);

        shape = BodyBuilder.createCircle(0.1f);
        fixtureDef = BodyBuilder.createFixtureDef(shape, 1f, 0, 0);
        fixtureDef.isSensor = true;

        body = WorldManager.getInstance().createBody(bodyDef);
        body.setUserData(this);
        body.createFixture(fixtureDef);

        shape.dispose();

        body.setLinearVelocity(direzione().scl(velocity));
    }

    @Override
    public EntityInstance despawn() {
        System.out.println("Entità id " + id() + " despawnata");
        manager.removeEntity(this);
        Gdx.app.postRunnable(() -> WorldManager.getInstance().destroyBody(body));
        return null;
    }

    @Override
    public void setRendered(boolean rendered) {
        super.setRendered(rendered);
        if (!rendered) {
            despawn();
        }
    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        // Rendering del proiettile (non implementato)
    }
}
