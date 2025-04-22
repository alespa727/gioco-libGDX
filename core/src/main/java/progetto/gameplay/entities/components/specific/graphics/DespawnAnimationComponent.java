package progetto.gameplay.entities.components.specific.graphics;

import progetto.gameplay.entities.components.base.Component;


public class DespawnAnimationComponent extends Component {
    public final float dissolve_duration = 0.6f;
    public float accumulator = 0f;
}
