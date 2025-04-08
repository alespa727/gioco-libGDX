package progetto.gameplay.entity.types.living;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.entity.types.Entity;
import progetto.gameplay.entity.types.EntityTextures;

public class HumanoidTextures extends EntityTextures {

    private final TextureRegion[] movingUp = new TextureRegion[4];
    private final TextureRegion[] movingDown = new TextureRegion[4];
    private final TextureRegion[] movingLeft = new TextureRegion[4];
    private final TextureRegion[] movingRight = new TextureRegion[4];

    private final TextureRegion[] idleUp = new TextureRegion[2];
    private final TextureRegion[] idleDown = new TextureRegion[2];
    private final TextureRegion[] idleLeft = new TextureRegion[2];
    private final TextureRegion[] idleRight = new TextureRegion[2];

    public HumanoidTextures(Texture img) {
        super(img);

        salvaAnimazioni();

        float offset = MathUtils.random(0f, 0.2f);
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

        putAnim(idleSpeed, new Vector2(-0.5f, 0.5f), idleLeft);
        putAnim(idleSpeed, new Vector2(-0.5f, -0.5f), idleLeft);
        putAnim(idleSpeed, new Vector2(0.5f, -0.5f), idleRight);
        putAnim(idleSpeed, new Vector2(0.5f, 0.5f), idleRight);
    }

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

    private void putAnim(float speed, Vector2 direction, TextureRegion[] frames) {
        animations.put(direction, new Animation<>(speed, frames));
    }

    /** Restituisce l'animazione corretta in base alla direzione dell'entità */
    @Override
    public Animation<TextureRegion> getAnimation(Entity e) {
        return animations.get(e.direzione());
    }
}
