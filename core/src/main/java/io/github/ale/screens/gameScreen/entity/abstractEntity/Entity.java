package io.github.ale.screens.gameScreen.entity.abstractEntity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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

    private float atkCooldown = 0; // Tempo rimanente prima del prossimo attacco

    private final EntityConfig config;

    private EntityInfo info;
    private Dimensioni size;
    private EntityState stati;
    private Stats statistiche;
    private Vector2 coordinate;
    private Direzione direzione;
    private Hitbox hitbox;
    private EntityGraphics graphics;

    public Entity(EntityConfig config) {
        this.config = config;
        inizializzaEntityGraphics();
        inizializzaDimensione(new Dimensioni(config.imageWidth, config.imageHeight));
        inizializzaCoordinate(config.x, config.y);
        inizializzaInfo(config.nome, config.descrizione, config.id);
        getEntityGraphics().setTexture(config.imgpath);
        inizializzaHitbox(getX(), getY(), config.width, config.height);
        inizializzaDirezione(new Vector2(0, -0.5f));
        inizializzaStati(config.isAlive, config.inCollisione, config.isMoving);
        inizializzaStatistiche(config.hp, config.speed, config.attackdmg);
        inizializzaAnimazione();
    }

    public EntityConfig config() {
        return this.config;
    }

    public int id(){ return info.id(); }

    public void render() {
        updateEntity();
        updateEntityType();
    }

    public abstract void updateEntity();

    public abstract void updateEntityType();

    public abstract void drawRange(ShapeRenderer renderer);

    public void kill() {
        statistiche().inflictDamage(statistiche().getHealth(), stati.immortality());
        stati.setIsAlive(false);
    }

    public void respawn() {
        stati().setIsAlive(config.isAlive);
        // this.setX(config.x);
        // this.setY(config.y);
        this.statistiche().gotDamaged = false;
    }

    /**
     * restituisce nome entità
     * 
     * @return
     */
    public String nome() {
        return this.info.getNome();
    }

    /**
     * restituisce descrizione
     * 
     * @return
     */
    public String descrizione() {
        return this.info.getDescrizione();
    }

    /**
     * restituisce la direzione
     * 
     * @return
     */
    public Vector2 direzione() {
        return this.direzione.getDirezione();
    }

    /**
     * restituisce la stats
     * 
     * @return
     */
    @Override
    public Stats statistiche() {
        return this.statistiche;
    }

    /**
     * restituisce la hitbox
     * 
     * @return
     */
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

    @Override
    public final float getY() {
        return coordinate.y;
    }

    public void setY(float y) {
        this.coordinate.y = y;
    }

    @Override
    public final float getX() {
        return coordinate.x;
    }

    public void setX(float x) {
        this.coordinate.x = x;
    }

    @Override
    public Dimensioni getSize() {
        return size;
    }

    public void setSize(Dimensioni size) {
        this.size = size;
    }

    @Override
    public Animation<TextureRegion> getAnimazione() {
        return this.graphics.getAnimazione();
    }

    @Override
    public final void setAnimation() {
        this.graphics.setAnimation(this);
    }

    /**
     * Inizializza textures
     */
    public final void inizializzaEntityGraphics() {
        this.graphics = new EntityGraphics();
    }

    /**
     * Inizializza le info dell'entita
     * 
     * @param nome
     * @param descrizione
     */
    public final void inizializzaInfo(String nome, String descrizione, int id) {
        this.info = new EntityInfo(nome, descrizione, id);
    }

    /**
     * inizializza le coordinate
     * 
     * @param x
     * @param y
     */
    public final void inizializzaCoordinate(float x, float y) {
        coordinate= new Vector2(x-size.getWidth()/4, y-size.getHeight()/4);
    }

    /**
     * inizializza hitbox entità
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public final void inizializzaHitbox(float x, float y, float width, float height) {
        hitbox = new Hitbox(x, y, width, height);
    }

    /**
     * inizializza direzione entità
     * 
     * @param direzione
     */
    public final void inizializzaDirezione(Vector2 direzione) {
        this.direzione = new Direzione();
        this.direzione.setDirezione(direzione);
    }

    /**
     * inizializza stati entità
     * 
     * @param isAlive
     * @param inCollisione
     * @param isMoving
     */
    public final void inizializzaStati(boolean isAlive, boolean inCollisione, boolean isMoving) {
        stati = new EntityState();
        stati.setIsAlive(isAlive);
        stati.setInCollisione(inCollisione);
        stati.setIsMoving(isMoving);
        stati.setImmortality(false);
    }

    /**
     * inizializza animazione entità
     */
    public final void inizializzaAnimazione() {
        this.graphics.inizializzaAnimazione(this);
    }

    /**
     * inizializza Dimensione della texture dell'entità
     * 
     * @param size
     */
    public final void inizializzaDimensione(Dimensioni size) {
        this.size = size;
    }

    /**
     * inizializza le statistiche basiche dell'entità
     * 
     * @param hp
     * @param speed
     * @param attackdmg
     */
    public final void inizializzaStatistiche(float hp, float speed, float attackdmg) {
        this.statistiche = new Stats(hp, speed, attackdmg);
    }

    /**
     * setta la direzione
     * 
     * @param direzione
     */
    public void setDirezione(Vector2 direzione) {
        this.direzione.setDirezione(direzione);
    }

    /**
     * restituisce la hitbox
     * 
     * @return
     */
    public void adjustHitbox() {
        this.hitbox.adjust(this);
    }

    /**
     * restituisce oggetto contenente metodi delle texture
     * 
     * @return
     */
    public final EntityGraphics getEntityGraphics() {
        return graphics;
    }

    public void mantieniNeiLimiti() {
        setX(MathUtils.clamp(getX(), 0 - 0.65f, Map.getWidth() - hitbox().width - hitbox().width));
        setY(MathUtils.clamp(getY(), 0 - 0.55f, Map.getHeight() - hitbox().height - hitbox().height));
    }

    public float atkCooldown() {
        return atkCooldown;
    }

    public void setAtkCooldown(float atkCooldown) {
        this.atkCooldown = atkCooldown;
    }

    public float calcolaAngolo(float x1, float y1, float x2, float y2) {
        float deltaX = x2 - x1; // Change this to calculate delta from (x1, y1) to (x2, y2)
        float deltaY = y2 - y1; // Change this to calculate delta from (x1, y1) to (y2, y2)
        float angolo = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
    
        if (angolo < 0) {
            angolo += 360; // Convert to angle between 0° and 360°
        }
        
        return angolo;
    }
    

}
