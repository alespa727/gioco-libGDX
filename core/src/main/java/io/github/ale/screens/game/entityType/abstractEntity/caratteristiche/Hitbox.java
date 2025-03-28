package io.github.ale.screens.game.entityType.abstractEntity.caratteristiche;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.ale.screens.game.entityType.abstractEntity.Entity;

public class Hitbox {
    private Rectangle hitbox;
    private final Vector2 offset;

    public Hitbox(float x, float y, float width, float height, float offsetX, float offsetY) {
        hitbox = new Rectangle(x, y, width, height);
        offset = new Vector2(offsetX, offsetY);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public void adjust(Entity e){
        this.hitbox.x = e.getX()+e.getSize().width/2-this.hitbox.width/2 + offset.x;
        this.hitbox.y = e.getY()+e.getSize().height/2-this.hitbox.height/2 + offset.y;
    }

}
