package progetto.gameplay.entities.specific.specific.living;

import progetto.gameplay.entities.skills.base.SkillSet;
import progetto.gameplay.entities.specific.base.EntityInstance;

import java.util.List;
public class HumanoidInstances extends EntityInstance {

    // === Attributi specifici dell'umano ===
    public final transient SkillSet skillset;
    public List<String> skillNames;
    public final float speed;
    public final float maxHealth;
    public final float health;

    public HumanoidInstances(){
        super();
        this.skillset = new SkillSet();
        this.maxHealth = 0;
        this.speed = 0;
        this.health = 0;
    }

    // === Costruttore ===
    public HumanoidInstances(Humanoid e) {
        super(e);
        this.skillset = e.getSkillset();
        this.maxHealth = e.getStats().maxHealth;
        this.speed = e.getMaxSpeed();
        this.health = e.getHealth();
        this.skillNames = skillset.getSkillNames();
    }

    @Override
    public String toString() {
        return "HumanoidInstances{" +
            super.toString() +
            "skillset=" + skillset +
            ", speed=" + speed +
            ", maxHealth=" + maxHealth +
            ", health=" + health +
            '}';
    }
}
