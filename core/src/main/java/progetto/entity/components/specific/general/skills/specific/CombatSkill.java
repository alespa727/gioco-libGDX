package progetto.entity.components.specific.general.skills.specific;

import progetto.entity.components.specific.general.skills.base.Skill;
import progetto.entity.entities.specific.living.Humanoid;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(Humanoid entity, String name, String description, float damage) {
        super(entity, name, description);
        this.damage = damage;
    }

}
