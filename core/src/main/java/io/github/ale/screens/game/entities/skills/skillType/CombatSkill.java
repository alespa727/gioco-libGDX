package io.github.ale.screens.game.entities.skills.skillType;

import io.github.ale.screens.game.entities.skills.Skill;
import io.github.ale.screens.game.entities.entityTypes.mobs.LivingEntity;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(LivingEntity entity, String name, float damage, String description) {
        super(entity, name, description);
        this.damage = damage;
    }
}
