package io.github.ale.entity.abstractEnity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import io.github.ale.entity.abstractEnity.state.EntityState;
import io.github.ale.entity.abstractEnity.stats.Health;
import io.github.ale.entity.abstractEnity.stats.Stats;
import io.github.ale.entity.abstractEnity.texture.TexturesEntity;

public abstract class Entity implements io.github.ale.interfaces.Drawable{

    public float elapsedTime;

    private Vector3 coordinate;
    private Direzione direzione;

    private TexturesEntity texture;
    private Animation<TextureRegion> animation;

    public float cooldownAttack = 0; // Tempo rimanente prima del prossimo attacco
    public float cooldownFollowing = 0; // Tempo rimanente prima del prossimo attacco

    private EntityState stati;
    public Stats statistiche;

    protected Rectangle hitbox;
    private Dimensioni size;

    protected abstract void create();
    public abstract void drawHitbox(ShapeRenderer renderer);

    public void inizializzaCoordinate(float x, float y){
        coordinate = new Vector3(x, y, 0);
    }

    public float getY() { return coordinate.y; }
    public void setY(float y) {this.coordinate.y = y;}

    public float getX() { return coordinate.x; }
    public void setX(float x) { this.coordinate.x = x; }

    public void setStatistiche(float hp, float speed, float attackdmg){ this.statistiche = new Stats(hp, speed, attackdmg); }

    public void inizializzaDirezione(String direzione){ 
        this.direzione = new Direzione();
        this.direzione.setDirezione(direzione);
    }

    public void inizializzaDimensione(Dimensioni size){
        this.size = size;
    }

    public void setDirezione(String direzione){ this.direzione.setDirezione(direzione);}
    public String getDirezione(){ return this.direzione.getDirezione();}
    public Direzione getDirezioneObject(){ return this.direzione;}

    public void setIsMoving( boolean isMoving){ this.stati.setIsMoving(isMoving); }

    public boolean isAlive(){ return stati.isAlive(); }
    public boolean inCollisione(){ return stati.isInCollisione(); }
    public boolean isMoving(){ return stati.isMoving(); }

    public void setIsAlive(boolean isAlive){ stati.setIsAlive(isAlive); }
    public void setInCollisione(boolean inCollisione){ stati.setInCollisione(inCollisione); }
    public void setIsmoving(boolean isMoving){ stati.setIsMoving(isMoving);}

    public Rectangle getHitbox(){ return this.hitbox; }
    public void inizializzaHitbox(float x, float y, float width, float height){ hitbox = new Rectangle(x, y, width, height);}
    public Health getHealth() { return this.statistiche.getHealthObject(); } 

    public void setStati(boolean isAlive, boolean inCollisione, boolean isMoving){
        stati = new EntityState();
        stati.setIsAlive(isAlive);
        stati.setInCollisione(inCollisione); 
        stati.setIsMoving(isMoving);
    }

    public boolean checkIfDead(){

        //DA IMPLEMENTARE
        return true;
    }

    /**
     * setta l'animazione attuale da utilizzare
     */

    public void setAnimation() {
        if (animation == null || animation != texture.setAnimazione(getDirezione())) {
            animation = texture.setAnimazione(getDirezione());
        }
    }

    public void inizializzaAnimazione() {
        animation = getTexture().setAnimazione(getDirezione());
    }

    public Animation<TextureRegion> getAnimazione() {
        return animation;
    }

    public TexturesEntity getTexture(){
        return texture;
    }

    public void setTexture(String path){
        texture = new TexturesEntity(path);
    }

    public Dimensioni getSize() {
        return size;
    }

    public void setSize(Dimensioni size) {
        this.size = size;
    }
 

}
