package progetto.ECS.components.specific.graphics;

import com.badlogic.gdx.graphics.Color;
import progetto.ECS.components.base.Component;

public class ColorComponent extends Component {
    public Color color = Color.WHITE.cpy();
}
