package progetto.gameplay.entity.types.notliving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;

import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.manager.ManagerEntity;
import progetto.gameplay.manager.ManagerWorld;
import progetto.factories.BodyFactory;

public class Bullet extends Entity {

    // === Attributi specifici ===
    public final float damage;
    private final float velocity;
    private final float radius;
    private final Entity owner;

    // === Costruttore ===
    public Bullet(EntityConfig config, ManagerEntity manager, float radius, float velocity, float damage, Entity owner) {
        super(config, manager);
        this.velocity = velocity;
        this.damage = damage;
        this.radius = radius;
        this.owner = owner;
    }

    public Entity getOwner() {
        return owner;
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
        //System.out.println("Proiettile creato");
    }

    @Override
    public void initBody() {
        bodyDef = BodyFactory.createBodyDef(BodyDef.BodyType.KinematicBody, config().x, config().y);
        bodyDef.fixedRotation = true;
        bodyDef.position.set(config().x, config().y);

        shape = BodyFactory.createCircle(radius);
        fixtureDef = BodyFactory.createFixtureDef(shape, 1f, 0, 0);
        fixtureDef.isSensor = true;

        body = ManagerWorld.getInstance().createBody(bodyDef);
        body.setUserData(this);
        body.createFixture(fixtureDef);

        shape.dispose();

        body.setLinearVelocity(direzione().scl(velocity));
    }

    @Override
    public EntityInstance despawn() {
        System.out.println("Entità id " + id() + " despawnata");
        manager.removeEntity(this);
        Gdx.app.postRunnable(() -> ManagerWorld.getInstance().destroyBody(body));
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
