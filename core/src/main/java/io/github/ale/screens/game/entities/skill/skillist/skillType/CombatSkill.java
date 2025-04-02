package io.github.ale.screens.game.entities.skill.skillist.skillType;

import io.github.ale.screens.game.entities.skill.skillist.Skill;
import io.github.ale.screens.game.entities.entityTypes.mobs.LivingEntity;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(LivingEntity entity, String name, float damage, String description) {
        super(entity, name, description);
        this.damage = damage;
    }
}
