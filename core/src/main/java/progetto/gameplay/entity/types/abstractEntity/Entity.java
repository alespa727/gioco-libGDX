package progetto.gameplay.entity.types.abstractEntity;

// Importazioni
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import progetto.gameplay.entity.types.humanEntity.HumanTextures;
import progetto.gameplay.manager.entity.EntityManager;
import progetto.gameplay.map.Map;
import progetto.gameplay.map.graph.node.Node;
import progetto.utils.BodyBuilder;

// Classe astratta Entity
public abstract class Entity {

    // === Attributi principali ===
    public EntityInstance instance;
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
    public final EntityManager manager;
    float width, height;

    // === Stato e gestione ===
    private boolean isRendered;
    private Node lastNode;
    private Node node;
    private final HumanTextures textures;
    private float atkCooldown = 0;
    public boolean isLoaded = false;
    private boolean isAlive;
    private boolean immortality;

    // === Costruttori ===
    public Entity(EntityInstance instance, EntityManager manager) {
        this.config = instance.config;
        this.manager = manager;
        this.nome = instance.nome;
        this.coordinate = instance.coordinate;
        createBody(coordinate.x, coordinate.y);
        this.descrizione = instance.descrizione;
        this.id = instance.id;
        this.isRendered = false;
        this.direzione = new Vector2(instance.direzione);
        textures = new HumanTextures(config.img);
        immortality = false;
        isAlive = true;
    }

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
        immortality = false;
        isAlive = true;
    }

    // === Metodi astratti da implementare ===
    public abstract void updateEntity(float delta);
    public abstract void updateEntityType(float delta);
    public abstract void create();
    public abstract EntityInstance despawn();

    // === Corpo e fisica ===
    public void createBody() {
        this.width = config.width;
        this.height = config.height;
        bodyDef = BodyBuilder.createBodyDef(BodyDef.BodyType.DynamicBody, config.x, config.y);
        shape = BodyBuilder.createCircle(config.width);
        fixtureDef = BodyBuilder.createFixtureDef(shape, 25f, .8f, .1f);
    }

    public void createBody(float x, float y) {
        this.width = config.width;
        this.height = config.height;
        bodyDef = BodyBuilder.createBodyDef(BodyDef.BodyType.DynamicBody, x, y);
        shape = BodyBuilder.createCircle(config.width);
        fixtureDef = BodyBuilder.createFixtureDef(shape, 25f, .8f, .1f);
    }

    public void initBody() {
        body = BodyBuilder.createBody(this, bodyDef, fixtureDef, shape);
        body.setAngularDamping(5f);
        body.setLinearDamping(5f);
    }

    // === Rendering ===
    public void draw(SpriteBatch batch, float elapsedTime) {
        this.elapsedTime = elapsedTime;
        batch.draw(textures.getAnimation(this).getKeyFrame(elapsedTime, true), getX(), getY(), config.imageWidth, config.imageHeight);
        batch.setColor(Color.WHITE);
    }

    public void drawShadow(ShapeRenderer renderer) {
        renderer.setColor(Color.BLACK);
        renderer.ellipse(getX(), getY() - 1f, 1f, 1f);
    }

    // === Logica di gioco ===
    public void render(float delta) {
        if (isLoaded) {
            this.delta = delta;
            updateEntityType(delta);
            updateEntity(delta);
            setCoordinate(body.getPosition().x, body.getPosition().y);
        }
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

    public void teleport(Vector2 pos) {
        if (isLoaded) body.setTransform(pos, 0f);
    }

    // === Getters e Setters ===
    public final EntityConfig config() { return new EntityConfig(config); }
    public final int id() { return id; }
    public final String nome() { return nome; }
    public final String descrizione() { return descrizione; }
    public final Vector2 direzione() { return this.direzione; }

    public final void setCoordinate(float x, float y) {
        this.coordinate.x = x - config().imageWidth / 2;
        this.coordinate.y = y - config().imageHeight / 2;
    }

    public final Vector2 getPosition() {
        if (body != null) return body.getPosition();
        return coordinate;
    }

    public final float getX() { return coordinate.x; }
    public final void setX(float x) { this.coordinate.x = x; }
    public final float getY() { return coordinate.y; }
    public final void setY(float y) { this.coordinate.y = y; }

    public float atkCooldown() { return atkCooldown; }
    public void setAtkCooldown(float atkCooldown) { this.atkCooldown = atkCooldown; }

    public void setDirezione(Vector2 direzione) {
        this.direzione.set(direzione);
    }

    public boolean isRendered() { return isRendered; }
    public void setRendered(boolean rendered) {
        body.setActive(rendered);
        isRendered = rendered;
    }

    public Node getNode() { return node; }
    public HumanTextures getTextures() { return textures; }

    // === Stato vitale ===
    public boolean isAlive() { return isAlive; }
    public void setAlive() { isAlive = true; }
    public void setDead() { isAlive = false; }

    public boolean isInvulnerable() { return immortality; }
    public void setInvulnerability(boolean invul) { immortality = invul; }
}
