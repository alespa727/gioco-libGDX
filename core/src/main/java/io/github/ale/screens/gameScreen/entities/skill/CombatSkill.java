package io.github.ale.screens.gameScreen.entities.skill;

import io.github.ale.screens.gameScreen.entityType.combatEntity.CombatEntity;

public abstract class CombatSkill extends Skill {
    protected CombatEntity entity;

    public CombatSkill(CombatEntity entity, String name, String description) {
        super(name, description);
        this.entity = entity;
    }
}
