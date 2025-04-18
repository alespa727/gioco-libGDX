package progetto.skills.specific;

import progetto.entity.specific.specific.living.Humanoid;
import progetto.skills.base.Skill;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(Humanoid entity, String name, String description, float damage) {
        super(entity, name, description);
        this.damage = damage;
    }

}
