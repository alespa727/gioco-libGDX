package io.github.ale.screens.game.entities.skills.skillType;

import io.github.ale.screens.game.entities.types.mobs.LivingEntity;
import io.github.ale.screens.game.entities.skills.Skill;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(LivingEntity entity, String name, float damage, String description) {
        super(entity, name, description);
        this.damage = damage;
    }
}
