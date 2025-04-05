package progetto.gameplay.entity.types.abstractEntity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public abstract class EntityTextures {

    // === Risorse grafiche ===
    protected final Texture texture;
    protected final ObjectMap<Vector2, Animation<TextureRegion>> animations;

    // === Costruttore ===
    public EntityTextures(Texture texture) {
        this.texture = texture;
        this.animations = new ObjectMap<>();
    }

    // === Metodi astratti da implementare nelle sottoclassi ===
    public abstract Animation<TextureRegion> getAnimation(Entity e);
    public abstract void salvaAnimazioni();
}
