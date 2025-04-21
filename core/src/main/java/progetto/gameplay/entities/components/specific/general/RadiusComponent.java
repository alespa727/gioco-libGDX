package progetto.gameplay.entities.components.specific.general;

import progetto.gameplay.entities.components.base.Component;

public class RadiusComponent extends Component {
    private float radius;

    public RadiusComponent(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }
}
