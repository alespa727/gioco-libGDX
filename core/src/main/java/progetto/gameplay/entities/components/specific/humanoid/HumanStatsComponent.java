package progetto.gameplay.entities.components.specific.humanoid;

import progetto.gameplay.entities.components.base.Component;

public class HumanStatsComponent extends Component {
    private final float maxSpeed;
    private final float maxHealth;
    private float health;
    private float speedMultiplier=1f;
    public HumanStatsComponent(float maxSpeed, float maxHealth) {
        this.maxSpeed = maxSpeed;
        this.maxHealth = maxHealth;
        health = maxHealth;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getHealth() {
        return health;
    }
}
