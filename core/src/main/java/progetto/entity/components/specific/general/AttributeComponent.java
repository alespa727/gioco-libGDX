package progetto.entity.components.specific.general;

import progetto.entity.components.base.Component;

public class AttributeComponent extends Component {
    public final float maxSpeed;
    public final float maxHealth;
    public float health;

    public AttributeComponent(float maxSpeed, float maxHealth) {
        this.maxSpeed = maxSpeed;
        this.maxHealth = maxHealth;
        health = maxHealth;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getHealth() {
        return health;
    }
}
