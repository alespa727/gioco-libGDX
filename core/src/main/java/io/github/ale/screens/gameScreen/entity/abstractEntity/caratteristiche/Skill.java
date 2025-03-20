package io.github.ale.screens.gameScreen.entity.abstractEntity.caratteristiche;

import io.github.ale.screens.gameScreen.entity.abstractEntity.Entity;

public abstract class Skill {
    String skillName;
    String skillDescription;

    public Entity entity;
    public float countdown = 0;
    public float cooldown = 0.8f;

    public String getSkillDescription() {
        return skillDescription;
    }

    public String getSkillName() {
        return skillName;
    }

    public abstract void attack();
}
