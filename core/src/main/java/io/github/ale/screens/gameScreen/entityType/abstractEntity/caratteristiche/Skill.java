package io.github.ale.screens.gameScreen.entityType.abstractEntity.caratteristiche;

import io.github.ale.screens.gameScreen.entityType.livingEntity.LivingEntity;

public abstract class Skill {

    protected LivingEntity entity;
    private final String name;
    private final String description;

    public float countdown = 0;
    public float cooldown = 0.8f;
    public boolean executed=false;

    public Skill(LivingEntity entity, String name, String description){
        this.entity=entity;
        this.name=name;
        this.description=description;
    }

    public String description() {
        return description;
    }

    public String setName() {
        return name;
    }

    public abstract void execute();
}
