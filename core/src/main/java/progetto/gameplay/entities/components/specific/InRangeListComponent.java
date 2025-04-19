package progetto.gameplay.entities.components.specific;

import com.badlogic.gdx.utils.Array;
import progetto.gameplay.entities.components.base.Component;
import progetto.gameplay.entities.specific.specific.living.combat.Warrior;

public class InRangeListComponent extends Component{
    public final Array<Warrior> inRange;

    public InRangeListComponent() {
        this.inRange = new Array<>();
    }
}
