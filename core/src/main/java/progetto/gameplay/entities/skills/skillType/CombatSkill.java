package progetto.gameplay.entities.skills.skillType;

import progetto.gameplay.entities.types.HumanEntity;
import progetto.gameplay.entities.skills.Skill;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(HumanEntity entity, String name, String description, float damage) {
        super(entity, name, description);
        this.damage = damage;
    }

}
