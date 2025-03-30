package io.github.ale.screens.game.entityType.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.*;
import io.github.ale.screens.game.entityType.EntityManager;
import io.github.ale.screens.game.entityType.entity.caratteristiche.Dimensioni;
import io.github.ale.screens.game.entityType.entity.graphics.EntityGraphics;
import io.github.ale.screens.game.entityType.entity.state.EntityState;
import io.github.ale.screens.game.maps.Map;
import io.github.ale.screens.game.pathfinding.Node;

public abstract class Entity{

    public float delta;

    public Body body;
    float width, height;

    public final int id;
    public final String nome;
    public final String descrizione;

    private boolean isRendered;

    private final EntityConfig config;
    private Node lastNode;
    private Node node;
    private Dimensioni size;
    private EntityState stati;
    private Vector2 coordinate;
    private Vector2 direzione;
    private EntityGraphics graphics;

    public EntityManager manager;

    private float atkCooldown = 0;

    // Constructor
    public Entity(EntityConfig config, EntityManager manager) {
        this.config = config;
        this.manager = manager;
        this.nome = config.nome;

        this.body = createBody(config.x, config.y, config.width, config.height);

        this.descrizione = config.descrizione;
        this.id = config.id;
        this.isRendered = false;

        inizializzaEntityGraphics();
        inizializzaDimensione(new Dimensioni(config.imageWidth, config.imageHeight));

        inizializzaCoordinate(config.x, config.y);
        graphics.setTexture(config.img);
        inizializzaDirezione(config.direzione);
        inizializzaStati(config.isAlive, config.inCollisione, config.isMoving);
        inizializzaAnimazione();
        Gdx.app.postRunnable(this::create);
    }

    // metodi astratti
    public abstract void updateEntity(float delta);
    public abstract void updateEntityType(float delta);
    public abstract void create();
    public abstract void drawHitbox(ShapeRenderer renderer);

    public Body createBody(float x, float y, float width, float height){
        this.width=width;
        this.height=height;
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
        body.setFixedRotation(false);
        body.setBullet(true);

        // disposa
        boxShape.dispose();
        return body;
    }

    public void setRendered(boolean rendered) {
        isRendered = rendered;
    }

    public boolean isRendered() {
        return isRendered;
    }

    /**
     * disegna il nemico
     */
    public void draw(SpriteBatch batch, float elapsedTime) {
        graphics.setAnimation(this);

        batch.draw(graphics.getAnimazione().getKeyFrame(elapsedTime, true), getX(), getY(), getSize().width, getSize().height);

        batch.setColor(Color.WHITE);
    }

    public void updateNode(){
        if (lastNode!=null && node!=null){
            lastNode.setWalkable(true);
        }
        if (node!=null) {
            lastNode = node;
        }
        node = Map.getGraph().getClosestNode(coordinateCentro().x, coordinateCentro().y);
        if (node != null) {
            node.setWalkable(false);
        }

    }

    public Node getNode(){
        return node;
    }

    // Core methods
    public void render(float delta) {
        this.delta = delta;
        updateEntity(delta);
        updateEntityType(delta);
        setCoordinate(body.getPosition().x, body.getPosition().y);
    }

    public void despawn() {
        System.out.println("Entità id " + id() + " despawnata");
        manager.removeEntity(this);
        manager.world.destroyBody(body);
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
        this.coordinate.x =x-getSize().width/2;
        this.coordinate.y = y-getSize().height/2;
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

    public final Dimensioni getSize() {
        return this.size;
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

    public final void inizializzaCoordinate(float x, float y) {
        coordinate = new Vector2(x - size.width/2, y - size.height/2);
    }

    public final void inizializzaDirezione(Vector2 direzione) {
        this.direzione = new Vector2(direzione);
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

    public final void inizializzaDimensione(Dimensioni size) {
        this.size = size;
    }

}
