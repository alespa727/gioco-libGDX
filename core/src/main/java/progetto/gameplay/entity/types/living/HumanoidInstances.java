package progetto.gameplay.entity.types.living;

import progetto.gameplay.entity.skills.SkillSet;
import progetto.gameplay.entity.types.EntityInstance;

public class HumanoidInstances extends EntityInstance {

    // === Attributi specifici dell'umano ===
    public final SkillSet skillset;
    public final float speed;
    public final float maxHealth;

    // === Costruttore ===
    public HumanoidInstances(Humanoid e) {
        super(e);
        this.skillset = e.getSkillset();
        this.maxHealth = e.getMaxHealth();
        this.speed = e.getSpeed();
    }
}
