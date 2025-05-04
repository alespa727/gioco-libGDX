package progetto.ECS.components.specific.graphics;

import progetto.ECS.components.base.Component;


public class DespawnAnimationComponent extends Component {
    public float dissolve_duration = 0.6f;
    public float accumulator = 0f;

    public DespawnAnimationComponent() {

    }

    public DespawnAnimationComponent(float duration) {
        dissolve_duration = duration;
    }
}
