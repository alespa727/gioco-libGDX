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

    private TexturesEntity texture;

    public Animation<TextureRegion> animation;

    public float elapsedTime;

    public float cooldownAttack = 0; // Tempo rimanente prima del prossimo attacco
    public float cooldownFollowing = 0; // Tempo rimanente prima del prossimo attacco

    public boolean isAlive;

    public boolean inCollisione;

    Stats statistiche;

    public Health hp;

    

    public float baseAttackDamage = 10;
    public float attackMultiplier = 1f;
    public float attackDamage;

   
    public float baseSpeed=1.5f;
    public float speedMultiplier = 1f;
    public float speed;

    

    protected abstract void create();
    public abstract void draw(SpriteBatch batch);
    public abstract void drawHitbox(ShapeRenderer renderer);

    public float getY() { return y; }
    public void setY(float y) {this.y = y;}

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

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
