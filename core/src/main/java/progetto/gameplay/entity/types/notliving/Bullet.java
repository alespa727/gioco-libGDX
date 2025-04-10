package progetto.gameplay.entity.types.notliving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import progetto.Core;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityConfig;
import progetto.gameplay.entity.types.EntityInstance;
import progetto.gameplay.manager.entity.ManagerEntity;
import progetto.gameplay.manager.ManagerWorld;
import progetto.utils.Cooldown;

public class Bullet extends Entity {

    // === Attributi specifici ===
    private final Entity owner;
    public final float damage;
    public final float velocity;
    public final float radius;

    private final Texture texture;
    private final Cooldown cooldown;
    private boolean cooldownActive;

    private boolean flag=false;

    // === Costruttore ===
    public Bullet(EntityConfig config, ManagerEntity manager, float radius, float velocity, float damage, Entity owner) {
        super(config, manager);
        this.velocity = velocity;
        this.damage = damage;
        this.radius = radius;
        this.owner = owner;
        this.texture = Core.assetManager.get("entities/circle.png", Texture.class);
        this.cooldownActive = false;
        this.cooldown = new Cooldown(2);
        this.cooldown.reset();
        this.direction.getDirection().set(config.direzione);
    }

    public void startCooldown(float time) {
        cooldownActive = true;
        this.cooldown.reset(time);
    }

    //METODO HIT PER DIFFERENZIARE I DIVERSI TIPI DI PROIETTILI

    public Entity getOwner() {
        return owner;
    }

    // === Override metodi principali ===

    @Override
    public void updateEntity(float delta) {
        if(cooldownActive) {
            cooldown.update(delta);
        }
        if (cooldown.isReady){
            despawn();
            System.out.println("DESPAWN");
        }

    }

    @Override
    public void updateEntityType(float delta) {
        // Eventuale logica di aggiornamento legata al tipo (vuota per ora)
    }

    @Override
    public void create() {
        physics.getBody().setLinearDamping(0f);
        physics.getBody().setLinearVelocity(new Vector2(getDirection()).scl(velocity));
        physics.getBody().getFixtureList().get(0).setSensor(true);
        physics.getBody().setUserData(this);

    }

    @Override
    public EntityInstance despawn() {
        System.out.println("DESPAWN");
        manager.remove(this);
        Body body = getPhysics().getBody();
        Gdx.app.postRunnable(() -> ManagerWorld.getInstance().destroyBody(body));
        return new EntityInstance(this);
    }

    @Override
    public void setShouldRender(boolean rendered) {
        super.setShouldRender(rendered);
        if (!rendered) {
            despawn();
        }
    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        Sprite sprite = new Sprite(texture);
        sprite.setSize(radius*2, radius*2);
        sprite.setPosition(getPosition().x- sprite.getWidth()/2, getPosition().y- sprite.getHeight()/2);
        sprite.draw(batch);
    }
}
