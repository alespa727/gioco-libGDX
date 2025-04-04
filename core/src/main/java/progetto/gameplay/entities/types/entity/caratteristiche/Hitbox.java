package progetto.gameplay.entities.types.entity.caratteristiche;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entities.types.entity.Entity;

public class Hitbox {
    private final Vector2 offset;
    private Rectangle hitbox;

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

    public void adjust(Entity e) {
        this.hitbox.x = e.getX() + e.config().imageWidth / 2 - this.hitbox.width / 2 + offset.x;
        this.hitbox.y = e.getY() + e.config().imageHeight / 2 - this.hitbox.height / 2 + offset.y;
    }

}
