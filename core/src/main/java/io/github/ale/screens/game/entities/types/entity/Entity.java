package io.github.ale.screens.game.entities.types.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import io.github.ale.screens.game.entities.types.entity.graphics.EntityGraphics;
import io.github.ale.screens.game.entities.types.entity.state.EntityState;
import io.github.ale.screens.game.manager.entity.EntityManager;
import io.github.ale.screens.game.map.Map;
import io.github.ale.screens.game.map.graph.node.Node;

public abstract class Entity {

    public final int id;
    public final String nome;
    public final String descrizione;
    private final EntityConfig config;
    private final Vector2 coordinate;
    private final Vector2 direzione;
    public float delta;
    public Body body;
    public EntityManager manager;
    float width, height;
    float texturewidth, textureheight;
    private boolean isRendered;
    private Node lastNode;
    private Node node;
    private EntityState stati;
    private EntityGraphics graphics;
    private float atkCooldown = 0;

    // Constructor
    public Entity(EntityConfig config, EntityManager manager) {
        this.config = config;
        this.manager = manager;
        this.nome = config.nome;

        this.coordinate = new Vector2();

        this.body = createBody(config.x, config.y, config.width, config.height);

        this.descrizione = config.descrizione;
        this.id = config.id;
        this.isRendered = false;

        this.direzione = new Vector2(config.direzione);

        inizializzaEntityGraphics();

        graphics.setTexture(config.img);

        inizializzaStati(config.isAlive, config.inCollisione, config.isMoving);
        inizializzaAnimazione();
        Gdx.app.postRunnable(this::create);
    }

    // metodi astratti
    public abstract void updateEntity(float delta);

    public abstract void updateEntityType(float delta);

    public abstract void create();

    public abstract void drawHitbox(ShapeRenderer renderer);

    public Body createBody(float x, float y, float width, float height) {
        this.width = width;
        this.height = height;
        // creo un corpo dinamico
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // creo la hitbox
        Body body = manager.world.createBody(bodyDef);
        body.setTransform(x, y, 0f);
        System.out.println("Creato corpo dinamico per " + nome());
        body.setUserData(this);
        body.setLinearDamping(3f);

        // forma hitbox
        CircleShape boxShape = new CircleShape();
        boxShape.setRadius(0.3f);

        // proprietà fisiche
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.density = 25f;      // Set density (mass per area)
        fixtureDef.friction = 0.8f;   // Set friction (sliding resistance)
        fixtureDef.restitution = 0.1f; // Set restitution ("bounciness")

        // aggiungo le proprietà fisiche
        body.createFixture(fixtureDef);
        body.setAngularDamping(5f);
        body.setBullet(true);

        // disposa
        boxShape.dispose();
        body.setActive(true);
        return body;
    }

    public boolean isRendered() {
        return isRendered;
    }

    public Entity setRendered(boolean rendered) {
        body.setActive(rendered);
        isRendered = rendered;
        return this;
    }

    /**
     * disegna il nemico
     */
    public void draw(SpriteBatch batch, float elapsedTime) {
        graphics.setAnimation(this);

        batch.draw(graphics.getAnimazione().getKeyFrame(elapsedTime, true), getX(), getY(), config.imageWidth, config.imageHeight);

        batch.setColor(Color.WHITE);
    }

    public void drawShadow(ShapeRenderer renderer) {
        renderer.setColor(Color.BLACK);
        renderer.ellipse(getX(), getY() - 1f, 1f, 1f);
    }

    public void updateNode() {
        if (lastNode != null && node != null) {
            lastNode.setWalkable(true);
        }
        if (node != null) {
            lastNode = node;
        }
        node = Map.getGraph().getClosestNode(coordinateCentro().x, coordinateCentro().y);
        if (node != null) {
            node.setWalkable(false);
        }

    }

    public Node getNode() {
        return node;
    }

    // Core methods
    public void render(float delta) {
        this.delta = delta;

        updateEntityType(delta);
        updateEntity(delta);

        setCoordinate(body.getPosition().x, body.getPosition().y);
    }

    public void despawn() {
        System.out.println("Entità id " + id() + " despawnata");
        manager.removeEntity(this);
        manager.world.destroyBody(body);
    }

    public void teleport(Vector2 pos) {
        body.setTransform(pos, 0f);
    }

    // Getters and setters
    public final EntityConfig config() {
        return new EntityConfig(config);
    }

    public final int id() {
        return id;
    }

    public final String nome() {
        return nome;
    }

    public final String descrizione() {
        return descrizione;
    }

    public final Vector2 direzione() {
        return this.direzione;
    }

    public final EntityState stati() {
        return stati;
    }

    public final Vector2 coordinate() {
        return coordinate;
    }

    public final void setCoordinate(float x, float y) {
        this.coordinate.x = x - config().imageWidth / 2;
        this.coordinate.y = y - config().imageHeight / 2;
    }

    public final Vector2 coordinateCentro() {
        return body.getPosition();
    }

    public final float getX() {
        return coordinate.x;
    }

    public final void setX(float x) {
        this.coordinate.x = x;
    }

    public final float getY() {
        return coordinate.y;
    }

    public final void setY(float y) {
        this.coordinate.y = y;
    }

    public final EntityGraphics graphics() {
        return this.graphics;
    }

    public float atkCooldown() {
        return atkCooldown;
    }

    public void setAtkCooldown(float atkCooldown) {
        this.atkCooldown = atkCooldown;
    }

    public void setDirezione(Vector2 direzione) {
        this.direzione.set(direzione);
    }

    // Initialization methods
    public final void inizializzaEntityGraphics() {
        this.graphics = new EntityGraphics();
    }

    public final void inizializzaStati(boolean isAlive, boolean inCollisione, boolean isMoving) {
        stati = new EntityState();
        stati.setIsAlive(isAlive);
        stati.setInCollisione(inCollisione);
        stati.setIsMoving(isMoving);
        stati.setImmortality(false);
    }

    public final void inizializzaAnimazione() {
        this.graphics.inizializzaAnimazione(this);
    }

}
