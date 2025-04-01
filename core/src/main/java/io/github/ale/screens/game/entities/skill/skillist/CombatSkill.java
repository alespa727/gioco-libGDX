package io.github.ale.screens.game.entities.skill.skillist;

import io.github.ale.screens.game.entityType.combat.CombatEntity;
import io.github.ale.screens.game.entityType.mobs.LivingEntity;

public abstract class CombatSkill extends Skill {
    public final float damage;

    public CombatSkill(LivingEntity entity, String name, float damage, String description) {
        super(entity, name, description);
        this.damage = damage;
    }
}
