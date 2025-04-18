package progetto.graphics.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import progetto.gameplay.entities.specific.base.Entity;

public abstract class AnimationSet {

    // === Risorse grafiche ===
    protected final Texture texture;
    protected final ObjectMap<Vector2, Animation<TextureRegion>> animations;

    // === Costruttore ===
    public AnimationSet(Texture texture) {
        this.texture = texture;
        this.animations = new ObjectMap<>();
    }

    // === Metodi astratti da implementare nelle sottoclassi ===
    public abstract TextureRegion play(Entity e, float elapsedTime);
}
