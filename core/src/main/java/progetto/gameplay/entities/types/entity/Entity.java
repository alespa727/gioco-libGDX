package progetto.gameplay.entities.types.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.WorldManager;
import progetto.gameplay.entities.types.entity.state.EntityState;
import progetto.gameplay.entities.types.entity.texture.HumanTextures;
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
    public BodyDef bodyDef;
    public FixtureDef fixtureDef;
    public Shape shape;
    public EntityManager manager;
    float width, height;
    private boolean isRendered;
    private Node lastNode;
    private Node node;
    private EntityState stati;
    private HumanTextures textures;
    private float atkCooldown = 0;
    public boolean isLoaded = false;

    // Constructor
    public Entity(EntityConfig config, EntityManager manager) {
        this.config = config;
        this.manager = manager;
        this.nome = config.nome;

        this.coordinate = new Vector2(config.x, config.y);

        createBody();

        this.descrizione = config.descrizione;
        this.id = config.id;
        this.isRendered = false;

        this.direzione = new Vector2(config.direzione);
        textures = new HumanTextures(config.img);

        inizializzaStati(config.isAlive, config.inCollisione, config.isMoving);
    }

    public HumanTextures getTextures() {
        return textures;
    }

    // metodi astratti
    public abstract void updateEntity(float delta);

    public abstract void updateEntityType(float delta);

    public abstract void create();

    public abstract void drawHitbox(ShapeRenderer renderer);

    public void createBody() {
        this.width = config.width;
        this.height = config.height;
        // creo un corpo dinamico
        bodyDef = BodyBuilder.createBodyDef(BodyDef.BodyType.DynamicBody, config.x, config.y);

        // forma hitbox
        shape = BodyBuilder.createCircle(config.width);

        // proprietà fisiche
        fixtureDef = BodyBuilder.createFixtureDef(shape, 25f, .8f, .1f);
    }

    public void initBody(){
        body = BodyBuilder.createBody(this, bodyDef, fixtureDef, shape);
        body.setAngularDamping(5f);
        body.setLinearDamping(5f);
    }

    public boolean isRendered() {
        return isRendered;
    }

    public void setRendered(boolean rendered) {
        body.setActive(rendered);
        isRendered = rendered;
    }

    /**
     * disegna il nemico
     */
    public void draw(SpriteBatch batch, float elapsedTime) {
        this.elapsedTime = elapsedTime;

        batch.draw(textures.getAnimation(this).getKeyFrame(elapsedTime, true), getX(), getY(), config.imageWidth, config.imageHeight);

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
        if (isLoaded){
            this.delta = delta;

            updateEntityType(delta);
            updateEntity(delta);

            setCoordinate(body.getPosition().x, body.getPosition().y);
        }
    }

    public void despawn() {
        System.out.println("Entità id " + id() + " despawnata");
        manager.removeEntity(this);
        Gdx.app.postRunnable(() -> WorldManager.getInstance().destroyBody(body));
    }

    public void teleport(Vector2 pos) {
        if (isLoaded) body.setTransform(pos, 0f);
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
        if (body!=null) return body.getPosition();
        return coordinate;
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

    public float atkCooldown() {
        return atkCooldown;
    }

    public void setAtkCooldown(float atkCooldown) {
        this.atkCooldown = atkCooldown;
    }

    public void setDirezione(Vector2 direzione) {
        this.direzione.set(direzione);
    }

    public final void inizializzaStati(boolean isAlive, boolean inCollisione, boolean isMoving) {
        stati = new EntityState();
        stati.setIsAlive(isAlive);
        stati.setInCollisione(inCollisione);
        stati.setIsMoving(isMoving);
        stati.setImmortality(false);
    }
}
