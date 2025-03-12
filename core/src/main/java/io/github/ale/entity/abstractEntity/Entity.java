package io.github.ale.entity.abstractEntity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.entity.abstractEntity.caratteristiche.Dimensioni;
import io.github.ale.entity.abstractEntity.caratteristiche.EntityInfo;
import io.github.ale.entity.abstractEntity.caratteristiche.Hitbox;
import io.github.ale.entity.abstractEntity.graphics.EntityGraphics;
import io.github.ale.entity.abstractEntity.state.Direzione;
import io.github.ale.entity.abstractEntity.state.EntityState;
import io.github.ale.entity.abstractEntity.stats.Stats;
import io.github.ale.interfaces.Creatable;
import io.github.ale.maps.Map;

public abstract class Entity implements io.github.ale.interfaces.Drawable, Creatable{

    public float cooldownAttack = 0; // Tempo rimanente prima del prossimo attacco
    public float cooldownFollowing = 0; // Tempo rimanente prima del prossimo attacco

    private EntityInfo info;
    private Dimensioni size;
    private EntityState stati;
    private Stats statistiche;
    private Vector2 coordinate;
    private Direzione direzione;
    private Hitbox hitbox;
    private EntityGraphics graphics;

    public Entity(EntityConfig config){
        inizializzaEntityGraphics();
        inizializzaCoordinate(config.x, config.y);
        getEntityGraphics().setTexture(config.imgpath);
        inizializzaHitbox(getX(), getY(), config.width, config.height);
        inizializzaDirezione(config.direzione);
        inizializzaStati(config.isAlive, config.inCollisione, config.isMoving);
        inizializzaStatistiche(config.hp, config.speed, config.attackdmg);
        inizializzaDimensione(new Dimensioni(config.imageWidth, config.imageHeight));
        inizializzaAnimazione();
    }
    public Vector2 getVector(){
        return coordinate;
    }

    public void setVector(float x, float y){
        coordinate.x = x;
        coordinate.y = y;
    }

    public Vector2 getCenterVector(){
        return new Vector2(getX()+getSize().getWidth()/2, getY()+getSize().getHeight()/2);
    }

    @Override
    public final float getY() { return coordinate.y; }
    public void setY(float y) {this.coordinate.y = y;}

    @Override
    public final float getX() { return coordinate.x; }
    public void setX(float x) { this.coordinate.x = x; }

    @Override
    public Dimensioni getSize() { return size; }
    public void setSize(Dimensioni size) { this.size = size; }

    @Override
    public Animation<TextureRegion> getAnimazione() { return this.graphics.getAnimazione(); }

    @Override
    public final void setAnimation() { this.graphics.setAnimation(this); }
    /**
     * Inizializza textures
     */
    public final void inizializzaEntityGraphics(){
        this.graphics = new EntityGraphics();
    }
    
    /**
     * Inizializza le info dell'entita
     * @param nome
     * @param descrizione
     */
    public final void inizializzaInfo(String nome, String descrizione){
        this.info = new EntityInfo(nome, descrizione);
    }

    /**
     * inizializza le coordinate
     * @param x
     * @param y
     */
    public final void inizializzaCoordinate(float x, float y){
        coordinate = new Vector2(x, y);
    }

    /**
     * inizializza hitbox entità
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public final void inizializzaHitbox(float x, float y, float width, float height){ 
        hitbox = new Hitbox(x, y, width, height);
    }

    /**
     * inizializza direzione entità
     * @param direzione
     */
    public final void inizializzaDirezione(String direzione){ 
        this.direzione = new Direzione();
        this.direzione.setDirezione(direzione);
    }
    
    /**
     * inizializza stati entità
     * @param isAlive
     * @param inCollisione
     * @param isMoving
     */
    public final void inizializzaStati(boolean isAlive, boolean inCollisione, boolean isMoving){
        stati = new EntityState();
        stati.setIsAlive(isAlive);
        stati.setInCollisione(inCollisione); 
        stati.setIsMoving(isMoving);
    }

    /**
     * inizializza animazione entità
     */
    public final void inizializzaAnimazione() {
        this.graphics.inizializzaAnimazione(this);
     }

    /**
     * inizializza Dimensione della texture dell'entità
     * @param size
     */
    public final void inizializzaDimensione(Dimensioni size){
        this.size = size;
    }

    /**
     * inizializza le statistiche basiche dell'entità
     * @param hp
     * @param speed
     * @param attackdmg
     */
    public final void inizializzaStatistiche(float hp, float speed, float attackdmg){ 
        this.statistiche = new Stats(hp, speed, attackdmg); 
    }

    /**
     * setta la direzione
     * @param direzione
     */
    public void setDirezione(String direzione){ 
        this.direzione.setDirezione(direzione);
    }

    /**
     * restituisce la direzione
     * @return
     */
    public String getDirezione(){ 
        return this.direzione.getDirezione();
    }

    /**
     * restituisce la stats
     * @return
     */
    public Stats getStatistiche(){
        return this.statistiche;
    }

    /**
     * restituisce la hitbox
     * @return
     */
    public Rectangle getHitbox(){ 
        return this.hitbox.getHitbox(); 
    }

    /**
     * restituisce la hitbox
     * @return
     */
    public void adjustHitbox(){ 
        this.hitbox.adjust(this);
    }

    /**
     * setta l'animazione attuale da utilizzare
     */

     public EntityState getStati(){
        return stati;
    }

    /**
     * restituisce nome entità
     * @return
     */
    public String getNome(){
        return this.info.getNome();
    }

    /**
     * restituisce descrizione
     * @return
     */
    public String getDescrizione(){
        return this.info.getDescrizione();
    }

    /**
     * restituisce oggetto contenente metodi delle texture
     * @return
     */
    public final EntityGraphics getEntityGraphics(){ return graphics; }

    public void mantieniNeiLimiti(){
        setX(MathUtils.clamp(getX(), 0 - 0.65f, Map.getWidth() - getHitbox().width - getHitbox().width));
        setY(MathUtils.clamp(getY(), 0 - 0.55f, Map.getHeight() - getHitbox().height - getHitbox().height));
    }
 

}
