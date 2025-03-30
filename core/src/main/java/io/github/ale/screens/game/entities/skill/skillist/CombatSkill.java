package io.github.ale.screens.game.entities.skill.skillist;

import io.github.ale.screens.game.entityType.combat.CombatEntity;

public abstract class CombatSkill extends Skill {
    protected CombatEntity entity;

    public CombatSkill(CombatEntity entity, String name, String description) {
        super(name, description);
        this.entity = entity;
    }
}
