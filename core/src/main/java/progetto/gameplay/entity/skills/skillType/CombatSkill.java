package progetto.gameplay.entity.skills.skillType;

import progetto.gameplay.entity.types.humanEntity.HumanEntity;
import progetto.gameplay.entity.skills.Skill;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(HumanEntity entity, String name, String description, float damage) {
        super(entity, name, description);
        this.damage = damage;
    }

}
