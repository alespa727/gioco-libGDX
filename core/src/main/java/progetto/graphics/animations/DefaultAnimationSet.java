package progetto.graphics.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import progetto.ECS.components.specific.movement.DirectionComponent;
import progetto.ECS.entities.Entity;

/**
 * Gestisce un set di animazioni predefinite per un'entità, comprese animazioni di movimento
 * e idle in diverse direzioni (su, giù, sinistra, destra) e diagonali.
 */
public class DefaultAnimationSet extends AnimationSet {

    /** Animazioni di movimento nelle direzioni principali */
    private final TextureRegion[] movingUp = new TextureRegion[4];
    /** Animazioni di movimento nelle direzioni principali */
    private final TextureRegion[] movingDown = new TextureRegion[4];
    /** Animazioni di movimento nelle direzioni principali */
    private final TextureRegion[] movingLeft = new TextureRegion[4];
    /** Animazioni di movimento nelle direzioni principali */
    private final TextureRegion[] movingRight = new TextureRegion[4];

    /** Animazioni di idle nelle direzioni principali */
    private final TextureRegion[] idleUp = new TextureRegion[2];
    /** Animazioni di movimento nelle direzioni principali */
    private final TextureRegion[] idleDown = new TextureRegion[2];
    /** Animazioni di movimento nelle direzioni principali */
    private final TextureRegion[] idleLeft = new TextureRegion[2];
    /** Animazioni di movimento nelle direzioni principali */
    private final TextureRegion[] idleRight = new TextureRegion[2];

    /**
     * Costruisce il set di animazioni per un'immagine texture.
     * Imposta le animazioni di movimento e idle per tutte le direzioni.
     *
     * @param img La texture da utilizzare per le animazioni.
     */
    public DefaultAnimationSet(Texture img) {
        super(img);

        salvaAnimazioni();

        float offset = MathUtils.random(0f, 0.1f);
        float animSpeed = 1f / 5f + offset;
        float idleSpeed = 0.5f + offset;

        // Animazioni in movimento
        putAnim(animSpeed, new Vector2(0, 1f), movingUp);      // su
        putAnim(animSpeed, new Vector2(0, -1f), movingDown);   // giù
        putAnim(animSpeed, new Vector2(-1f, 0), movingLeft);   // sinistra
        putAnim(animSpeed, new Vector2(1f, 0), movingRight);   // destra

        // Diagonali
        putAnim(animSpeed, new Vector2(-1f, 1f), movingLeft);
        putAnim(animSpeed, new Vector2(-1f, -1f), movingLeft);
        putAnim(animSpeed, new Vector2(1f, -1f), movingRight);
        putAnim(animSpeed, new Vector2(1f, 1f), movingRight);

        // Idle (mezze direzioni)
        putAnim(idleSpeed, new Vector2(0, 0.5f), idleUp);
        putAnim(idleSpeed, new Vector2(0, -0.5f), idleDown);
        putAnim(idleSpeed, new Vector2(-0.5f, 0), idleLeft);
        putAnim(idleSpeed, new Vector2(0.5f, 0), idleRight);

        putAnim(idleSpeed, new Vector2(0, 0), idleDown);

        putAnim(idleSpeed, new Vector2(-0.5f, 0.5f), idleLeft);
        putAnim(idleSpeed, new Vector2(-0.5f, -0.5f), idleLeft);
        putAnim(idleSpeed, new Vector2(0.5f, -0.5f), idleRight);
        putAnim(idleSpeed, new Vector2(0.5f, 0.5f), idleRight);
    }

    /**
     * Carica le animazioni da una texture, suddivisa in frame.
     * Assegna i frame corretti a ciascuna direzione di movimento e idle.
     */
    public void salvaAnimazioni() {
        TextureRegion[][] tmpFrames = TextureRegion.split(texture, 32, 32);

        for (int i = 0; i < 4; i++) {
            movingUp[i] = tmpFrames[5][i];
            movingDown[i] = tmpFrames[3][i];
            movingRight[i] = tmpFrames[4][i];

            movingLeft[i] = new TextureRegion(tmpFrames[4][i]);
            movingLeft[i].flip(true, false);
        }

        for (int i = 0; i < 2; i++) {
            idleUp[i] = tmpFrames[2][i];
            idleDown[i] = tmpFrames[0][i];
            idleRight[i] = tmpFrames[1][i];

            idleLeft[i] = new TextureRegion(tmpFrames[1][i]);
            idleLeft[i].flip(true, false);
        }
    }

    /**
     * Aggiunge un'animazione alla mappa di animazioni, associandola a una direzione.
     *
     * @param speed La velocità dell'animazione.
     * @param direction La direzione dell'animazione.
     * @param frames I frame dell'animazione.
     */
    private void putAnim(float speed, Vector2 direction, TextureRegion[] frames) {
        animations.put(direction, new Animation<>(speed, frames));
    }

    /**
     * Restituisce l'animazione corretta in base alla direzione dell'entità
     */
    @Override
    public TextureRegion play(Entity e, float elapsedTime) {
        return animations.get(e.get(DirectionComponent.class).direction).getKeyFrame(elapsedTime, true);
    }

    /**
     * Restituisce l'animazione corretta in base alla direzione
     */
    @Override
    public TextureRegion play(Vector2 key, float elapsedTime) {
        return animations.get(key).getKeyFrame(elapsedTime, true);
    }
}
