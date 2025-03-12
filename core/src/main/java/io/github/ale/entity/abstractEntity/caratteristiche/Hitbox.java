package io.github.ale.entity.abstractEntity.caratteristiche;

import com.badlogic.gdx.math.Rectangle;

import io.github.ale.entity.abstractEntity.Entity;

public class Hitbox {
    private Rectangle hitbox;

    public Hitbox(float x, float y, float width, float height){
        hitbox = new Rectangle(x, y, width, height);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public void adjust(Entity e){
        this.hitbox.x = e.getX() + 0.65f;
        this.hitbox.y = e.getY() + 0.55f;
    }
    
}
