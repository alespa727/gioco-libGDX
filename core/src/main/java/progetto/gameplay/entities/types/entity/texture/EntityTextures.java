package progetto.gameplay.entities.types.entity.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import progetto.gameplay.entities.types.entity.Entity;

public abstract class EntityTextures {
    protected Texture texture;
    protected final ObjectMap<Vector2, Animation<TextureRegion>> animations;

    public EntityTextures(Texture texture) {
        this.texture = texture;
        animations = new ObjectMap<>();
    }
    public abstract Animation<TextureRegion> getAnimation(Entity e);
    public abstract void salvaAnimazioni(Texture entity);
}
