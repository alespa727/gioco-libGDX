package progetto.gameplay.entities.types.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.WorldManager;
import progetto.gameplay.entities.types.entity.graphics.EntityGraphics;
import progetto.gameplay.entities.types.entity.state.EntityState;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.gameplay.map.Map;
import progetto.gameplay.map.graph.node.Node;
import progetto.utils.BodyBuilder;

public abstract class Entity {

    public final int id;
    public final String nome;
    public final String descrizione;
    private final EntityConfig config;
    private final Vector2 coordinate;
    private final Vector2 direzione;
    public float delta;
    public float elapsedTime;
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

        body = createBody();

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

    public Body createBody() {
        this.width = config.width;
        this.height = config.height;
        // creo un corpo dinamico
        BodyDef bodyDef = BodyBuilder.createBodyDef(BodyDef.BodyType.DynamicBody, config.x, config.y);

        // forma hitbox
        Shape circleShape = BodyBuilder.createCircle(0.3f);

        // proprietà fisiche
        FixtureDef fixtureDef = BodyBuilder.createFixtureDef(circleShape, 25f, .8f, .1f);

        // creo la hitbox
        Body body = BodyBuilder.createBody(this, bodyDef, fixtureDef, circleShape);
        System.out.println("Creato corpo dinamico per " + nome());

        body.setLinearDamping(3f);
        body.setAngularDamping(5f);
        body.setBullet(true);

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
        this.elapsedTime = elapsedTime;
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
        node = Map.getGraph().getClosestNode(getPosition().x, getPosition().y);
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
        WorldManager.getInstance().destroyBody(body);
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

    public final Vector2 getPosition() {
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
