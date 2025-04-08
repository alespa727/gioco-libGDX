package progetto.gameplay.entity.skills.skillType;

import progetto.gameplay.entity.types.living.Humanoid;
import progetto.gameplay.entity.skills.Skill;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(Humanoid entity, String name, String description, float damage) {
        super(entity, name, description);
        this.damage = damage;
    }

}
