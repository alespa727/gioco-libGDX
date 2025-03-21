package io.github.ale.screens.gameScreen.entity.abstractEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import io.github.ale.screens.gameScreen.interfaces.Creatable;
import io.github.ale.screens.gameScreen.interfaces.Drawable;
import io.github.ale.screens.gameScreen.maps.Map;

public abstract class Entity implements Drawable, Creatable {

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
    protected Rectangle range;

    private float atkCooldown = 0;
    private float countdownKnockback = 0.273f;
    public float delta;
    private float dx, dy, x = 0, y = 0, angolo;

    // Constructor
    public Entity(EntityConfig config) {
        this.config = config;
        delta = Gdx.graphics.getDeltaTime();
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
    }

    // Abstract methods
    public abstract void updateEntity();
    public abstract void updateEntityType();
    public abstract void drawRange(ShapeRenderer renderer);

    // Core methods
    public void render() {
        delta = Gdx.graphics.getDeltaTime();
        updateEntity();
        updateEntityType();
    }

    public void kill() {
        statistiche().inflictDamage(statistiche().getHealth(), stati.immortality());
        stati.setIsAlive(false);
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
        if (manager.entita(hitbox()).size > 0) {
            for (int i = 0; i < manager.entita(hitbox()).size; i++) {
                coordinate().set(
                    getX() - direzione().x * statistiche().getSpeed() * 1.5f * delta,
                    getY() - direzione().y * statistiche().getSpeed() * 1.5f * delta
                );
            }
        }
    }

    public void limiti() {
        setX(MathUtils.clamp(getX(), 0 - 0.65f, Map.getWidth() - hitbox().width - hitbox().width));
        setY(MathUtils.clamp(getY(), 0 - 0.55f, Map.getHeight() - hitbox().height - hitbox().height));
    }

    public void hit(float angolo, float damage) {
        statistiche().inflictDamage(damage, false);
        dx = (float) Math.cos(Math.toRadians(angolo)) * 6f;
        dy = (float) Math.sin(Math.toRadians(angolo)) * 6f;
        countdownKnockback = 0.5f;
        this.angolo = angolo;
        knockback();
    }

    protected void knockback() {
        delta = Gdx.graphics.getDeltaTime();
        if (countdownKnockback >= 0f) {
            countdownKnockback -= delta;
            dx *= 0.9;
            dy *= 0.9;
            if (!Map.checkCollisionX(this, 0.1f, angolo)) {
                x = dx * delta;
                setX(getX() + x);
            }
            if (!Map.checkCollisionY(this, 0.1f, angolo)) {
                y = dy * delta;
                setY(getY() + y);
            }
        } else {
            x = 0;
            y = 0;
        }
    }

    // Getters and setters
    public EntityConfig config() {
        return this.config;
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

    @Override
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
        return new Vector2(getX() + getSize().getWidth() / 2, getY() + getSize().getHeight() / 2);
    }

    public void setCentro(Vector2 punto) {
        coordinate.set(punto.x + getSize().getWidth() / 2, punto.y + getSize().getHeight() / 2);
    }

    @Override
    public float getX() {
        return coordinate.x;
    }

    public void setX(float x) {
        this.coordinate.x = x;
    }

    @Override
    public float getY() {
        return coordinate.y;
    }

    public void setY(float y) {
        this.coordinate.y = y;
    }

    @Override
    public Dimensioni getSize() {
        return size;
    }

    public void setSize(Dimensioni size) {
        this.size = size;
    }

    public Animation<TextureRegion> getAnimazione() {
        return this.graphics.getAnimazione();
    }

    @Override
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

    public Rectangle range() {
        return range;
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
        coordinate = new Vector2(x - size.getWidth() / 4, y - size.getHeight() / 4);
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
