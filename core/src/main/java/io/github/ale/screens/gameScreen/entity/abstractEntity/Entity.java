package io.github.ale.screens.gameScreen.entity.abstractEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.gameScreen.entity.EntityManager;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Dimensioni;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.EntityInfo;
import io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche.Hitbox;
import io.github.ale.screens.gameScreen.entity.abstractEntity.graphics.EntityGraphics;
import io.github.ale.screens.gameScreen.entity.abstractEntity.state.Direzione;
import io.github.ale.screens.gameScreen.entity.abstractEntity.state.EntityState;
import io.github.ale.screens.gameScreen.entity.abstractEntity.stats.Stats;
import io.github.ale.screens.gameScreen.maps.Map;

public abstract class Entity{

    // Fields
    private final EntityConfig config;
    private EntityInfo info;
    private Dimensioni size;
    private EntityState stati;
    private Stats statistiche;
    private Vector2 coordinate;
    private Direzione direzione;
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
        getEntityGraphics().setTexture(config.imgpath);
        inizializzaHitbox(getX(), getY(), config.width, config.height);
        inizializzaDirezione(config.direzione);
        inizializzaStati(config.isAlive, config.inCollisione, config.isMoving);
        inizializzaStatistiche(config.hp, config.speed, config.attackdmg);
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

        setAnimation();
        if(statistiche().gotDamaged){
            batch.setColor(1, 0, 0, 0.6f);
        }
        
        batch.draw(getAnimazione().getKeyFrame(elapsedTime, true), getX(), getY(), getSize().width, getSize().height);
        
        
        batch.setColor(Color.WHITE);
    }

    // Core methods
    public void render() {
        delta = Gdx.graphics.getDeltaTime();
        updateEntity();
        updateEntityType();
    }

    public void kill() {
        statistiche().inflictDamage(statistiche().health(), stati.immortality());
        stati.setIsAlive(false);
    }
    
    public boolean checkIfDead() {
        if (statistiche().health() <= 0) {
            this.stati().setIsAlive(false);
            despawn();
        }
        return this.stati().isAlive();
    }

    public void respawn() {
        stati().setIsAlive(config.isAlive);
        this.statistiche().gotDamaged = false;
    }

    public void despawn() {
        System.out.println("Entità id " + id() + " despawnata");
        manager.despawn(this);
    }

    public void collisionientita() {
        //DA FARE
    }

    public void limiti() {
        setX(MathUtils.clamp(getX(), 0 - 0.65f, Map.width() - hitbox().width - hitbox().width));
        setY(MathUtils.clamp(getY(), 0 - 0.55f, Map.height() - hitbox().height - hitbox().height));
    }

    // Getters and setters
    public EntityConfig config() {
        return new EntityConfig(config);
    }

    public int id() {
        return info.id();
    }

    public String nome() {
        return this.info.getNome();
    }

    public String descrizione() {
        return this.info.getDescrizione();
    }

    public Vector2 direzione() {
        return this.direzione.getDirezione();
    }

    public Stats statistiche() {
        return this.statistiche;
    }

    public Rectangle hitbox() {
        return this.hitbox.getHitbox();
    }

    public EntityState stati() {
        return stati;
    }

    public Vector2 coordinate() {
        return coordinate;
    }

    public Vector2 coordinateCentro() {
        return new Vector2(getX() + getSize().width / 2, getY() + getSize().height / 2);
    }

    public void setCentro(Vector2 punto) {
        coordinate.set(punto.x + getSize().width / 2, punto.y + getSize().height / 2);
    }

    public final float getX() {
        return coordinate.x;
    }

    public void setX(float x) {
        this.coordinate.x = x;
    }

    public final float getY() {
        return coordinate.y;
    }

    public void setY(float y) {
        this.coordinate.y = y;
    }

    public Dimensioni getSize() {
        return size;
    }

    public void setSize(Dimensioni size) {
        this.size = size;
    }

    public Animation<TextureRegion> getAnimazione() {
        return this.graphics.getAnimazione();
    }

    public void setAnimation() {
        this.graphics.setAnimation(this);
    }

    private EntityGraphics getEntityGraphics() {
        return graphics;
    }

    public float atkCooldown() {
        return atkCooldown;
    }

    public void setAtkCooldown(float atkCooldown) {
        this.atkCooldown = atkCooldown;
    }

    public void setDirezione(Vector2 direzione) {
        this.direzione.setDirezione(direzione);
    }

    public float calcolaAngolo(float x1, float y1, float x2, float y2) {
        float deltaX = x2 - x1;
        float deltaY = y2 - y1;
        float angle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
        return angle < 0 ? angle + 360 : angle;
    }

    // Initialization methods
    public final void inizializzaEntityGraphics() {
        this.graphics = new EntityGraphics();
    }

    public final void inizializzaInfo(String nome, String descrizione, int id) {
        this.info = new EntityInfo(nome, descrizione, id);
    }

    public final void inizializzaCoordinate(float x, float y) {
        coordinate = new Vector2(x - size.width / 4, y - size.height / 4);
    }

    public final void inizializzaHitbox(float x, float y, float width, float height) {
        hitbox = new Hitbox(x, y, width, height);
    }

    public final void inizializzaDirezione(Vector2 direzione) {
        this.direzione = new Direzione();
        this.direzione.setDirezione(direzione);
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

    public final void inizializzaStatistiche(float hp, float speed, float attackdmg) {
        this.statistiche = new Stats(hp, speed, attackdmg);
    }

    public void adjustHitbox() {
        this.hitbox.adjust(this);
    }
}
