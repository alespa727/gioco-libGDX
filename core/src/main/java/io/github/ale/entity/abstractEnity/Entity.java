package io.github.ale.entity.abstractEnity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import io.github.ale.entity.abstractEnity.caratteristiche.Dimensioni;
import io.github.ale.entity.abstractEnity.caratteristiche.EntityInfo;
import io.github.ale.entity.abstractEnity.caratteristiche.Hitbox;
import io.github.ale.entity.abstractEnity.graphics.EntityGraphics;
import io.github.ale.entity.abstractEnity.state.Direzione;
import io.github.ale.entity.abstractEnity.state.EntityState;
import io.github.ale.entity.abstractEnity.stats.Stats;
import io.github.ale.interfaces.Creatable;

public abstract class Entity implements io.github.ale.interfaces.Drawable, Creatable{

    public float cooldownAttack = 0; // Tempo rimanente prima del prossimo attacco
    public float cooldownFollowing = 0; // Tempo rimanente prima del prossimo attacco

    private EntityInfo info;
    private Dimensioni size;
    private EntityState stati;
    private Stats statistiche;
    private Vector3 coordinate;
    private Direzione direzione;
    private Hitbox hitbox;
    private EntityGraphics graphics;

    @Override
    public float getY() { return coordinate.y; }
    public void setY(float y) {this.coordinate.y = y;}

    @Override
    public float getX() { return coordinate.x; }
    public void setX(float x) { this.coordinate.x = x; }

    @Override
    public Dimensioni getSize() { return size; }
    public void setSize(Dimensioni size) { this.size = size; }

    @Override
    public Animation<TextureRegion> getAnimazione() { return this.graphics.getAnimazione(); }

    @Override
    public void setAnimation() { this.graphics.setAnimation(this); }
    /**
     * Inizializza textures
     */
    public void inizializzaEntityGraphics(){
        this.graphics = new EntityGraphics();
    }
    
    /**
     * Inizializza le info dell'entita
     * @param nome
     * @param descrizione
     */
    public void inizializzaInfo(String nome, String descrizione){
        this.info = new EntityInfo(nome, descrizione);
    }

    /**
     * inizializza le coordinate
     * @param x
     * @param y
     */
    public void inizializzaCoordinate(float x, float y){
        coordinate = new Vector3(x, y, 0);
    }

    /**
     * inizializza hitbox entità
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void inizializzaHitbox(float x, float y, float width, float height){ 
        hitbox = new Hitbox(x, y, width, height);
    }

    /**
     * inizializza direzione entità
     * @param direzione
     */
    public void inizializzaDirezione(String direzione){ 
        this.direzione = new Direzione();
        this.direzione.setDirezione(direzione);
    }
    
    /**
     * inizializza stati entità
     * @param isAlive
     * @param inCollisione
     * @param isMoving
     */
    public void inizializzaStati(boolean isAlive, boolean inCollisione, boolean isMoving){
        stati = new EntityState();
        stati.setIsAlive(isAlive);
        stati.setInCollisione(inCollisione); 
        stati.setIsMoving(isMoving);
    }

    /**
     * inizializza animazione entità
     */
    public void inizializzaAnimazione() {
        this.graphics.inizializzaAnimazione(this);
     }

    /**
     * inizializza Dimensione della texture dell'entità
     * @param size
     */
    public void inizializzaDimensione(Dimensioni size){
        this.size = size;
    }

    /**
     * inizializza le statistiche basiche dell'entità
     * @param hp
     * @param speed
     * @param attackdmg
     */
    public void inizializzaStatistiche(float hp, float speed, float attackdmg){ 
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
    public EntityGraphics getEntityGraphics(){ return graphics; }
 

}
