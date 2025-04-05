package progetto.gameplay.entities.types;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import progetto.gameplay.WorldManager;
import progetto.gameplay.entities.types.entity.Entity;
import progetto.gameplay.entities.types.entity.EntityConfig;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.utils.BodyBuilder;
import progetto.utils.camera.CameraManager;

public class Bullet extends Entity {
    public final float damage;
    private final float velocity;

    public Bullet(EntityConfig config, EntityManager manager, float velocity, float damage) {
        super(config, manager);
        this.velocity = velocity;
        this.damage = damage;
    }

    @Override
    public void updateEntity(float delta) {

    }

    @Override
    public void setRendered(boolean rendered) {
        super.setRendered(rendered);
        if (!rendered) {
            despawn();
        }

    }

    public void draw(SpriteBatch batch, float elapsedTime) {

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
    public void updateEntityType(float delta) {
    }

    @Override
    public void create() {
        System.out.println("Proiettile creato");
    }

    @Override
    public void drawHitbox(ShapeRenderer renderer) {

    }
}
