package progetto.entity.components.specific.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import progetto.entity.components.base.Component;
import progetto.entity.entities.specific.living.Humanoid;
import progetto.graphics.animations.CustomAnimation;

public class ShadowComponent extends Component {
    public final CustomAnimation animation;

    public ShadowComponent(Humanoid entity) {
        String[] string = new String[]{"default"};
        Texture texture = new Texture(Gdx.files.internal("entities/" + entity.getClass().getSimpleName() + "_shadow.png"));
        Texture[] textures = new Texture[]{texture};
        animation = new CustomAnimation(string, textures);
    }
}
