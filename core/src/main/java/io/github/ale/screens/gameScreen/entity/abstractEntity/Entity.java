package io.github.ale.screens.gameScreen.entity.abstractEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Dimensioni;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.EntityInfo;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Hitbox;
import io.github.ale.screens.gameScreen.entity.abstractEntity.graphics.EntityGraphics;
import io.github.ale.screens.gameScreen.entity.abstractEntity.state.EntityState;
import io.github.ale.screens.gameScreen.maps.Map;

public abstract class Entity{

    // Fields
    private final EntityConfig config;
    private EntityInfo info;
    private Dimensioni size;
    private EntityState stati;
    private Vector2 coordinate;
    private Vector2 direzione;
    private Hitbox hitbox;
    private EntityGraphics graphics;

    public EntityManager manager;

    private float atkCooldown = 0;
    public float delta;
    

    // Constructor
    public Entity(EntityConfig config, EntityManager manager) {
        this.config = config;
        delta = Gdx.graphics.getDeltaTime();
        this.manager = manager;
        inizializzaEntityGraphics();
        inizializzaDimensione(new Dimensioni(config.imageWidth, config.imageHeight));
        inizializzaCoordinate(config.x, config.y);
        inizializzaInfo(config.nome, config.descrizione, config.id);
        graphics.setTexture(config.imgpath);
        inizializzaHitbox(getX(), getY(), config.width, config.height, config.offsetX, config.offsetY);
        inizializzaDirezione(config.direzione);
        inizializzaStati(config.isAlive, config.inCollisione, config.isMoving);
        inizializzaAnimazione();
        Gdx.app.postRunnable(this::create);
    }

    // Abstract methods
    public abstract void updateEntity();
    public abstract void updateEntityType();
    public abstract void create();
    public abstract void drawHitbox(ShapeRenderer renderer);

    
    /**
     * disegna il nemico
     * 
     * @param batch
     * @param elapsedTime
     */
    public void draw(SpriteBatch batch, float elapsedTime) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        graphics.setAnimation(this);
        
        batch.draw(graphics.getAnimazione().getKeyFrame(elapsedTime, true), getX(), getY(), getSize().width, getSize().height);
    
        batch.setColor(Color.WHITE);
    }

    // Core methods
    public void render() {
        delta = Gdx.graphics.getDeltaTime();
        updateEntity();
        updateEntityType();
    }

    public void despawn() {
        System.out.println("Entità id " + id() + " despawnata");
        manager.despawn(this);
    }

    public void collisionientita() {
        //DA FARE
    }

    public void limiti() {
        setX(MathUtils.clamp(getX(), 0 - hitbox().width, Map.width() - hitbox().width - hitbox().width));
        setY(MathUtils.clamp(getY(), 0 -hitbox().height, Map.height() - hitbox().height - hitbox().height));
    }

    // Getters and setters
    public final EntityConfig config() {
        return new EntityConfig(config);
    }

    public final int id() {
        return info.id();
    }

    public final String nome() {
        return this.info.getNome();
    }

    public final String descrizione() {
        return this.info.getDescrizione();
    }

    public final Vector2 direzione() {
        return this.direzione;
    }

    public final Rectangle hitbox() {
        return this.hitbox.getHitbox();
    }

    public final EntityState stati() {
        return stati;
    }

    public final Vector2 coordinate() {
        return coordinate;
    }

    public final Vector2 coordinateCentro() {
        return new Vector2(hitbox().x+hitbox().width/2, hitbox().y+hitbox().height/2);
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

    public final void inizializzaInfo(String nome, String descrizione, int id) {
        this.info = new EntityInfo(nome, descrizione, id);
    }

    public final void inizializzaCoordinate(float x, float y) {
        coordinate = new Vector2(x - size.width/2, y - size.height/2);
    }

    public final void inizializzaHitbox(float x, float y, float width, float height, float offsetX, float offsetY) {
        hitbox = new Hitbox(x, y, width, height, offsetX, offsetY);
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

    public void adjustHitbox() {
        this.hitbox.adjust(this);
    }
}
