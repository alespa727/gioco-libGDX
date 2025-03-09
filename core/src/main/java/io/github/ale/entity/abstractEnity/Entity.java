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
import io.github.ale.entity.abstractEnity.texture.TexturesEntity;
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
    
    private TexturesEntity texture;
    private Animation<TextureRegion> animation;

    @Override
    public float getY() { return coordinate.y; }
    public void setY(float y) {this.coordinate.y = y;}

    @Override
    public float getX() { return coordinate.x; }
    public void setX(float x) { this.coordinate.x = x; }


    
    public void inizializzaInfo(String nome, String descrizione){
        this.info = new EntityInfo(nome, descrizione);
    }

    public void inizializzaCoordinate(float x, float y){
        coordinate = new Vector3(x, y, 0);
    }


    public void setStatistiche(float hp, float speed, float attackdmg){ 
        this.statistiche = new Stats(hp, speed, attackdmg); 
    }

    public void setDirezione(String direzione){ 
        this.direzione.setDirezione(direzione);
    }

    public String getDirezione(){ 
        return this.direzione.getDirezione();
    }
    
    public boolean isAlive(){ 
        return stati.isAlive(); 
    }

    public void setIsAlive(boolean isAlive){ 
        stati.setIsAlive(isAlive);
    }

    public Rectangle getHitbox(){ 
        return this.hitbox.getHitbox(); 
    }

    public void inizializzaHitbox(float x, float y, float width, float height){ 
        hitbox = new Hitbox(x, y, width, height);
    }

    public void inizializzaDirezione(String direzione){ 
        this.direzione = new Direzione();
        this.direzione.setDirezione(direzione);
    }
    
    public void inizializzaStati(boolean isAlive, boolean inCollisione, boolean isMoving){
        stati = new EntityState();
        stati.setIsAlive(isAlive);
        stati.setInCollisione(inCollisione); 
        stati.setIsMoving(isMoving);
    }
    
    public Stats getStatistiche(){
        return this.statistiche;
    }
    
    

    

    public void inizializzaDimensione(Dimensioni size){
        this.size = size;
    }

    
    public Direzione getDirezioneObject(){ return this.direzione;}

    public void setIsMoving( boolean isMoving){ this.stati.setIsMoving(isMoving); }

    
    public boolean inCollisione(){ return stati.isInCollisione(); }
    public boolean isMoving(){ return stati.isMoving(); }

    
    public void setInCollisione(boolean inCollisione){ stati.setInCollisione(inCollisione); }
    public void setIsmoving(boolean isMoving){ stati.setIsMoving(isMoving);}


    public boolean checkIfDead(){

        //DA IMPLEMENTARE
        return true;
    }

    /**
     * setta l'animazione attuale da utilizzare
     */

    @Override
    public void setAnimation() {
        if (animation == null || animation != texture.setAnimazione(getDirezione())) {
            animation = texture.setAnimazione(getDirezione());
        }
    }

    public void inizializzaAnimazione() {
        animation = getTexture().setAnimazione(getDirezione());
    }

    @Override
    public Animation<TextureRegion> getAnimazione() {
        return animation;
    }

    public TexturesEntity getTexture(){
        return texture;
    }

    public void setTexture(String path){
        texture = new TexturesEntity(path);
    }

    public void setSize(Dimensioni size) {
        this.size = size;
    }

    @Override
    public Dimensioni getSize() {
        return size;
    }

    public String getNome(){
        return this.info.getNome();
    }

    public String getDescrizione(){
        return this.info.getDescrizione();
    }

 

}
