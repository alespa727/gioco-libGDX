package progetto.gameplay.entities.components.specific.humanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.specific.base.Entity;


public class DespawnComponent extends Component{
    private final Entity owner;
    private float accumulator = 0f;
    private final float dissolve_duration = 0.6f;

    public DespawnComponent(Entity e) {
        this.owner = e;
    }

    public void draw(SpriteBatch batch, TextureRegion texture, float x, float y, float width, float height) {
        accumulator += Gdx.graphics.getDeltaTime();

        // Calcola il progresso interpolato
        float progress = Math.min(accumulator / dissolve_duration, 1f);
        float alpha = Interpolation.fade.apply(1f - progress);

        // Applica trasparenza
        batch.setColor(1, 1, 1, alpha);
        batch.draw(texture, x, y, width, height);
        batch.setColor(Color.WHITE);

        // Despawn quando finisce
        if (accumulator >= dissolve_duration) {
            owner.despawn();
        }
    }

}
