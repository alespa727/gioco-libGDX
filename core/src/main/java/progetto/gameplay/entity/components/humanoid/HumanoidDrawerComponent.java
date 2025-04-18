package progetto.gameplay.entity.components.humanoid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.gameplay.entity.components.Component;
import progetto.gameplay.entity.types.living.Humanoid;
import progetto.utils.shaders.Flash;

public class HumanoidDrawerComponent extends Component {
    private final Humanoid entity;
    private SpriteBatch batch;
    private float tempoTrascorso;

    public HumanoidDrawerComponent(Humanoid entity) {
        this.entity = entity;
    }

    public void draw(SpriteBatch batch, float tempoTrascorso) {
        this.batch = batch;
        this.tempoTrascorso = tempoTrascorso;
    }

    public void update() {
        if (batch==null) return;
        batch.setColor(entity.color);

        boolean applied = false;
        if (entity.getHumanStates().hasBeenHit()) {
            Flash.getInstance().apply(batch, Color.RED);
            applied = true;
        }

        batch.draw(entity.getTextures().getAnimation(entity).getKeyFrame(tempoTrascorso, true),
            entity.getPosition().x - entity.getConfig().imageWidth / 2,
            entity.getPosition().y - entity.getConfig().imageHeight / 2,
            entity.getConfig().imageWidth, entity.getConfig().imageHeight);


        if(applied) {
            Flash.getInstance().end(batch);
        }
    }
}
