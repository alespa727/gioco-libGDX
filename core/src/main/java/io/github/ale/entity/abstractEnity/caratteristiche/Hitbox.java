package io.github.ale.entity.abstractEnity.caratteristiche;

import com.badlogic.gdx.math.Rectangle;

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
    
}
