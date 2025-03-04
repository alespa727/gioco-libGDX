package io.github.ale.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {
    public  Rectangle hitbox;

    public float x;
    public float y;

    public Direzione direzione;

    public  TexturesEntity texture;

    public  Animation<TextureRegion> animation;

    public float elapsedTime;

    public  Health hp;

    public boolean inCollisione;

    public final float baseAttackDamage = 10;
    public float attackMultiplier = 1f;
    public float attackDamage;

    public float cooldownAttack = 0; // Tempo rimanente prima del prossimo attacco
    public float cooldownFollowing = 0; // Tempo rimanente prima del prossimo attacco

    public final float baseSpeed=2.5f;
    public float delta = 1f;
    public float speed;
    public boolean isAlive;

    public abstract void create();
    public abstract void draw(SpriteBatch batch);
    public abstract void drawHitbox(ShapeRenderer renderer);
    public abstract void update();

    public abstract boolean checkIfDead();


    public void setAnimation(){
        animation = texture.setAnimazione(direzione);
    }

    // Getters
    public float getWorldX() {
        return x;
    }

    public float getWorldY() {
        return y;
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
