package progetto.ECS.components.specific.graphics;

import com.badlogic.gdx.graphics.Texture;
import progetto.ECS.components.base.Component;
import progetto.graphics.animations.CustomAnimation;

public class CustomAnimationComponent extends Component {
    private final CustomAnimation animation;

    public CustomAnimationComponent(String[] strings, Texture[] texture) {
        animation = new CustomAnimation(strings, texture);
    }

    public CustomAnimation getAnimation() {
        return animation;
    }
}
