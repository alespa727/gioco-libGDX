package io.github.ale.screens.gameplay.entities.skills.skillType;

import io.github.ale.screens.gameplay.entities.types.mobs.LivingEntity;
import io.github.ale.screens.gameplay.entities.skills.Skill;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(LivingEntity entity, String name, String description, float damage) {
        super(entity, name, description);
        this.damage = damage;
    }

}
