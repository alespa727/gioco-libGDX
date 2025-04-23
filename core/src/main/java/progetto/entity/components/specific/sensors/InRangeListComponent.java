package progetto.entity.components.specific.sensors;

import com.badlogic.gdx.utils.Array;
import progetto.entity.components.base.Component;
import progetto.entity.entities.specific.living.combat.Warrior;

public class InRangeListComponent extends Component {
    public final Array<Warrior> inRange = new Array<>();
}
