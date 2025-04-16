package progetto.gameplay.entity.components.humanoid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import progetto.gameplay.entity.components.Component;
import progetto.gameplay.entity.types.living.Humanoid;

public class HumanoidDrawerComponent extends Component {
    Humanoid entity;
    SpriteBatch batch;
    float tempoTrascorso;

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
        if (entity.getHumanStates().hasBeenHit()) {
            batch.setColor(1, 0, 0, 0.6f);
        }
        batch.draw(entity.getTextures().getAnimation(entity).getKeyFrame(tempoTrascorso, true),
            entity.getPosition().x - entity.getConfig().imageWidth / 2,
            entity.getPosition().y - entity.getConfig().imageHeight / 2,
            entity.getConfig().imageWidth, entity.getConfig().imageHeight);
        batch.setColor(Color.WHITE);
    }
}
