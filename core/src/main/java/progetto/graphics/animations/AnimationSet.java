package progetto.graphics.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import progetto.ECS.entities.Entity;

/**
 * Classe astratta per gestire un set di animazioni.
 * Ogni animazione è associata a una texture e a una posizione 2D.
 */
public abstract class AnimationSet {

    protected final Texture texture; // Texture per le animazioni
    protected final ObjectMap<Vector2, Animation<TextureRegion>> animations; // Mappa animazioni

    /**
     * Costruisce un AnimationSet con una texture.
     * @param texture la texture per le animazioni.
     */
    public AnimationSet(Texture texture) {
        this.texture = texture;
        this.animations = new ObjectMap<>();
    }

    /**
     * Esegue l'animazione per un'entità.
     * @param e l'entità da animare
     * @param elapsedTime il tempo trascorso
     * @return il frammento di animazione corrente
     */
    public abstract TextureRegion play(Entity e, float elapsedTime);

    /**
     * Esegue l'animazione per una chiave (es. posizione).
     * @param key la chiave per l'animazione
     * @param elapsedTime il tempo trascorso
     * @return il frammento di animazione corrente
     */
    public abstract TextureRegion play(Vector2 key, float elapsedTime);
}
