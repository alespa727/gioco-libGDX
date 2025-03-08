package io.github.ale.entity.abstractEnity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import io.github.ale.entity.Direzione;
import io.github.ale.entity.abstractEnity.stats.Health;
import io.github.ale.entity.abstractEnity.stats.Stats;
import io.github.ale.entity.abstractEnity.texture.TexturesEntity;

public abstract class Entity {
    protected Rectangle hitbox;

    private float x;
    private float y;
    public Direzione direzione;

    private boolean isMoving;

    private TexturesEntity texture;

    public Animation<TextureRegion> animation;

    public float elapsedTime;

    public float cooldownAttack = 0; // Tempo rimanente prima del prossimo attacco
    public float cooldownFollowing = 0; // Tempo rimanente prima del prossimo attacco

    public boolean isAlive;

    public boolean inCollisione;

    public Stats statistiche;

    public Health hp;

    

    protected abstract void create();
    public abstract void draw(SpriteBatch batch);
    public abstract void drawHitbox(ShapeRenderer renderer);

    public float getY() { return y; }
    public void setY(float y) {this.y = y;}

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public void setStatistiche(float hp, float speed, float attackdmg){ this.statistiche = new Stats(hp, speed, attackdmg); }

    public void setDirezione(String direzione){ this.direzione.setDirezione(direzione);}
    public String getDirezione(){ return this.direzione.getDirezione();}

    public void setIsMoving( boolean isMoving){ this.isMoving = isMoving; }
    public boolean getIsMoving(){ return this.isMoving; }



    public boolean checkIfDead(){
        return true;
    }

    /**
     * setta l'animazione attuale da utilizzare
     */

    public void setAnimation() {
        if (animation == null || animation != texture.setAnimazione(direzione)) {
            animation = texture.setAnimazione(direzione);
        }
    }

    public TexturesEntity getTexture(){
        return texture;
    }

    public void setTexture(String path){
        texture = new TexturesEntity(path);
    }

    public Health getHealth() {
        return this.hp;
    }

    public Rectangle getHitbox(){
        return this.hitbox;
    }

    public boolean getInCollisione(){
        return inCollisione;
    }

    // Getters
    public void setWorldX(float x) {
        this.x = x;
    }

    public void setWorldY(float y) {
        this.y = y;
    }

    public void setInCollisione(boolean state){
        inCollisione = state;
    }

    

    
}
