package io.github.ale.screens.game.entities.skills;
import io.github.ale.screens.game.entities.entityTypes.mobs.LivingEntity;

public abstract class Skill {

    protected LivingEntity entity;

    public final String name;
    public final String description;

    private boolean isBeingUsed = false;

    public Skill(LivingEntity entity, String name, String description){
        this.entity = entity;
        this.name=name;
        this.description=description;
    }

    public void setBeingUsed(boolean isBeingUsed){
        this.isBeingUsed = isBeingUsed;
    }

    public boolean isBeingUsed() {
        return isBeingUsed;
    }

    public abstract void execute();
}
