package progetto.ECS.components.specific.graphics;

import com.badlogic.gdx.graphics.Texture;
import progetto.ECS.components.base.Component;

public class TextureComponent extends Component {
    private final Texture texture;

    public TextureComponent(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }
}
