package io.github.ale.screens.game.entities.skill.skillist;
import io.github.ale.screens.game.entityType.mobs.LivingEntity;

public abstract class Skill {

    protected LivingEntity entity;

    public final String name;
    public final String description;

    public Skill(LivingEntity entity, String name, String description){
        this.entity = entity;
        this.name=name;
        this.description=description;
    }

    public abstract void execute();
}
