package progetto.gameplay.entities.components.specific;

import progetto.gameplay.entities.components.base.Component;


public class DespawnAnimationComponent extends Component{
    public float accumulator = 0f;
    public final float dissolve_duration = 0.6f;
}
