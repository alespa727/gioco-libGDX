package progetto.gameplay.entities.skills.specific;

import progetto.gameplay.entities.skills.base.Skill;
import progetto.gameplay.entities.specific.specific.living.Humanoid;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(Humanoid entity, String name, String description, float damage) {
        super(entity, name, description);
        this.damage = damage;
    }

}
