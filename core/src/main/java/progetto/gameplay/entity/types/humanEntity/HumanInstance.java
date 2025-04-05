package progetto.gameplay.entity.types.humanEntity;

import progetto.gameplay.entity.skills.SkillSet;
import progetto.gameplay.entity.types.abstractEntity.EntityInstance;

public class HumanInstance extends EntityInstance {

    // === Attributi specifici dell'umano ===
    public final SkillSet skillset;
    public final float speed;
    public final float maxHealth;

    // === Costruttore ===
    public HumanInstance(HumanEntity e) {
        super(e);
        this.skillset = e.getSkillset();
        this.maxHealth = e.getMaxHealth();
        this.speed = e.getSpeed();
    }
}
