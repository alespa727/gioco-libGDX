package progetto.ECS.components.specific.sensors;

import com.badlogic.gdx.utils.Array;
import progetto.ECS.components.base.Component;
import progetto.ECS.entities.specific.living.combat.Warrior;

public class InRangeListComponent extends Component {
    public final Array<Warrior> inRange = new Array<>();
}
