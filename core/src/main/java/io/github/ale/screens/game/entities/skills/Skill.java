package io.github.ale.screens.game.entities.skills;

import io.github.ale.screens.game.entities.types.mobs.LivingEntity;

public abstract class Skill {

    public final String name;
    public final String description;
    protected LivingEntity entity;
    private boolean isBeingUsed = false;

    public Skill(LivingEntity entity, String name, String description) {
        this.entity = entity;
        this.name = name;
        this.description = description;
    }

    public boolean isBeingUsed() {
        return isBeingUsed;
    }

    public void setBeingUsed(boolean isBeingUsed) {
        this.isBeingUsed = isBeingUsed;
    }

    public abstract void execute();
}
